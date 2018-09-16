package net.wuffy.master;

import java.util.ArrayList;
import java.util.List;

import net.wuffy.common.WuffyPhantomRefernce;
import net.wuffy.common.config.ConfigService;
import net.wuffy.common.logger.Logger;
import net.wuffy.common.logger.LoggerManager;
import net.wuffy.common.util.ITickable;
import net.wuffy.common.util.TickingTask;
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
	}

	private final MasterConfig config;

	private List<ITickable> tickables = new ArrayList<ITickable>();
	private Thread masterThread;

	public Master() {
		super(500);

		this.config = ConfigService.getConfig(MasterConfig.class);

		this.tickables.add(WuffyPhantomRefernce.getInstance());
		this.tickables.add(ServerHandler.getInstance());

		this.masterThread = new Thread(this, "Wuffy Master");
		this.masterThread.start();

		Logger.info("Bootstrap", "Started");
	}

	@Override
	protected void update() {
		this.tickables.forEach(ITickable::update);
	}

	@Override
	protected void stop() {
		Logger.info("Bootstrap", "Stopping master. goodbye");
		LoggerManager.close();
	}

	public void addTickable(ITickable tickable) {
		this.tickables.add(tickable);
	}

	public MasterConfig getConfig() {
		return config;
	}
}