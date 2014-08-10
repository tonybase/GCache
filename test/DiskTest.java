import io.ganguo.app.gcache.CacheManager;
import io.ganguo.app.gcache.CacheManager.GCache;
import io.ganguo.app.gcache.util.GBenchmark;

import java.io.File;

public class DiskTest {
	private static final String TAG = DiskTest.class.getName();

	public static void main(String[] args) {
		final GCache cache = CacheManager.getForDisk(new File("diskCache")).getCache();
		
		testPut(cache, 1000);
		testGet(cache, 1000);
		cache.clear();
	}

	public static void testPut(GCache cache, int count) {
		GBenchmark.start("testPut" + count);
		for (int i = 0; i < count; i++) {
			cache.put(i + "", i + TAG, 450);
		}
		GBenchmark.end("testPut" + count);
	}

	public static void testGet(GCache cache, int count) {
		GBenchmark.start("testGet" + count);
		for (int i = 0; i < count; i++) {
			cache.getString(i + "");
		}
		GBenchmark.end("testGet" + count);
	}

}
