package net.wuffy.common.util;

import java.util.Map.Entry;

public class Pair<K, V> implements Entry<K, V> {
	
	protected final K key;
	protected V value;
	
	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public K getKey() {
		return this.key;
	}

	@Override
	public V getValue() {
		return this.value;
	}

	@Override
	public V setValue(V value) {
		var oldValue = this.value;
		this.value = value;
		return oldValue;
	}
}