package io.ganguo.app.gcache.util;

import java.util.Date;
import java.util.Deque;
import java.util.LinkedList;

/**
 *
 * 一个工具类，可以用来跟踪程序效率问题
 * 目前暂时不支持多线程
 *
 */
public class GBenchmark {
	private static final String TAG = GBenchmark.class.getName();
	private static Deque<BenchEntry> benchStack = new LinkedList<BenchEntry>();
	
	public static void start(String tag){
		BenchEntry entry = new BenchEntry();
		entry.tag = tag;
		entry.depth = benchStack.size();
		entry.start = new Date();
		benchStack.addFirst(entry);
	}
	
	public static void end(String tag){
		BenchEntry entry = benchStack.pollFirst();
		if(!entry.tag.equals(tag)){
			GLog.w(TAG, "Benchmark Not Match, tag spell mistake or forgot Benchmark.end(tag) invoke at somewhere ??");
			return;
		}
		
		entry.end = new Date();
		long used = entry.end.getTime() - entry.start.getTime();
		GLog.d(TAG, "Benchmark [" + entry.tag +" ] - Used: " + (used)  + " ms. ");
	}
	
	private static class BenchEntry{
		public long depth;
		public String tag;
		public Date start;
		public Date end;
	}
}
