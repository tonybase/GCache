package io.ganguo.app.gcache;

/**
 * 缓存时间
 *
 * Created by zhihui_chen on 14-8-8.
 */
public enum CacheTime {

    NONE(0),
    HALF_MINUTE(30),
    ONE_MINUTE(1 * 60),
    FIVE_MINUTE(5 * 60),
    TEN_MINUTE(10 * 60),
    FIFTEEN_MINUTE(15 * 60),
    HALF_HOUR(30 * 60),
    HOUR(60 * 60),
    DAY(24 * 60 * 60),
    WEEK(7 * 24 * 60 * 60);

    private int value;

    CacheTime(int value) {
	    this.value = value;
    }

    public int getValue() {
	    return value * 1000;
    }

}
