package net.wuffy.bootstrap;

import net.wuffy.bootstrap.command.ConsoleCommandDeveloperMode;
import net.wuffy.bootstrap.command.ConsoleCommandGarbageCollector;
import net.wuffy.bootstrap.command.ConsoleCommandStartInstance;
import net.wuffy.bootstrap.command.ConsoleCommandStopInstance;
import net.wuffy.bot.WuffyBot;
import net.wuffy.client.WuffyClient;
import net.wuffy.common.WuffyPhantomRefernce;
import net.wuffy.common.config.ConfigService;
import net.wuffy.common.logger.Logger;
import net.wuffy.common.logger.LoggerManager;
import net.wuffy.common.util.TickingTask;
import net.wuffy.core.Core;
import net.wuffy.core.console.ConsoleCommandManager;

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

		GlobalConfig config = ConfigService.getConfig(GlobalConfig.class);

		Logger.info("Bootstrap", "Loading bots...");
		config.bots.stream().filter(bot -> bot.enabled).forEach(bot -> { bot.admins.addAll(config.admins); new WuffyBot(bot); });
		Logger.info("Bootstrap", "Loaded bots: " + Core.getBots().size());

		Logger.info("Bootstrap", "Loading clients...");
		config.clients.stream().filter(client -> client.enabled).forEach(client -> { client.admins.addAll(config.admins); new WuffyClient(client); });
		Logger.info("Bootstrap", "Loaded clients: " + Core.getClients().size());

		Wuffy.instance = new Wuffy(config);
	}

	private final GlobalConfig config;

	private final ConsoleCommandManager consoleCommandManager;

	private final Thread masterThread;

	public Wuffy(GlobalConfig config) {
		super(1000 / 40);

		this.config = config;

		this.consoleCommandManager = new ConsoleCommandManager();

		this.consoleCommandManager.registerExecutor(new ConsoleCommandStartInstance());
		this.consoleCommandManager.registerExecutor(new ConsoleCommandStopInstance());
		this.consoleCommandManager.registerExecutor(new ConsoleCommandGarbageCollector());
		this.consoleCommandManager.registerExecutor(new ConsoleCommandDeveloperMode());

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