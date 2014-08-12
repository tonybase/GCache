package io.ganguo.app.gcache.interfaces;

import io.ganguo.app.gcache.interfaces.Cache.Entry;


public interface Transcoder<V> {
	public V decode(Entry entry);

	public Entry encode(V value, int ttl);
}
