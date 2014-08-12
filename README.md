GCache
======

一个轻量级，高性能的缓存构架，以android缓存而设计为初衷，也可以应用于一般的Java项目中。

======

DiskWithMemoryCache:

	GCache cache = Builders.newBuilder()
				.withTranscoder(new StringTranscoder())
				.withCacheRootDirectory(new File("diskCache"))
				.maxDiskUsageBytes(10000)
				.maxMemoryUsageBytes(1000)
				.defaultCacheTime(50)
				.build();
				
MemoryCache:

	GCache cache = Builders.newBuilderForMemory()
				.withTranscoder(new StringTranscoder())
				.maxMemoryUsageBytes(10000)
				.defaultCacheTime(20)
				.build();
				
DiskBasedCache:

	GCache cache = Builders.newBuilderForDisk()
				.withTranscoder(new StringTranscoder())
				.withCacheRootDirectory(new File("diskCache"))
				.maxDiskUsageBytes(1000)
				.defaultCacheTime(50)
				.build();			
				
	cache.put(K, V);
	cache.get(K);
	cache.get(K, TTL);
