package io.ganguo.app.gcache;

import java.io.File;

/**
 * Created by zhihui_chen on 14-8-7.
 */
public class Config {
	/**
	 * 计算单位 KB
	 */
	public static final int BYTES_KB = 1024;

	/**
	 * 计算单位 MB
	 */
	public static final int BYTES_MB = 1024 * 1024;

	/**
	 * Default maximum disk usage in bytes.
	 */
	public static final int DEFAULT_DISK_USAGE_BYTES = 10 * BYTES_MB;

	/**
	 * Default maximum disk memory in bytes.
	 */
	public static final int DEFAULT_MEMORY_USAGE_BYTES = 5 * BYTES_MB;

	/**
	 * High water mark percentage for the cache
	 */
	public static final float HYSTERESIS_FACTOR = 0.9f;

	public static final String CACHE_FILE_PREFIX = "cache_";

	/**
	 * 磁盘最大占用空间
	 */
	private long diskUsageBytes = DEFAULT_DISK_USAGE_BYTES;

	/**
	 * 内存最大占用空间
	 */
	private long memoryUsageBytes = DEFAULT_MEMORY_USAGE_BYTES;

	/**
	 * 默认放入缓存时间
	 */
	private int defaultCacheTime = 0;
	
	/**
	 * 放入缓存最小过期时间
	 */
	private int minCacheTime = 0;
	
	/**
	 * 放入缓存最大过期时间
	 */
	private int maxCacheTime = 0;
	
	/**
	 * 文件系统缓存根目录
	 */
	private File cacheRootDirectory;

	public long getDiskUsageBytes() {
		return diskUsageBytes;
	}

	public void setDiskUsageBytes(long diskUsageBytes) {
		this.diskUsageBytes = diskUsageBytes;
	}

	public long getMemoryUsageBytes() {
		return memoryUsageBytes;
	}

	public void setMemoryUsageBytes(long memoryUsageBytes) {
		this.memoryUsageBytes = memoryUsageBytes;
	}

	public int getDefaultCacheTime() {
		return defaultCacheTime;
	}

	public void setDefaultCacheTime(int defaultCacheTime) {
		this.defaultCacheTime = defaultCacheTime;
	}

	public int getMinCacheTime() {
		return minCacheTime;
	}

	public void setMinCacheTime(int minCacheTime) {
		this.minCacheTime = minCacheTime;
	}

	public int getMaxCacheTime() {
		return maxCacheTime;
	}

	public void setMaxCacheTime(int maxCacheTime) {
		this.maxCacheTime = maxCacheTime;
	}

	public File getCacheRootDirectory() {
		return cacheRootDirectory;
	}

	public void setCacheRootDirectory(File cacheRootDirectory) {
		this.cacheRootDirectory = cacheRootDirectory;
	}

}
