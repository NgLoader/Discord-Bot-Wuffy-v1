package net.wuffy.master.sharding;

import net.wuffy.common.WuffyPhantomRefernce;
import net.wuffy.common.util.IWuffyPhantomReference;
import net.wuffy.master.server.Server;

public class Shard implements IWuffyPhantomReference {

	private final Server server;
	private final int id;

	public Shard(Server server, int id) {
		this.server = server;
		this.id = id;

		WuffyPhantomRefernce.getInstance().add(this, String.format("Shard \"%s\" from \"%s\"", this.id, this.server.getName()));
	}

	public Server getServer() {
		return this.server;
	}

	public int getId() {
		return this.id;
	}
}