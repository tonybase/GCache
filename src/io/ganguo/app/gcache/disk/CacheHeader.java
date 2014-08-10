package io.ganguo.app.gcache.disk;

import io.ganguo.app.gcache.Cache.Entry;
import io.ganguo.app.gcache.Config;
import io.ganguo.app.gcache.util.GLog;
import io.ganguo.app.gcache.util.StreamUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 缓存头部信息
 * 
 * Created by zhihui_chen on 14-8-8.
 */
public class CacheHeader {
	private static final String TAG = CacheHeader.class.getName();

	/**
	 * 生存周期 毫秒
	 */
	public long ttl;

	/**
	 * 缓存数据文件长度
	 */
	public long size;

	/**
	 * 缓存KEY
	 */
	public String key;

	private CacheHeader() {
	}

	/**
	 * 包装数据
	 * 
	 * @param entry
	 */
	public CacheHeader(String key, Entry entry) {
		this.ttl = entry.getTtl();
		this.size = entry.size();
		this.key = key;
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
	 * Reads the header off of an InputStream and returns a CacheHeader
	 * object.
	 * 
	 * @param is
	 *                The InputStream to read from.
	 * @throws java.io.IOException
	 */
	public static CacheHeader readHeader(InputStream is) throws IOException {
		CacheHeader entry = new CacheHeader();
		int magic = StreamUtils.readInt(is);
		if (magic != Config.CACHE_MAGIC) {
			// don't bother deleting, it'll get pruned eventually
			throw new IOException();
		}
		entry.key = StreamUtils.readString(is);
		entry.size = StreamUtils.readLong(is);
		entry.ttl = StreamUtils.readLong(is);
		return entry;
	}

	/**
	 * Creates a cache entry for the specified data.
	 */
	public Entry toCacheEntry(byte[] data) {
		Entry e = new Entry();
		e.setData(data);
		e.setTtl(ttl);
		return e;
	}

	/**
	 * Writes the contents of this CacheHeader to the specified
	 * OutputStream.
	 */
	public boolean writeHeader(OutputStream os) {
		try {
			StreamUtils.writeInt(os, Config.CACHE_MAGIC);
			StreamUtils.writeString(os, key);
			StreamUtils.writeLong(os, size);
			StreamUtils.writeLong(os, ttl);
			os.flush();
			return true;
		} catch (IOException e) {
			GLog.w(TAG, "write Header error!", e);
			return false;
		}
	}

}
