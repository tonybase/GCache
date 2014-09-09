import io.ganguo.app.gcache.CacheBuilder;
import io.ganguo.app.gcache.Config;
import io.ganguo.app.gcache.interfaces.GCache;
import io.ganguo.app.gcache.transcoder.StringTranscoder;
import io.ganguo.app.gcache.util.GBenchmark;

import java.io.File;

public class TestDiskWithMemory {
	private static final String TAG = TestDiskWithMemory.class.getName();

	public static void main(String[] args) {
		GCache cache = CacheBuilder.newBuilder()
				.withTranscoder(new StringTranscoder())
				.withCacheRootDirectory(new File("diskCache"))
				.maxDiskUsageBytes(10 * Config.BYTES_MB)
				.maxMemoryUsageBytes(5 * Config.BYTES_MB)
				.defaultCacheTime(30000)
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
