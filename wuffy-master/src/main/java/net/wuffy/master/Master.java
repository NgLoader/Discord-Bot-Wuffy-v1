package net.wuffy.master;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLException;

import net.wuffy.common.WuffyPhantomRefernce;
import net.wuffy.common.config.ConfigService;
import net.wuffy.common.logger.Logger;
import net.wuffy.common.logger.LoggerManager;
import net.wuffy.common.util.ITickable;
import net.wuffy.common.util.TickingTask;
import net.wuffy.master.network.loadbalancer.NetworkSystemLoadBalancer;
import net.wuffy.master.network.master.NetworkSystemMaster;
import net.wuffy.master.sharding.ServerHandler;

public class Master extends TickingTask {

	public static final Long START_TIME_IN_MILLIS = System.currentTimeMillis();

	private static Master instance;

	public static Master getInstance() {
		return Master.instance;
	}

	public static void main(String[] args) {
		if(Float.parseFloat(System.getProperty( "java.class.version" )) < 54) {
			System.err.println("*** ERROR *** Wuffy equires Java 10 or above to work! Please download and install it!");
			return;
		}

		Logger.info("Bootstrap", "Starting Wuffy Master.");

		var threads = 32;

		for(int i = 0; i < args.length; i++) {
			var arg = args[i];

			if(arg.equalsIgnoreCase("--debug"))
				LoggerManager.setDebug(true);
			else if(arg.equalsIgnoreCase("--threads"))
				threads = Integer.valueOf(args[i + 1]);
			else if(arg.equalsIgnoreCase("--dev"))
				System.setProperty("developerMode", "true");
		}

		System.setProperty("io.netty.eventLoopThreads", Integer.toString(threads));

		Logger.info("Bootstrap", "Loading");
		Master.instance = new Master();
		Master.instance.start();
	}

	private final MasterConfig config;

	private List<ITickable> tickables = new ArrayList<ITickable>();
	private Thread masterThread;

	private NetworkSystemLoadBalancer networkSystemLoadBalancer;
	private NetworkSystemMaster networkSystemMaster;

	public Master() {
		super(500);

		this.config = ConfigService.getConfig(MasterConfig.class);

		this.tickables.add(WuffyPhantomRefernce.getInstance());
		this.tickables.add(ServerHandler.getInstance());

		this.running = false;

		Logger.info("Bootstrap", "Started");
	}

	public void start() {
		if(this.running)
			return;
		this.running = true;

		try {
			this.networkSystemLoadBalancer = new NetworkSystemLoadBalancer().start(this.config);
			this.tickables.add(this.networkSystemLoadBalancer);

			this.networkSystemMaster = new NetworkSystemMaster().start(this.config);
			this.tickables.add(this.networkSystemMaster);
		} catch (SSLException e) {
			Logger.fatal("Bootstrap", "SSLException", e);
			this.running = false;
			return;
		}

		this.masterThread = new Thread(this, "Wuffy Master");
		this.masterThread.start();
	}

	@Override
	protected void update() {
		this.tickables.forEach(ITickable::update);
	}

	@Override
	protected void stop() {
		Logger.info("Bootstrap", "Stopping master. goodbye");
		this.networkSystemLoadBalancer.close();
		LoggerManager.close();
	}

	public void addTickable(ITickable tickable) {
		this.tickables.add(tickable);
	}

	public MasterConfig getConfig() {
		return config;
	}
}