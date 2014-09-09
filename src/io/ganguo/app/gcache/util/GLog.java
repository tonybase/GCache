package io.ganguo.app.gcache.util;

/**
 * 日志输出
 * 
 * @author zhihui_chen
 *
 */
public class GLog {
	
	public static boolean isDebug = true;

	public static void d(String tag, String msg) {
		d(tag, msg, null);
	}

	public static void v(String tag, String msg) {
		v(tag, msg, null);
	}

	public static void i(String tag, String msg) {
		i(tag, msg, null);
	}

	public static void w(String tag, String msg) {
		w(tag, msg, null);
	}

	public static void e(String tag, String msg) {
		e(tag, msg, null);
	}

	public static void d(String tag, String msg, Throwable e) {
		println("debug: " + tag, msg, e, false);
	}

	public static void v(String tag, String msg, Throwable e) {
		println("verbose: " + tag, msg, e, false);
	}

	public static void i(String tag, String msg, Throwable e) {
		println("info: " + tag, msg, e, false);
	}

	public static void w(String tag, String msg, Throwable e) {
		println("warn: " + tag, msg, e, true);
	}

	public static void e(String tag, String msg, Throwable e) {
		println("error: " + tag, msg, e, true);
	}

	private static void println(String tag, String msg, Throwable e, boolean isError) {
		if(!isDebug) return ;
		
		if (isError) {
			System.err.println("[" + tag + "] " + msg);
		} else {
			System.out.println("[" + tag + "] " + msg);
		}
		if (e != null) {
			e.printStackTrace();
		}
	}

}
