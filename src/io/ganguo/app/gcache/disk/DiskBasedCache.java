package io.ganguo.app.gcache.disk;

import io.ganguo.app.gcache.Cache;
import io.ganguo.app.gcache.Config;
import io.ganguo.app.gcache.util.CacheUtils;
import io.ganguo.app.gcache.util.GLog;
import io.ganguo.app.gcache.util.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 文件系统缓存
 * 
 * Created by zhihui_chen on 14-8-6.
 */
public class DiskBasedCache implements Cache {
	public static final String TAG = DiskBasedCache.class.getName();

	/**
	 * Map of the Key, CacheHeader pairs
	 */
	private final Map<String, CacheHeader> entries = new LinkedHashMap<String, CacheHeader>(16, .75f, true);

	/**
	 * 缓存占用总大小大小
	 */
	private long totalSize = 0;

	/**
	 * 文件缓存的根目录
	 */
	private File rootDirectory;

	/**
	 * 最多支持存放多少数据
	 */
	private long maxCacheSizeInBytes;

	/**
	 * 需要文件缓存的根目录
	 * 
	 * @param rootDirectory
	 */
	public DiskBasedCache(File rootDirectory) {
		this(rootDirectory, Config.DEFAULT_DISK_USAGE_BYTES);
	}

	/**
	 * 需要文件缓存的根目录 最多支持存放多少数据
	 * 
	 * @param rootDirectory
	 * @param maxCacheSizeInBytes
	 */
	public DiskBasedCache(File rootDirectory, long maxCacheSizeInBytes) {
		this.rootDirectory = rootDirectory;
		this.maxCacheSizeInBytes = maxCacheSizeInBytes;
	}

	/**
	 * 缓存配置
	 * 
	 * @param config
	 * @return
	 */
	@Override
	public void config(Config config) {
		this.maxCacheSizeInBytes = config.getDiskUsageBytes();
	}

	/**
	 * 初始化缓存
	 */
	@Override
	public synchronized void initialize() {
		if (!this.rootDirectory.exists()) {
			if (!this.rootDirectory.mkdirs()) {
				GLog.d(TAG, "Unable to create cache dir " 
							+ this.rootDirectory .getAbsolutePath());
			}
			return;
		}

		File[] files = rootDirectory.listFiles();
		if (files == null) {
			return;
		}
		for (File file : files) {
			if(!file.getName().startsWith(Config.CACHE_FILE_PREFIX)) {
				continue ;
			}
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				CacheHeader header = CacheHeader.readHeader(fis);
				header.size = file.length();
				put(header.key, header);
			} catch (IOException e) {
				GLog.e(TAG, "Failed to read header for" + file.getAbsolutePath(),  e);
				if (file != null) {
					file.delete();
				}
			} finally {
				try {
					if (fis != null) {
						fis.close();
					}
				} catch (IOException ignored) {
				}
			}
		}
	}

	/**
	 * 使缓存失效
	 * 
	 * @param key
	 */
	@Override
	public synchronized void invalidate(String key) {
		CacheHeader header = entries.get(key);
		header.ttl = 0;
	}

	/**
	 * 把数据放入缓存中
	 * 
	 * @param key
	 * @param bytes
	 * @param ttl
	 */
	@Override
	public void put(String key, byte[] bytes, int ttl) {
		put(key, new Entry(bytes, ttl));
	}

	/**
	 * 把数据放入缓存中
	 * 
	 * @param key
	 * @param entry
	 */
	@Override
	public synchronized void put(String key, Entry entry) {
		pruneIfNeeded(entry.size());
		File file = CacheUtils.getFileForKey(rootDirectory, key);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			CacheHeader e = new CacheHeader(key, entry);
			boolean success = e.writeHeader(fos);
			if (!success) {
				fos.close();
				throw new IOException();
			}
			fos.write(entry.getData());
			fos.close();
			put(key, e);
			return;
		} catch (IOException e) {
			GLog.d(TAG, "Failed to write header for "
					+ file.getAbsolutePath(), e);
		}
		boolean deleted = file.delete();
		if (!deleted) {
			GLog.d(TAG, "Could not clean up file "
								+ file.getAbsolutePath());
		}
	}

	/**
	 * 把数据头记住
	 * 
	 * @param key
	 * @param header
	 */
	public synchronized void put(String key, CacheHeader header) {
		if (!entries.containsKey(key)) {
			totalSize += header.size;
		} else {
			CacheHeader oldEntry = entries.get(key);
			totalSize += (header.size - oldEntry.size);
		}
		entries.put(key, header);
	}

	/**
	 * 获取Bytes缓存数据
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public byte[] getBytes(String key) {
		Entry entry = get(key);
		if(entry != null) {
			return entry.getData();
		}
		return null;
	}
	
	/**
	 * 获取缓存数据
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public synchronized Entry get(String key) {
		CacheHeader entry = entries.get(key);
		// if the entry does not exist, return.
		if (entry == null) {
			return null;
		}
		if (entry.isExpired()) {
			remove(key);
			return null;
		}

		File file = CacheUtils.getFileForKey(rootDirectory, key);
		CountingInputStream cis = null;
		try {
			cis = new CountingInputStream(new FileInputStream(file));
			CacheHeader.readHeader(cis); // eat header
			byte[] data = StreamUtils.streamToBytes(cis, (int) (file.length() - cis.bytesRead));
			return entry.toCacheEntry(data);
		} catch (IOException e) {
			GLog.d(TAG, "Could not read cache data for " + file.getAbsolutePath(), e);
			remove(key);
			return null;
		} finally {
			if (cis != null) {
				try {
					cis.close();
				} catch (IOException ioe) {
					return null;
				}
			}
		}
	}

	/**
	 * 是否存在该缓存
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public boolean contains(String key) {
		return entries.containsKey(key);
	}

	/**
	 * 删除缓存数据
	 * 
	 * @param key
	 */
	@Override
	public synchronized void remove(String key) {
		boolean deleted = CacheUtils.getFileForKey(rootDirectory, key).delete();
		CacheHeader entry = entries.get(key);
		if (entry != null) {
			totalSize -= entry.size;
			entries.remove(key);
		}
		if (!deleted) {
			GLog.d(TAG,
					String.format("Could not delete cache entry for key=%s, filename=%s",
							key,
							CacheUtils.getHashForKey(key)));
		}
	}

	/**
	 * 清除所有缓存
	 */
	@Override
	public synchronized void clear() {
		File[] files = rootDirectory.listFiles();
		if (files != null) {
			for (File file : files) {
				if(!file.getName().startsWith(Config.CACHE_FILE_PREFIX)) {
					continue ;
				}
				file.delete();
			}
		}
		entries.clear();
		totalSize = 0;
	}

	/**
	 * 是否够用空间，如果不够用，清除不常用的
	 * 
	 * @param neededSpace
	 */
	private void pruneIfNeeded(int neededSpace) {
		if ((totalSize + neededSpace) < maxCacheSizeInBytes) {
			return;
		}

		long before = totalSize;
		int prunedFiles = 0;
		long startTime = System.currentTimeMillis();

		Iterator<Map.Entry<String, CacheHeader>> iterator = entries
				.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, CacheHeader> header = iterator.next();
			CacheHeader e = header.getValue();
			boolean deleted = CacheUtils.getFileForKey(rootDirectory, e.key).delete();
			if (deleted) {
				totalSize -= e.size;
			} else {
				GLog.d(TAG,
						String.format("Could not delete cache entry for key=%s, filename=", 
						e.key, CacheUtils.getHashForKey(e.key)));
			}
			iterator.remove();
			prunedFiles++;

			if ((totalSize + neededSpace)  < maxCacheSizeInBytes * Config.HYSTERESIS_FACTOR) {
				break;
			}
		}

		GLog.v(TAG, String.format("pruned %d files, %d bytes, %d ms",
				prunedFiles, (totalSize - before),
				System.currentTimeMillis() - startTime));
	}

}
