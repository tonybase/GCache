package io.ganguo.app.gcache;

/**
 * 缓存时间
 * 
 * Created by zhihui_chen on 14-8-8.
 */
public class CacheTime {

	public static final int NONE = 0;
	public static final int HALF_MINUTE = 30000;
	public static final int ONE_MINUTE = 1 * 6000;
	public static final int FIVE_MINUTE = 5 * ONE_MINUTE;
	public static final int TEN_MINUTE = 10 * ONE_MINUTE;
	public static final int FIFTEEN_MINUTE = 15 * ONE_MINUTE;
	public static final int HALF_HOUR = 30 * ONE_MINUTE;
	public static final int HOUR = 60 * ONE_MINUTE;
	public static final int DAY = 24 * HOUR;
	public static final int WEEK = 7 * 24 * HOUR;

}
