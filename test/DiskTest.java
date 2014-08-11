import io.ganguo.app.gcache.CacheManager;
import io.ganguo.app.gcache.CacheManager.GCache;
import io.ganguo.app.gcache.util.GBenchmark;
import io.ganguo.app.gcache.util.GLog;

import java.io.File;

public class DiskTest {
	private static final String TAG = DiskTest.class.getName();

	public static void main(String[] args) {
		final GCache cache = CacheManager.getForDisk(new File("diskCache")).getCache();
		
		testPut(cache, 10);
		testGet(cache, 10);
		cache.clear();
	}

	public static void testPut(GCache cache, int count) {
		GBenchmark.start("testPut" + count);
		for (int i = 0; i < count; i++) {
			cache.put(i + "", i + TAG, 450);
			GLog.d("put key:", i + " value:" + i +TAG);
		}
		GBenchmark.end("testPut" + count);
	}

	public static void testGet(GCache cache, int count) {
		GBenchmark.start("testGet" + count);
		for (int i = 0; i < count; i++) {
			String str = cache.getString(i + "");
			GLog.d("get", str);
		}
		GBenchmark.end("testGet" + count);
	}

}
