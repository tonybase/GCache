package io.ganguo.app.gcache.memory;

import io.ganguo.app.gcache.Cache;
import io.ganguo.app.gcache.Config;
import io.ganguo.app.gcache.util.GLog;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 内存缓存
 * <p/>
 * Created by zhihui_chen on 14-8-7.
 */
public class MemoryCache implements Cache {
	private static final String TAG = MemoryCache.class.getName();
	
	/**
	 * K,V 缓存 使用LRU特性 accessOrder true
	 */
	private final Map<String, Entry> entries = new LinkedHashMap<String, Entry>(16, .75f, true);

	/**
	 * 缓存占用总大小大小
	 */
	private long totalSize = 0;

	/**
	 * 允许最大的缓存大小
	 */
	private int maxCacheSizeInBytes = Config.DEFAULT_MEMORY_USAGE_BYTES;

	/**
	 * 使用默认配置缓存容量
	 * 
	 * @param maxCacheSizeInBytes
	 */
	public MemoryCache() {
		this.maxCacheSizeInBytes = Config.DEFAULT_MEMORY_USAGE_BYTES;
	}
	
	/**
	 * 配置缓存容量
	 * 
	 * @param maxCacheSizeInBytes
	 */
	public MemoryCache(int maxCacheSizeInBytes) {
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

	}

	/**
	 * 使缓存失效
	 * 
	 * @param key
	 */
	@Override
	public synchronized void invalidate(String key) {
		Entry entry = get(key);
		entry.setTtl(0);
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

		if (!entries.containsKey(key)) {
			totalSize += entry.size();
		} else {
			Entry oldEntry = entries.get(key);
			totalSize -= oldEntry.size();
		}
		entries.put(key, entry);
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
		Entry entry = entries.get(key);

		if (entry != null && entry.isExpired()) {
			remove(key);
			return null;
		}
		return entry;
	}

	/**
	 * 是否存在该缓存
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public boolean contains(String key) {
		Entry entry = entries.get(key);
		if (entry != null && !entry.isExpired()) {
			return true;
		}
		return false;
	}

	/**
	 * 删除缓存数据
	 * 
	 * @param key
	 */
	@Override
	public synchronized void remove(String key) {
		Entry entry = entries.get(key);
		if (entry != null) {
			totalSize -= entry.size();
			entries.remove(key);
		}
	}

	/**
	 * 清除所有缓存
	 */
	@Override
	public synchronized void clear() {
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

		// 使用LRU特性，删除最前面，最不常用的
		Iterator<Map.Entry<String, Entry>> iterator = entries.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Entry> entry = iterator.next();
			Entry e = entry.getValue();
			totalSize -= e.size();
			iterator.remove();
			prunedFiles++;

			if ((totalSize + neededSpace) < maxCacheSizeInBytes
					* Config.HYSTERESIS_FACTOR) {
				break;
			}
		}
		GLog.d(TAG, String.format("pruned %d files, %d bytes, %d ms",
				prunedFiles, (totalSize - before),
				System.currentTimeMillis() - startTime));

	}
}
