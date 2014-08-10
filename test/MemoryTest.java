import io.ganguo.app.gcache.CacheManager;
import io.ganguo.app.gcache.CacheManager.GCache;
import io.ganguo.app.gcache.util.GBenchmark;

public class MemoryTest {
	private static final String TAG = MemoryTest.class.getName();

	public static void main(String[] args) {
		final GCache cache = CacheManager.getForMemeory().getCache();
		
		testPut(cache, 100000);
		testGet(cache, 100000);
		cache.clear();
		
		/*
		new Thread(new Runnable() {

			@Override
			public void run() {
				testPut(cache, 100000);
			}
		}).start();
		new Thread(new Runnable() {

			@Override
			public void run() {
				testPut(cache, 100000);
			}
		}).start();
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				testGet(cache, 100000);
			}
		}).start();
		new Thread(new Runnable() {

			@Override
			public void run() {
				testGet(cache, 10000);
			}
		}).start();
		*/
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
