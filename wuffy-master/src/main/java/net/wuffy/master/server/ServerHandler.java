package net.wuffy.master.server;

import java.util.LinkedList;
import java.util.List;

public class ServerHandler {

	private List<Server> servers = new LinkedList<Server>();

	public void addServer(Server server) {
		this.servers.add(server);
	}

	public void removeServer(Server server) {
		this.servers.remove(server);

		server.destroy();
	}

	public List<Server> getServers() {
		return this.servers;
	}
}