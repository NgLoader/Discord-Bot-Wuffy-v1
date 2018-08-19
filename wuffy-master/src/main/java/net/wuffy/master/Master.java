package net.wuffy.master;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

import net.wuffy.common.logger.Logger;
import net.wuffy.common.logger.LoggerManager;
import net.wuffy.common.util.CryptUtil;
import net.wuffy.common.util.ITickable;
import net.wuffy.common.util.TickingTask;

public class Master extends TickingTask {

	public static final KeyPair KEY_PAIR = CryptUtil.generateKeyPair();

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
		new Master();
	}

	private List<ITickable> tickables = new ArrayList<ITickable>();
	private Thread masterThread;

	public Master() {
		super(1000 / 40);

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
}