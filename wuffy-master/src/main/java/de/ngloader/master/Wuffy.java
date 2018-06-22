package de.ngloader.master;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.ngloader.api.IShardProvider;
import de.ngloader.api.WuffyConfig;
import de.ngloader.api.WuffyServer;
import de.ngloader.api.command.ICommandManager;
import de.ngloader.api.config.IConfigService;
import de.ngloader.api.database.DatabaseConfig;
import de.ngloader.api.database.IStorageService;
import de.ngloader.api.database.impl.guild.IExtensionGuild;
import de.ngloader.api.database.impl.guild.IWuffyGuild;
import de.ngloader.api.database.impl.lang.IExtensionLanguage;
import de.ngloader.api.database.impl.user.IExtensionUser;
import de.ngloader.api.database.impl.user.IWuffyUser;
import de.ngloader.api.database.mongo.MongoStorage;
import de.ngloader.api.database.sql.SQLStorage;
import de.ngloader.api.logger.ILogger;
import de.ngloader.command.CommandManager;
import de.ngloader.common.logger.LoggerManager;
import de.ngloader.database.ModuleStorageService;
import de.ngloader.database.impl.guild.MongoExtensionGuild;
import de.ngloader.database.impl.guild.SQLExtensionGuild;
import de.ngloader.database.impl.guild.WuffyGuild;
import de.ngloader.database.impl.lang.MongoExtensionLanguage;
import de.ngloader.database.impl.lang.SQLExtensionLanguage;
import de.ngloader.database.impl.user.MongoExtensionUser;
import de.ngloader.database.impl.user.SQLExtensionUser;
import de.ngloader.database.impl.user.WuffyUser;
import de.ngloader.master.command.CommandRegistry;
import de.ngloader.master.config.ConfigService;
import de.ngloader.master.listener.MessageListener;
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

		new Wuffy(debug);
	}

	private final Thread masterThread;

	private ShardProvider shardProvider;
	private CommandManager commandManager;
	private ModuleStorageService moduleStorageService;
	private IConfigService configService;

	private final Map<Long, IWuffyGuild> guilds = new HashMap<Long, IWuffyGuild>();
	private final Map<Long, IWuffyUser> users = new HashMap<Long, IWuffyUser>();

	public Wuffy(boolean debug) {
		WuffyServer.setInstance(this);

		this.masterThread = new Thread(this, "Wuffy master thread");

		/* SETTINGS START */
		WuffyServer.getLogger().info("Startup config", "Loading");
		this.configService = new ConfigService();
		this.configService.loadConfig(WuffyConfig.class);
		this.configService.loadConfig(DatabaseConfig.class);
		WuffyServer.getLogger().info("Startup conifg", "Loaded");
		/* SETTINGS END */

		/* DATABASE START */
		WuffyServer.getLogger().info("Startup database", "Starting");
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

		WuffyServer.getLogger().info("Startup database", "Enabling");
		this.moduleStorageService.enable();
		WuffyServer.getLogger().info("Startup database", "Loaded");
		/* DATABASE END */

		
	
		/* BOT START */
		this.shardProvider = new ShardProvider(new ReadyListenerAdapter() {

			public void onReady(net.dv8tion.jda.core.events.ReadyEvent event) {
				/* COMMAND START */
				WuffyServer.getLogger().info("Startup command", "Loading");
				commandManager = new CommandManager();
				WuffyServer.getLogger().info("Startup command", "Adding commands");
				CommandRegistry.register();
				WuffyServer.getLogger().info("Startup command", "Loaded");
				/* COMMAND END */

				event.getJDA().removeEventListener(this);
				event.getJDA().addEventListener(new MessageListener());

				masterThread.start();
			};
		});
		/* BOT END */
	}

	@Override
	protected void update() {
		this.commandManager.update();
	}

	@Override
	protected void stop() {
		getLogger().info("Stopping wuffy. Goodbye my friend :)");

		LoggerManager.close();
	}

	@Override
	protected ILogger getLogger0() {
		return LoggerManager.getLogger();
	}

	@Override
	protected IStorageService getStorageService0() {
		return this.moduleStorageService;
	}

	@Override
	protected IShardProvider getShardProvider0() {
		return this.shardProvider;
	}

	@Override
	protected ICommandManager getCommandManager0() {
		return this.commandManager;
	}

	@Override
	protected IConfigService getConfigService0() {
		return this.configService;
	}

	@Override
	protected IWuffyUser getUser0(Long longId) {
		if(!this.users.containsKey(longId))
			this.users.put(longId, new WuffyUser(this.shardProvider.getJDA().getUserById(longId)));

		return this.users.get(longId);
	}

	@Override
	protected IWuffyGuild getGuild0(Long longId) {
		if(!this.guilds.containsKey(longId))
			this.guilds.put(longId, new WuffyGuild(this.shardProvider.getJDA().getGuildById(longId)));

		return this.guilds.get(longId);
	}
}