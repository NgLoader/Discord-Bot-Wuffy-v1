package net.wuffy.bootstrap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.wuffy.bootstrap.command.ConsoleCommandDeveloperMode;
import net.wuffy.bootstrap.command.ConsoleCommandGarbageCollector;
import net.wuffy.bootstrap.command.ConsoleCommandStartInstance;
import net.wuffy.bootstrap.command.ConsoleCommandStopInstance;
import net.wuffy.common.WuffyPhantomRefernce;
import net.wuffy.common.config.ConfigService;
import net.wuffy.common.logger.Logger;
import net.wuffy.common.logger.LoggerManager;
import net.wuffy.common.util.ITickable;
import net.wuffy.common.util.TickingTask;
import net.wuffy.console.ConsoleCommandManager;

public class Wuffy extends TickingTask {

	public static final Long START_TIME_IN_MILLIS = System.currentTimeMillis();

	private static Wuffy instance;

	public static Wuffy getInstance() {
		return Wuffy.instance;
	}

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
			else if(arg.equalsIgnoreCase("--dev"))
				System.setProperty("developerMode", "true");
		}

		System.setProperty("io.netty.eventLoopThreads", Integer.toString(threads));

		Logger.info("Bootstrap", "Loading and initializing bots and clients");

		Wuffy.instance = new Wuffy();
	}

	private final Map<UUID, CoreProcess> processes = new HashMap<UUID, CoreProcess>();

	private final List<ITickable> tickables = new ArrayList<ITickable>();

	private final ConsoleCommandManager consoleCommandManager;

	private final Thread masterThread;

	private GlobalConfig config;

	public Wuffy() {
		super(1000 / 40);

		this.tickables.add(WuffyPhantomRefernce.getInstance());

		this.config = ConfigService.getConfig(GlobalConfig.class);

		this.consoleCommandManager = new ConsoleCommandManager();
		this.tickables.add(this.consoleCommandManager);

		this.consoleCommandManager.registerExecutor(new ConsoleCommandStartInstance());
		this.consoleCommandManager.registerExecutor(new ConsoleCommandStopInstance());
		this.consoleCommandManager.registerExecutor(new ConsoleCommandGarbageCollector());
		this.consoleCommandManager.registerExecutor(new ConsoleCommandDeveloperMode());

		this.masterThread = new Thread(this, "Wuffy Discord Bot - Main");
		this.masterThread.start();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			this.running = false;
			this.stop();
		}));
	}

	@Override
	protected void update() {
		this.tickables.forEach(ITickable::update);
		this.processes.values().forEach(ITickable::update);
	}

	@Override
	protected void stop() {
		Logger.info("Shutdown", "Stopping wuffy... Thanks for using");

		this.consoleCommandManager.close();

		this.processes.values().forEach(CoreProcess::stop);

		Logger.info("Shutdown", "Goodbye");
		LoggerManager.close();
	}

	public GlobalConfig getConfig() {
		return this.config;
	}

	public ConsoleCommandManager getConsoleCommandManager() {
		return this.consoleCommandManager;
	}

	public Map<UUID, CoreProcess> getProcesses() {
		return this.processes;
	}
}