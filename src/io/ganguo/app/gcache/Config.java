package io.ganguo.app.gcache;

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

	/**
	 * Magic number for current version of cache file format.
	 */
	public static final int CACHE_MAGIC = 0x20140808;

	/**
	 * 磁盘最大占用空间
	 */
	private int diskUsageBytes = DEFAULT_DISK_USAGE_BYTES;

	/**
	 * 内存最大占用空间
	 */
	private int memoryUsageBytes = DEFAULT_MEMORY_USAGE_BYTES;

	public int getDiskUsageBytes() {
		return diskUsageBytes;
	}

	public void setDiskUsageBytes(int diskUsageBytes) {
		this.diskUsageBytes = diskUsageBytes;
	}

	public int getMemoryUsageBytes() {
		return memoryUsageBytes;
	}

	public void setMemoryUsageBytes(int memoryUsageBytes) {
		this.memoryUsageBytes = memoryUsageBytes;
	}
}
