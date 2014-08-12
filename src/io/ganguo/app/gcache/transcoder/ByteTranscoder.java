package io.ganguo.app.gcache.transcoder;

import io.ganguo.app.gcache.interfaces.Cache.Entry;
import io.ganguo.app.gcache.interfaces.Transcoder;

public class ByteTranscoder implements Transcoder<String, byte[]> {

	@Override
	public Entry encode(byte[] value, int ttl) {
		return new Entry(value, ttl);
	}

	@Override
	public byte[] decode(Entry entry) {
		return entry.getData();
	}

	@Override
	public String decodeKey(String key) {
		return key;
	}

}
