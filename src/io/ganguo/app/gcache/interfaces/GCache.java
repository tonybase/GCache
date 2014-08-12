package io.ganguo.app.gcache.interfaces;

import io.ganguo.app.gcache.Config;
import io.ganguo.app.gcache.util.GLog;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@SuppressWarnings("unchecked")
public abstract class GCache implements Cache {
	private static final String TAG = GCache.class.getName();

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
	 * 并发读写锁
	 */
	private ReadWriteLock lock = new ReentrantReadWriteLock();

	/**
	 * 缓存类型解码器
	 */
	protected Transcoder transcoder = null;

	public GCache(Transcoder transcoder) {
		this.transcoder = transcoder;
	}

	/**
	 * 初始化配置
	 * 
	 */
	@Override
	public void config(Config config) {
		this.minCacheTime = config.getMinCacheTime();
		this.maxCacheTime = config.getMaxCacheTime();
		this.defaultCacheTime = config.getDefaultCacheTime();

		GLog.d(TAG, "minCacheTime=" + minCacheTime + " maxCacheTime="
				+ maxCacheTime + " defaultCacheTime="
				+ defaultCacheTime);
	}

	/**
	 * 读取缓存
	 * 
	 * @param key
	 * @return
	 */
	public <K, V> V get(K key) {
		if (key == null)
			return null;
		if (!contains(keyToString(key)))
			return null;
		lock.readLock().lock();
		try {
			return (V) transcoder.decode(getEntry(keyToString(key)));
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * 写入缓存
	 * 
	 * @param key
	 * @param value
	 */
	public <K, V> void put(K key, V value) {
		if (key == null || value == null)
			return;
		lock.writeLock().lock();
		try {
			putEntry(transcoder.decodeKey(key),
						transcoder.encode(value,
							defaultCacheTime));
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * 写入缓存
	 * 
	 * @param key
	 * @param value
	 * @param ttl
	 */
	public <K, V> void put(K key, V value, int ttl) {
		if (key == null || value == null)
			return;
		// 最小缓存时间控制
		if (minCacheTime > 0 && ttl < minCacheTime) {
			ttl = minCacheTime;
		}
		// 最大缓存时间控制
		if (maxCacheTime > 0 && ttl > maxCacheTime) {
			ttl = maxCacheTime;
		}
		lock.writeLock().lock();
		try {
			putEntry(keyToString(key), transcoder.encode(value, ttl));
		} finally {
			lock.writeLock().unlock();
		}
	}

	public <K> String keyToString(K key) {
		return transcoder.decodeKey(key);
	}
}
