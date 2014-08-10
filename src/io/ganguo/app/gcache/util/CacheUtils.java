package io.ganguo.app.gcache.util;

import java.io.File;

/**
 * Created by zhihui_chen on 14-8-8.
 */
public class CacheUtils {

	/**
	 * 得到KEY的唯一码
	 * 
	 * @param key
	 * @return
	 */
	public static String getHashForKey(String key) {
		int firstHalfLength = key.length() / 2;
		String hashKey = String.valueOf(key.substring(0, firstHalfLength).hashCode());
		hashKey += String.valueOf(key.substring(firstHalfLength).hashCode());
		return hashKey;
	}

	public static File getFileForKey(File rootDirectory, String key) {
		return new File(rootDirectory, getHashForKey(key));
	}

}
