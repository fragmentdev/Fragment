package me.xemu.fragment.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Cache<T> {
	private final Map<String, T> cacheMap;

	public Cache() {
		this.cacheMap = new HashMap<>();
	}

	public void put(String key, T value) {
		cacheMap.put(key, value);
	}

	public T get(String key) {
		return cacheMap.get(key);
	}

	public boolean contains(String key) {
		return cacheMap.containsKey(key);
	}

	public void remove(String key) {
		cacheMap.remove(key);
	}

	public void clear() {
		cacheMap.clear();
	}

	public int getSize() {
		return cacheMap.size();
	}

	public boolean isEmpty() {
		return cacheMap.isEmpty();
	}

	public Set<String> getKeys() {
		return cacheMap.keySet();
	}
}
