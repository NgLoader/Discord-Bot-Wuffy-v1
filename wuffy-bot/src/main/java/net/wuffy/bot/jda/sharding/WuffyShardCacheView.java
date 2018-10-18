package net.wuffy.bot.jda.sharding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import net.dv8tion.jda.bot.utils.cache.ShardCacheView;
import net.dv8tion.jda.core.JDA;

public class WuffyShardCacheView implements ShardCacheView {

	private final Map<Integer, JDA> cache;

	public WuffyShardCacheView() {
		this.cache = new ConcurrentHashMap<Integer, JDA>();
	}

	public WuffyShardCacheView(int initialCapacity) {
		this.cache = new ConcurrentHashMap<Integer, JDA>(initialCapacity);
	}

	@Override
	public List<JDA> asList() {
		return Collections.unmodifiableList(new ArrayList<JDA>(this.cache.values()));
	}

	@Override
	public Set<JDA> asSet() {
		return Collections.unmodifiableSet(new HashSet<JDA>(this.cache.values()));
	}

	@Override
	public long size() {
		return this.cache.size();
	}

	@Override
	public boolean isEmpty() {
		return this.cache.isEmpty();
	}

	@Override
	public List<JDA> getElementsByName(String name, boolean ignoreCase) {
		List<JDA> found = new LinkedList<JDA>();
		for(JDA jda : this.cache.values()) {
			String shardString = jda.getShardInfo().getShardString();

			if(shardString != null && (ignoreCase ? shardString.equalsIgnoreCase(name) : shardString.equals(name)))
				found.add(jda);
		}
		return found;
	}

	@Override
	public Stream<JDA> stream() {
		return this.cache.values().stream();
	}

	@Override
	public Stream<JDA> parallelStream() {
		return this.cache.values().parallelStream();
	}

	@Override
	public Iterator<JDA> iterator() {
		return this.cache.values().iterator();
	}

	@Override
	public JDA getElementById(int id) {
		return this.cache.get(id);
	}
}