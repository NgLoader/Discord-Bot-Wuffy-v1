package de.ngloader.master;

import java.io.File;

import de.ngloader.command.CommandManager;
import de.ngloader.common.logger.LoggerManager;
import de.ngloader.common.util.TickingTask;
import de.ngloader.database.ModuleStorageService;
import de.ngloader.database.impl.guild.ExtensionGuild;
import de.ngloader.database.impl.guild.MongoExtensionGuild;
import de.ngloader.database.impl.guild.SQLExtensionGuild;
import de.ngloader.database.impl.user.ExtensionUser;
import de.ngloader.database.impl.user.MongoExtensionUser;
import de.ngloader.database.impl.user.SQLExtensionUser;
import de.ngloader.database.mongo.MongoStorage;
import de.ngloader.database.sql.SQLStorage;
import de.ngloader.master.provider.ShardProvider;

public class Wuffy extends TickingTask {

	public static void main(String[] args) {
		try {
			Class.forName("de.ngloader.common.logger.LoggerManager");
		} catch (ClassNotFoundException e) {
			System.out.println("Failed to init LoggerManager");
			System.exit(0);
			return;
		}

		var threads = 32;

		for(int i = 0; i < args.length; i++) {
			var arg = args[i];

			if(arg.equalsIgnoreCase("--debug"))
				LoggerManager.setDebug(true);
			else if(arg.equalsIgnoreCase("--threads"))
				threads = Integer.valueOf(args[i + 1]);
		}

		System.setProperty("io.netty.eventLoopThreads", Integer.toString(threads));

		new Wuffy();
	}

	private static Wuffy instance;

	private final Thread masterThread;

	private final ShardProvider shardProvider;
	private final CommandManager commandManager;
	private final ModuleStorageService moduleStorageService;

	public Wuffy() {
		Wuffy.instance = this;

		/* DATABASE START */
		this.moduleStorageService = new ModuleStorageService(new File("./wuffy/").toPath().resolve("database.yml"));

		this.moduleStorageService.registerExtension("guild", ExtensionGuild.class);
		this.moduleStorageService.registerExtension("user", ExtensionUser.class);

		var mongoStorage = this.moduleStorageService.getStorage(MongoStorage.class);
		var sqlStorage = this.moduleStorageService.getStorage(SQLStorage.class);

		mongoStorage.registerProvider(ExtensionGuild.class, new MongoExtensionGuild());
		sqlStorage.registerProvider(ExtensionGuild.class, new SQLExtensionGuild());

		mongoStorage.registerProvider(ExtensionUser.class, new MongoExtensionUser());
		sqlStorage.registerProvider(ExtensionUser.class, new SQLExtensionUser());

		this.moduleStorageService.enable();
		/* DATABASE END */

		/* COMMAND START */
		this.commandManager = new CommandManager();
		/* COMMAND END */

		/* BOT START */
		this.shardProvider = new ShardProvider();
		/* BOT END */

		this.masterThread = new Thread(this, "Wuffy master thread");
		this.masterThread.start();
	}

	public static ModuleStorageService getModuleStorageService() {
		return Wuffy.instance.moduleStorageService;
	}

	public static CommandManager getCommandManager() {
		return Wuffy.instance.commandManager;
	}

	public static ShardProvider getShardProvider() {
		return Wuffy.instance.shardProvider;
	}

	@Override
	protected void update() {
		this.commandManager.update();
	}

	@Override
	protected void stop() {
		LOGGER.info("Stopping wuffy");

		LoggerManager.close();
	}
}