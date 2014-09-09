import io.ganguo.app.gcache.CacheBuilder;
import io.ganguo.app.gcache.Config;
import io.ganguo.app.gcache.interfaces.GCache;
import io.ganguo.app.gcache.transcoder.StringTranscoder;
import io.ganguo.app.gcache.util.GBenchmark;

public class TestMemory {
	private static final String TAG = TestMemory.class.getName();

	public static void main(String[] args) {
		GCache cache = CacheBuilder.newBuilderForMemory()
				.withTranscoder(new StringTranscoder())
				.maxMemoryUsageBytes(5 * Config.BYTES_MB)
				.defaultCacheTime(20)
				.build();
		
		testPut(cache, 100);
		testGet(cache, 100);
	}

	public static void testPut(GCache cache, int count) {
		GBenchmark.start("testPut" + count);
		for (int i = 0; i < count; i++) {
			cache.put(i + "", i + TAG);
		}
		GBenchmark.end("testPut" + count);
	}

	public static void testGet(GCache cache, int count) {
		GBenchmark.start("testGet" + count);
		for (int i = 0; i < count; i++) {
			System.out.println(cache.get(i + ""));
		}
		GBenchmark.end("testGet" + count);
	}
}
