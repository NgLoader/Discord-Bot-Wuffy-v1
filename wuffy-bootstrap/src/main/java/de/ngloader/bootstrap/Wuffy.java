package de.ngloader.bootstrap;

import de.ngloader.bot.WuffyBot;
import de.ngloader.client.WuffyClient;
import de.ngloader.core.Core;
import de.ngloader.core.config.ConfigService;
import de.ngloader.core.console.ConsoleCommandManager;
import de.ngloader.core.logger.Logger;
import de.ngloader.core.logger.LoggerManager;
import de.ngloader.core.util.TickingTask;

public class Wuffy extends TickingTask {

	public static final Long START_TIME_IN_MILLIS = System.currentTimeMillis();

	public static void main(String[] args) {
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

		Logger.info("Bootstrap", "Initializing bots and clients");

		GlobalConfig config = ConfigService.getConfig(GlobalConfig.class);

		Logger.info("Bootstrap", "Loading bots...");
		config.bots.stream().filter(bot -> bot.enabled).forEach(bot -> new WuffyBot(bot));
		Logger.info("Bootstrap", "Loaded bots: " + Core.getBots().size());

		Logger.info("Bootstrap", "Loading clients...");
		config.clients.stream().filter(client -> client.enabled).forEach(client -> new WuffyClient(client));
		Logger.info("Bootstrap", "Loaded clients: " + Core.getClients().size());

		Logger.info("Bootstrap", "Starting all enabled instances...");
		Core.getInstances().forEach(Core::enable);

		new Wuffy();
	}

	private ConsoleCommandManager consoleCommandManager;

	private Thread masterThread;

	public Wuffy() {
		this.consoleCommandManager = new ConsoleCommandManager();

		this.masterThread = new Thread(this, "Wuffy Discord Bot - Core");
		this.masterThread.start();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			this.running = false;
			stop();
		}));
	}

	@Override
	protected void update() {
		this.consoleCommandManager.update();
	}

	@Override
	protected void stop() {
		Logger.info("Shutdown", "Stopping wuffy... Thanks for using");

		this.consoleCommandManager.close();

		Logger.info("Shutdown", "Goodbye");
		LoggerManager.close();
	}
}