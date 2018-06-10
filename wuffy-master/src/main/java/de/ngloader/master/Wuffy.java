package de.ngloader.master;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.ngloader.api.IShardProvider;
import de.ngloader.api.WuffyServer;
import de.ngloader.api.command.ICommandManager;
import de.ngloader.api.database.IStorageService;
import de.ngloader.api.database.impl.guild.IExtensionGuild;
import de.ngloader.api.database.impl.guild.IWuffyGuild;
import de.ngloader.api.database.impl.lang.IExtensionLanguage;
import de.ngloader.api.database.impl.user.IExtensionUser;
import de.ngloader.api.database.impl.user.IWuffyUser;
import de.ngloader.api.database.mongo.MongoStorage;
import de.ngloader.api.database.sql.SQLStorage;
import de.ngloader.api.logger.ILogger;
import de.ngloader.api.logger.ILoggerManager;
import de.ngloader.command.CommandManager;
import de.ngloader.common.logger.LoggerManager;
import de.ngloader.database.ModuleStorageService;
import de.ngloader.database.impl.guild.MongoExtensionGuild;
import de.ngloader.database.impl.guild.SQLExtensionGuild;
import de.ngloader.database.impl.lang.MongoExtensionLanguage;
import de.ngloader.database.impl.lang.SQLExtensionLanguage;
import de.ngloader.database.impl.lang.WuffyGuild;
import de.ngloader.database.impl.user.MongoExtensionUser;
import de.ngloader.database.impl.user.SQLExtensionUser;
import de.ngloader.database.impl.user.WuffyUser;
import de.ngloader.master.provider.ShardProvider;

public class Wuffy extends WuffyServer {

	public static void main(String[] args) {
		var debug = false;
		var threads = 32;

		for(int i = 0; i < args.length; i++) {
			var arg = args[i];

			if(arg.equalsIgnoreCase("--debug"))
				debug = true;
			else if(arg.equalsIgnoreCase("--threads"))
				threads = Integer.valueOf(args[i + 1]);
		}

		System.setProperty("io.netty.eventLoopThreads", Integer.toString(threads));

		WuffyServer.setInstance(new Wuffy(debug));
	}

	private final Thread masterThread;

	private final LoggerManager loggerManager;
	private final ShardProvider shardProvider;
	private final CommandManager commandManager;
	private final ModuleStorageService moduleStorageService;

	private final Map<Long, IWuffyGuild> guilds = new HashMap<Long, IWuffyGuild>();
	private final Map<Long, IWuffyUser> users = new HashMap<Long, IWuffyUser>();

	public Wuffy(boolean debug) {
		this.loggerManager = new LoggerManager();

		/* DATABASE START */
		this.moduleStorageService = new ModuleStorageService(new File("./").toPath().resolve("database.yml"));

		this.moduleStorageService.registerExtension("guild", IExtensionGuild.class);
		this.moduleStorageService.registerExtension("user", IExtensionUser.class);
		this.moduleStorageService.registerExtension("lang", IExtensionLanguage.class);

		var mongoStorage = this.moduleStorageService.getStorage(MongoStorage.class);
		var sqlStorage = this.moduleStorageService.getStorage(SQLStorage.class);

		if(mongoStorage != null) {
			mongoStorage.registerProvider(IExtensionGuild.class, new MongoExtensionGuild());
			mongoStorage.registerProvider(IExtensionUser.class, new MongoExtensionUser());
			mongoStorage.registerProvider(IExtensionLanguage.class, new MongoExtensionLanguage());
		}

		if(sqlStorage != null) {
			sqlStorage.registerProvider(IExtensionGuild.class, new SQLExtensionGuild());
			sqlStorage.registerProvider(IExtensionUser.class, new SQLExtensionUser());
			sqlStorage.registerProvider(IExtensionLanguage.class, new SQLExtensionLanguage());
		}

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

	@Override
	protected void update() {
		this.commandManager.update();
	}

	@Override
	protected void stop() {
		LOGGER.info("Stopping wuffy");

		this.loggerManager.close();
	}

	@Override
	public ILogger getLogger0() {
		return this.loggerManager.getLogger();
	}

	@Override
	public ILoggerManager getLoggerManager0() {
		return this.loggerManager;
	}

	@Override
	public IStorageService getStorageService0() {
		return this.moduleStorageService;
	}

	@Override
	public IShardProvider getShardProvider0() {
		return this.shardProvider;
	}

	@Override
	public ICommandManager getCommandManager0() {
		return this.commandManager;
	}

	@Override
	public IWuffyUser getUser0(Long longId) {
		if(!this.users.containsKey(longId))
			this.users.put(longId, new WuffyUser(this.shardProvider.getJDA().getUserById(longId)));

		return this.users.get(longId);
	}

	@Override
	public IWuffyGuild getGuild0(Long longId) {
		if(!this.guilds.containsKey(longId))
			this.guilds.put(longId, new WuffyGuild(this.shardProvider.getJDA().getGuildById(longId)));

		return this.guilds.get(longId);
	}
}