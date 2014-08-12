package io.ganguo.app.gcache.transcoder;

import io.ganguo.app.gcache.interfaces.Cache.Entry;
import io.ganguo.app.gcache.interfaces.Transcoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectTranscoder implements Transcoder<String, Object> {

	@Override
	public Entry encode(Object value, int ttl) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(value);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {}
		}
		return new Entry(bos.toByteArray(), ttl);
	}

	@Override
	public Object decode(Entry entry) {
		ByteArrayInputStream bis = new ByteArrayInputStream(entry.getData());
		ObjectInputStream in = null;
		Object obj = null;
		try {
			in = new ObjectInputStream(bis);
			obj = in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {}
		}
		return obj;
	}

	@Override
	public String decodeKey(String key) {
		return key;
	}

}
