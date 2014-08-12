package io.ganguo.app.gcache.interfaces;

import io.ganguo.app.gcache.Config;

/**
 * 缓存接口
 * <p/>
 * Created by zhihui_chen on 14-8-5.
 */
public interface Cache  {

	/**
	 * 缓存配置
	 * 
	 * @param config
	 * @return
	 */
	public void config(Config config);

	/**
	 * 初始化缓存
	 */
	public void initialize();

	/**
	 * 使缓存失效
	 * 
	 * @param key
	 */
	public <T> void invalidate(T key);

	/**
	 * 把数据放入缓存中
	 * 
	 * @param key
	 * @param entry
	 */
	public <T> void putEntry(T key, Entry entry);

	/**
	 * 获取缓存数据
	 * 
	 * @param key
	 * @return
	 */
	public <T> Entry getEntry(T key);

	/**
	 * 是否存在该缓存
	 * 
	 * @param key
	 * @return
	 */
	public <T> boolean contains(T key);

	/**
	 * 删除缓存数据
	 * 
	 * @param key
	 */
	public <T> void remove(T key);

	/**
	 * 清除所有缓存
	 */
	public void clear();

	/**
	 * Cache数据信息
	 */
	public static class Entry {
		/**
		 * 缓存数据
		 */
		private byte[] data;

		/**
		 * 生存周期 毫秒
		 */
		private long ttl;

		public Entry() {
		}

		/**
		 * 缓存数据、生存周期
		 * 
		 * @param data
		 * @param ttl
		 */
		public Entry(byte[] data, int ttl) {
			this.data = data;

			setTtl(ttl);
		}

		/**
		 * 获取缓存数据
		 * 
		 * @return
		 */
		public byte[] getData() {
			return data;
		}

		/**
		 * 设置缓存数据
		 * 
		 * @param data
		 */
		public void setData(byte[] data) {
			this.data = data;
		}

		/**
		 * 获取生存时间
		 * 
		 * @return
		 */
		public long getTtl() {
			return ttl;
		}

		/**
		 * 累加当前当前时间
		 * 
		 * @param ttl
		 */
		public void setTtl(long ttl) {
			this.ttl = ttl + System.currentTimeMillis();
		}

		/**
		 * 缓存是否已经过期
		 * 
		 * @return
		 */
		public boolean isExpired() {
			return this.ttl < System.currentTimeMillis();
		}

		/**
		 * 缓存数据大小
		 * 
		 * @return
		 */
		public int size() {
			return data.length;
		}

	}

}
