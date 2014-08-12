package io.ganguo.app.gcache;

import io.ganguo.app.gcache.disk.DiskBasedCache;
import io.ganguo.app.gcache.disk.DiskWithMemoryCache;
import io.ganguo.app.gcache.interfaces.GCache;
import io.ganguo.app.gcache.interfaces.Transcoder;
import io.ganguo.app.gcache.memory.MemoryCache;
import io.ganguo.app.gcache.transcoder.StringTranscoder;

import java.io.File;

public class Builders {
	public enum Type {
		DISK, MEMORY, DISK_WITH_MEMORY
	}

	public static Builder newBuilder() {
		return new Builder(Type.DISK_WITH_MEMORY);
	}

	public static Builder newBuilderForMemory() {
		return new Builder(Type.MEMORY);
	}

	public static Builder newBuilderForDisk() {
		return new Builder(Type.DISK);
	}

	public static class Builder {
		private Type type = Type.DISK_WITH_MEMORY;
		private Config config = new Config();
		private GCache gcache = null;

		public Builder(Type type) {
			this.type = type;
		}

		public Builder withTranscoder(Transcoder transcoder) {
			switch (type) {
			case DISK_WITH_MEMORY:
				this.gcache = new DiskWithMemoryCache(transcoder);
				break;
			case MEMORY:
				this.gcache = new MemoryCache(transcoder);
				break;
			case DISK:
				this.gcache = new DiskBasedCache(transcoder);
				break;
			}

			return this;
		}

		public Builder maxMemoryUsageBytes(long memoryUsageBytes) {
			config.setMemoryUsageBytes(memoryUsageBytes);
			return this;
		}

		public Builder maxDiskUsageBytes(long diskUsageBytes) {
			config.setDiskUsageBytes(diskUsageBytes);
			return this;
		}

		public Builder minCacheTime(int minCacheTime) {
			config.setMinCacheTime(minCacheTime);
			return this;
		}

		public Builder maxCacheTime(int maxCacheTime) {
			config.setMaxCacheTime(maxCacheTime);
			return this;
		}

		public Builder defaultCacheTime(int defaultCacheTime) {
			config.setDefaultCacheTime(defaultCacheTime);
			return this;
		}

		public Builder withCacheRootDirectory(File cacheRootDirectory) {
			config.setCacheRootDirectory(cacheRootDirectory);
			return this;
		}

		public GCache build() {
			gcache.config(config);
			return this.gcache;
		}
	}

	public static void main(String[] args) {
		GCache gc = Builders.newBuilder()
				.withTranscoder(new StringTranscoder())
				.maxMemoryUsageBytes(20000)
				.maxDiskUsageBytes(40000).maxCacheTime(3000)
				.minCacheTime(1000).defaultCacheTime(1000)
				.build();
		GCache gc2 = Builders.newBuilderForMemory()
				.withTranscoder(new StringTranscoder())
				.maxMemoryUsageBytes(20000).maxCacheTime(3000)
				.minCacheTime(1000).defaultCacheTime(1000)
				.build();
		GCache gc3 = Builders.newBuilderForDisk()
				.withTranscoder(new StringTranscoder())
				.maxDiskUsageBytes(40000).maxCacheTime(3000)
				.minCacheTime(1000).defaultCacheTime(1000)
				.build();
	}
}
