package net.wuffy.bot;

import java.util.UUID;

import net.dv8tion.jda.core.AccountType;
import net.wuffy.bot.command.CommandExecuterAdapter;
import net.wuffy.bot.database.DBExtension;
import net.wuffy.bot.database.mongo.MongoStorageImpl;
import net.wuffy.bot.jda.JDAAdapter;
import net.wuffy.common.config.ConfigService;
import net.wuffy.common.logger.Logger;
import net.wuffy.common.logger.LoggerManager;
import net.wuffy.console.ConsoleCommandManager;
import net.wuffy.core.Core;
import net.wuffy.core.database.StorageService;
import net.wuffy.core.database.mongo.MongoStorage;
import net.wuffy.core.scheduler.WuffyScheduler;

public class Wuffy extends Core {

	private static Wuffy instance;

	public static Wuffy getInstance() {
		return Wuffy.instance;
	}

	public static void main(String[] args) {
		if(Float.parseFloat(System.getProperty( "java.class.version" )) < 54) {
			System.err.println("*** ERROR *** Wuffy equires Java 10 or above to work! Please download and install it!");
			return;
		}

		Logger.info("Bootstrap", "Starting wuffy.");

		System.setProperty("developerMode", "false");

		int threads = 32;

		for(int i = 0; i < args.length; i++) {
			var arg = args[i];

			if(arg.equalsIgnoreCase("--debug"))
				LoggerManager.setDebug(true);
			else if(arg.equalsIgnoreCase("--threads"))
				threads = Integer.valueOf(args[i + 1]);
			else if(arg.equalsIgnoreCase("--dev"))
				System.setProperty("developerMode", "true");
			else if(arg.equalsIgnoreCase("--useMasterSystem"))
				System.setProperty("useMasterSystem", "true");
		}

		System.setProperty("io.netty.eventLoopThreads", Integer.toString(threads));

		Wuffy.instance = new Wuffy(ConfigService.getConfig(BotConfig.class));
	}

	private JDAAdapter jdaAdapter;
	private StorageService storageService;
	private ConsoleCommandManager consoleCommandManager;
	private CommandExecuterAdapter commandHandlerAdapter;
	private WuffyScheduler scheduler;

	public Wuffy(BotConfig config) {
		super(UUID.randomUUID(), config, AccountType.BOT); //Use not random UUID

		//Storage
		this.storageService = new StorageService(this, config.database);

		if(this.storageService.getStorage() instanceof MongoStorage)
			this.storageService.getStorage(MongoStorage.class).registerProvider(DBExtension.class, new MongoStorageImpl());

		this.storageService.enable();

		//Console
		this.consoleCommandManager = new ConsoleCommandManager();
		this.addTickable(this.consoleCommandManager);

		//Command Handler
		this.commandHandlerAdapter = new CommandExecuterAdapter();

		//Scheduler
		this.scheduler = new WuffyScheduler(this);
		this.addTickable(this.scheduler);

		//JDA start
		this.jdaAdapter = new JDAAdapter(this);
	}

	@Override
	protected void onDestroy() {
		this.consoleCommandManager.close();
		this.scheduler.cancelAllTasks();
		this.jdaAdapter.logout();
		this.storageService.disable();
	}

	public ConsoleCommandManager getConsoleCommandManager() {
		return this.consoleCommandManager;
	}

	public CommandExecuterAdapter getCommandHandlerAdapter() {
		return this.commandHandlerAdapter;
	}

	public StorageService getStorageService() {
		return this.storageService;
	}

	public WuffyScheduler getScheduler() {
		return this.scheduler;
	}

	public JDAAdapter getJdaAdapter() {
		return this.jdaAdapter;
	}
}