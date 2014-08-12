package io.ganguo.app.gcache.disk;

import io.ganguo.app.gcache.Config;
import io.ganguo.app.gcache.interfaces.Transcoder;
import io.ganguo.app.gcache.memory.MemoryCache;

import java.io.File;

/**
 * Created by zhihui_chen on 14-8-7.
 */
public class DiskWithMemoryCache extends DiskBasedCache {

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
		memoryCache = new MemoryCache();
	}

	/**
	 * put/get 解码器
	 * 
	 * @param transcoder
	 */
	public DiskWithMemoryCache(Transcoder transcoder) {
		super(transcoder);
		memoryCache = new MemoryCache(transcoder);
	}

	/**
	 * 缓存配置
	 * 
	 * @param config
	 * @return
	 */
	@Override
	public void config(Config config) {
		memoryCache.config(config);
		super.config(config);
	}

	/**
	 * 初始化缓存
	 */
	@Override
	public void initialize() {
		memoryCache.initialize();
		super.initialize();
	}

	/**
	 * 使缓存失效
	 * 
	 * @param key
	 */
	@Override
	public synchronized <K> void invalidate(K key) {
		memoryCache.invalidate(key);
		super.invalidate(key);
	}

	/**
	 * 把数据放入缓存中
	 * 
	 * @param key
	 * @param entry
	 */
	@Override
	public <K> void putEntry(K key, Entry entry) {
		memoryCache.putEntry(key, entry);
		super.putEntry(key, entry);
	}

	/**
	 * 获取缓存数据
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public <K> Entry getEntry(K key) {
		Entry entry = memoryCache.getEntry(key);
		if (entry == null) {
			entry = super.getEntry(key);
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
	public <K> boolean contains(K key) {
		boolean isCon = memoryCache.contains(key);
		if (isCon) {
			return isCon;
		}
		return super.contains(key);
	}

	/**
	 * 删除缓存数据
	 * 
	 * @param key
	 */
	@Override
	public synchronized <K> void remove(K key) {
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
