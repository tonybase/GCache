package io.ganguo.app.gcache.disk;

import io.ganguo.app.gcache.Config;
import io.ganguo.app.gcache.memory.MemoryCache;

import java.io.File;

/**
 * Created by zhihui_chen on 14-8-7.
 */
public class DiskWithMemoryCache extends DiskBasedCache {
	/**
	 * 允许最大的缓存大小
	 */
	private int maxMemCacheSizeInBytes = Config.DEFAULT_MEMORY_USAGE_BYTES;
	
	/**
	 * 内存缓存容器
	 */
	private MemoryCache memoryCache;

	/**
	 * 需要文件缓存的根目录 最多支持存放多少数据
	 * 
	 * @param rootDirectory
	 */
	public DiskWithMemoryCache(File rootDirectory) {
		super(rootDirectory);
	}

	/**
	 * 需要文件缓存的根目录 最多支持存放多少数据
	 * 
	 * @param rootDirectory
	 * @param maxDiskCacheSizeInBytes
	 */
	public DiskWithMemoryCache(File rootDirectory, long maxDiskCacheSizeInBytes) {
		super(rootDirectory, (int) maxDiskCacheSizeInBytes);
	}

	/**
	 * 需要文件缓存的根目录 最多支持存放多少数据
	 * 
	 * @param rootDirectory
	 * @param maxMemCacheSizeInBytes
	 */
	public DiskWithMemoryCache(File rootDirectory, int maxMemCacheSizeInBytes) {
		super(rootDirectory);
		this.maxMemCacheSizeInBytes = maxMemCacheSizeInBytes;
	}

	/**
	 * 需要文件缓存的根目录 最多支持存放多少数据
	 * 
	 * @param rootDirectory
	 * @param maxMemCacheSizeInBytes
	 * @param maxDiskCacheSizeInBytes
	 */
	public DiskWithMemoryCache(File rootDirectory,
			int maxMemCacheSizeInBytes, int maxDiskCacheSizeInBytes) {
		super(rootDirectory, maxDiskCacheSizeInBytes);
		
		this.maxMemCacheSizeInBytes = maxMemCacheSizeInBytes;
	}

	/**
	 * 缓存配置
	 * 
	 * @param config
	 * @return
	 */
	@Override
	public void config(Config config) {
		super.config(config);
		memoryCache.config(config);
	}

	/**
	 * 初始化缓存
	 */
	@Override
	public synchronized void initialize() {
		super.initialize();
		memoryCache = new MemoryCache(maxMemCacheSizeInBytes);
		memoryCache.initialize();
	}

	/**
	 * 使缓存失效
	 * 
	 * @param key
	 */
	@Override
	public synchronized void invalidate(String key) {
		memoryCache.invalidate(key);
		super.invalidate(key);
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
		super.put(key, entry);
		memoryCache.put(key, entry);
	}

	/**
	 * 获取Bytes缓存数据
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public synchronized byte[] getBytes(String key) {
		Entry entry = get(key);
		if (entry != null) {
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
		Entry entry = memoryCache.get(key);
		if (entry == null) {
			entry = super.get(key);
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
		return get(key) != null;
	}

	/**
	 * 删除缓存数据
	 * 
	 * @param key
	 */
	@Override
	public synchronized void remove(String key) {
		memoryCache.remove(key);
		super.remove(key);
	}

	/**
	 * 清除所有缓存
	 */
	@Override
	public synchronized void clear() {
		memoryCache.clear();
		super.clear();
	}
}
