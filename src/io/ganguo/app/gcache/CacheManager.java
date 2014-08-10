package io.ganguo.app.gcache;

import io.ganguo.app.gcache.disk.DiskBasedCache;
import io.ganguo.app.gcache.disk.DiskWithMemoryCache;
import io.ganguo.app.gcache.memory.MemoryCache;

import java.io.File;
import java.util.Hashtable;
import java.util.Map;

public class CacheManager {
	private static Map<String, CacheManager> gcacheMap = new Hashtable<String, CacheManager>();
	private GCache cacheDecorator = new GCache();

	/**
	 * 获取缓存实例
	 * 
	 * @return
	 */
	public GCache getCache() {
		return cacheDecorator;
	}

	/**
	 * 获取内存缓存管理实例
	 * 
	 * @return 管理实例
	 */
	public static CacheManager getForMemeory() {
		return getForMemeory(Config.DEFAULT_MEMORY_USAGE_BYTES);
	}

	/**
	 * 获取内存缓存管理实例 
	 * 可以设置允许最大容量
	 * 
	 * @param maxCacheSizeInBytes
	 * @return 管理实例
	 */
	public static CacheManager getForMemeory(int maxCacheSizeInBytes) {
		return getForMemeory("def_memory_cache", maxCacheSizeInBytes);
	}

	/**
	 * 获取内存缓存管理实例 
	 * 可以设置允许最大容量
	 * 
	 * @param key
	 * @param maxCacheSizeInBytes
	 * @return 管理实例
	 */
	public static CacheManager getForMemeory(String key, int maxCacheSizeInBytes) {
		CacheManager gcacheManager = gcacheMap.get(key);
		if (gcacheManager == null) {
			gcacheManager = new CacheManager();
			gcacheManager.cacheDecorator.cache = new MemoryCache(maxCacheSizeInBytes);
			gcacheManager.cacheDecorator.cache.initialize();
			gcacheMap.put(key, gcacheManager);
		}
		return gcacheManager;
	}

	/**
	 * 获取内存缓存管理实例
	 * 
	 * @param key
	 * @param rootDirectory
	 * @param maxCacheSizeInBytes
	 * @return 管理实例
	 */
	public static CacheManager getForDisk(File rootDirectory) {
		return getForDisk("def_memory_cache", rootDirectory, Config.DEFAULT_DISK_USAGE_BYTES);
	}
	
	/**
	 * 获取内存缓存管理实例
	 * 
	 * @param key
	 * @param rootDirectory
	 * @param maxCacheSizeInBytes
	 * @return 管理实例
	 */
	public static CacheManager getForDisk(File rootDirectory, int maxCacheSizeInBytes) {
		return getForDisk("def_disk_cache", rootDirectory, maxCacheSizeInBytes);
	}
	
	/**
	 * 获取内存缓存管理实例 可以设置允许最大容量
	 * 
	 * @param key
	 * @param rootDirectory
	 * @param maxCacheSizeInBytes
	 * @return 管理实例
	 */
	public static CacheManager getForDisk(String key, File rootDirectory, int maxCacheSizeInBytes) {
		CacheManager gcacheManager = gcacheMap.get(key);
		if (gcacheManager == null) {
			gcacheManager = new CacheManager();
			gcacheManager.cacheDecorator.cache = new DiskBasedCache(rootDirectory, maxCacheSizeInBytes);
			gcacheManager.cacheDecorator.cache.initialize();
			gcacheMap.put(key, gcacheManager);
		}
		return gcacheManager;
	}
	
	/**
	 * 获取内存缓存管理实例 
	 * 可以设置允许最大容量
	 * 
	 * @param key
	 * @param rootDirectory
	 * @param maxCacheSizeInBytes
	 * @return 管理实例
	 */
	public static CacheManager getForDiskWithMemory(File rootDirectory) {
		return getForDiskWithMemory("def_disk_mem_cache", rootDirectory, 
				Config.DEFAULT_MEMORY_USAGE_BYTES, Config.DEFAULT_DISK_USAGE_BYTES);
	}
	
	/**
	 * 获取内存缓存管理实例 
	 * 可以设置允许最大容量
	 * 
	 * @param key
	 * @param rootDirectory
	 * @param maxCacheSizeInBytes
	 * @return 管理实例
	 */
	public static CacheManager getForDiskWithMemory(String key, File rootDirectory) {
		return getForDiskWithMemory(key, rootDirectory, 
				Config.DEFAULT_MEMORY_USAGE_BYTES, Config.DEFAULT_DISK_USAGE_BYTES);
	}
	
	/**
	 * 获取内存缓存管理实例 
	 * 可以设置允许最大容量
	 * 
	 * @param key
	 * @param rootDirectory
	 * @param maxMemCacheSizeInBytes
	 * @param maxDiskCacheSizeInBytes
	 * @return 管理实例
	 */
	public static CacheManager getForDiskWithMemory(String key, File rootDirectory
			, int maxMemCacheSizeInBytes, int maxDiskCacheSizeInBytes) {
		CacheManager gcacheManager = gcacheMap.get(key);
		if (gcacheManager == null) {
			gcacheManager = new CacheManager();
			gcacheManager.cacheDecorator.cache = 
					new DiskWithMemoryCache(rootDirectory, maxMemCacheSizeInBytes, maxDiskCacheSizeInBytes);
			gcacheManager.cacheDecorator.cache.initialize();
			gcacheMap.put(key, gcacheManager);
		}
		return gcacheManager;
	}
	
	/**
	 * Cache 装饰模式（Decorator）
	 * 
	 * @author zhihui_chen
	 * 
	 */
	public class GCache {
		public Cache cache = null;

		public void config(Config config) {
			if(config != null) cache.config(config);
		}

		public void invalidate(String key) {
			if(key != null) cache.invalidate(key);
		}

		public void put(String key, byte[] bytes, int ttl) {
			if(key != null && bytes != null) {
				cache.put(key, bytes, ttl);
			}
		}
		
		public void put(String key, String value, int ttl) {
			if(key != null || value != null) {
				put(key, value.getBytes(), ttl);
			}
		}
		
		public byte[] getBytes(String key) {
			if(key != null)
				return cache.getBytes(key);
			return null;
		}
		
		public String getString(String key) {
			if(key != null) {
				byte[] bytes = getBytes(key);
				if(bytes != null) 
					return new String(bytes);
			}
			return null;
		}

		public boolean contains(String key) {
			if(key != null)
				return cache.contains(key);
			return false;
		}

		public void remove(String key) {
			if(key != null) cache.remove(key);
		}

		public void clear() {
			cache.clear();
		}
	}
}
