package de.ngloader.bootstrap;

import de.ngloader.bootstrap.command.ConsoleCommandGarbageCollector;
import de.ngloader.bootstrap.command.ConsoleCommandStartInstance;
import de.ngloader.bootstrap.command.ConsoleCommandStopInstance;
import de.ngloader.bot.WuffyBot;
import de.ngloader.client.WuffyClient;
import de.ngloader.core.Core;
import de.ngloader.core.WuffyPhantomRefernce;
import de.ngloader.core.config.ConfigService;
import de.ngloader.core.console.ConsoleCommandManager;
import de.ngloader.core.logger.Logger;
import de.ngloader.core.logger.LoggerManager;
import de.ngloader.core.util.TickingTask;

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

		var threads = 32;

		for(int i = 0; i < args.length; i++) {
			var arg = args[i];

			if(arg.equalsIgnoreCase("--debug"))
				LoggerManager.setDebug(true);
			else if(arg.equalsIgnoreCase("--threads"))
				threads = Integer.valueOf(args[i + 1]);
		}

		System.setProperty("io.netty.eventLoopThreads", Integer.toString(threads));

		Logger.info("Bootstrap", "Loading and initializing bots and clients");

		GlobalConfig config = ConfigService.getConfig(GlobalConfig.class);

		Logger.info("Bootstrap", "Loading bots...");
		config.bots.stream().filter(bot -> bot.enabled).forEach(bot -> new WuffyBot(bot));
		Logger.info("Bootstrap", "Loaded bots: " + Core.getBots().size());

		Logger.info("Bootstrap", "Loading clients...");
		config.clients.stream().filter(client -> client.enabled).forEach(client -> new WuffyClient(client));
		Logger.info("Bootstrap", "Loaded clients: " + Core.getClients().size());

		Wuffy.instance = new Wuffy(config);
	}

	private final GlobalConfig config;

	private final ConsoleCommandManager consoleCommandManager;

	private final Thread masterThread;

	public Wuffy(GlobalConfig config) {
		this.config = config;

		this.consoleCommandManager = new ConsoleCommandManager();

		this.consoleCommandManager.registerExecutor(new ConsoleCommandStartInstance());
		this.consoleCommandManager.registerExecutor(new ConsoleCommandStopInstance());
		this.consoleCommandManager.registerExecutor(new ConsoleCommandGarbageCollector());

		this.masterThread = new Thread(this, "Wuffy Discord Bot - Master");
		this.masterThread.start();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			this.running = false;
			stop();
		}));

		Logger.info("Bootstrap", "Enabling all instances...");
		Core.getInstances().forEach(Core::enable);
	}

	@Override
	protected void update() {
		this.consoleCommandManager.update();
		WuffyPhantomRefernce.getInstance().update();
	}

	@Override
	protected void stop() {
		Logger.info("Shutdown", "Stopping wuffy... Thanks for using");

		this.consoleCommandManager.close();

		Core.getInstances().forEach(Core::disable);

		Logger.info("Shutdown", "Goodbye");
		LoggerManager.close();
	}

	public GlobalConfig getConfig() {
		return config;
	}

	public ConsoleCommandManager getConsoleCommandManager() {
		return consoleCommandManager;
	}
}