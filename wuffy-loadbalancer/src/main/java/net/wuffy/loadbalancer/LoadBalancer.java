package net.wuffy.loadbalancer;

import java.net.InetSocketAddress;
import java.util.UUID;

import javax.net.ssl.SSLException;

import net.wuffy.common.config.ConfigService;
import net.wuffy.common.logger.Logger;
import net.wuffy.common.logger.LoggerManager;
import net.wuffy.common.util.TickingTask;
import net.wuffy.common.util.WebUtil;
import net.wuffy.console.ConsoleCommandManager;
import net.wuffy.loadbalancer.auth.AuthManager;
import net.wuffy.loadbalancer.command.CommandMaster;
import net.wuffy.loadbalancer.network.NetworkSystem;
import net.wuffy.loadbalancer.network.dns.DNSConfig;
import net.wuffy.loadbalancer.network.dns.DNSServer;

public class LoadBalancer extends TickingTask {

	public static final int defaultMasterAddress = WebUtil.convertIpToInt("127.0.0.1");

	public static int currentlyMasterAddress = LoadBalancer.defaultMasterAddress;

	public static int currentlyReconnectTrys = 0;
	public static UUID currentlyMasterID = null;

	public static LoadBalancer instance;

	public static void main(String[] args) throws Exception {
		if(Float.parseFloat(System.getProperty( "java.class.version" )) < 54) {
			System.err.println("*** ERROR *** Wuffy equires Java 10 or above to work! Please download and install it!");
			return;
		}

		Logger.info("Bootstrap", "Starting wuffy.");

		System.setProperty("developerMode", "false");

		var threads = 32;

		for(int i = 0; i < args.length; i++) {
			var arg = args[i];

			if(arg.equalsIgnoreCase("--debug"))
				LoggerManager.setDebug(true);
			else if(arg.equalsIgnoreCase("--threads"))
				threads = Integer.valueOf(args[i + 1]);
		}

		System.setProperty("io.netty.eventLoopThreads", Integer.toString(threads));

		Logger.info("Bootstrap", "Loading config");

		LoadBalancer.instance = new LoadBalancer();
		LoadBalancer.instance.run();
	}

	private final DNSConfig config;

	private ConsoleCommandManager consoleCommandManager;

	private NetworkSystem networkSystem;
	private DNSServer dnsServer;

	public LoadBalancer() {
		super(1000 / 10);

		Thread.currentThread().setName("Wuffy DNS - Main");

		this.config = ConfigService.getConfig(DNSConfig.class);

		AuthManager.initialize();

		Logger.info("Bootstrap", "Starting NetworkSystem Server");

		try {
			this.networkSystem = new NetworkSystem().start(this.config.master, new InetSocketAddress(config.master.host, config.master.port));
		} catch (SSLException e) {
			Logger.fatal("Bootstrap", "SSLException", e);
			this.running = false;
			return;
		}
	
		Logger.info("Bootstrap", "Starting DNS Server");
		this.dnsServer = new DNSServer(config);
		this.dnsServer.start();

		Logger.info("Bootstrap", "Starting CommandManager");
		this.consoleCommandManager = new ConsoleCommandManager();
		this.consoleCommandManager.registerExecutor(new CommandMaster());

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			this.running = false;

			Logger.info("ShutdownHook", "Shutting down LoadBalancer");
			this.stop();
		}));
	}

	@Override
	protected void update() {
		this.networkSystem.update();
		this.dnsServer.update();
		this.consoleCommandManager.update();
	}

	@Override
	protected void stop() {
		this.networkSystem.close();
		this.consoleCommandManager.close();
		LoggerManager.close();
	}

	public DNSConfig getConfig() {
		return this.config;
	}

	public NetworkSystem getNetworkSystem() {
		return this.networkSystem;
	}

	public DNSServer getDnsServer() {
		return this.dnsServer;
	}

	public ConsoleCommandManager getConsoleCommandManager() {
		return this.consoleCommandManager;
	}
}