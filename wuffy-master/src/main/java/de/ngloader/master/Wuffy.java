package de.ngloader.master;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.ngloader.api.IJDAProvider;
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
import de.ngloader.master.provider.JDAProvider;
import net.dv8tion.jda.core.JDA;

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

	private JDAProvider jdaProvider;
	private CommandManager commandManager;
	private ModuleStorageService moduleStorageService;
	private IConfigService configService;

	private final Map<Long, IWuffyGuild> guilds = new HashMap<Long, IWuffyGuild>();
	private final Map<Long, IWuffyUser> users = new HashMap<Long, IWuffyUser>();

	public Wuffy(boolean debug) {
		WuffyServer.setInstance(this);

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

		/* COMMAND START */
		WuffyServer.getLogger().info("Startup command", "Loading");
		commandManager = new CommandManager();
		WuffyServer.getLogger().info("Startup command", "Adding commands");
		CommandRegistry.register();
		WuffyServer.getLogger().info("Startup command", "Loaded");
		/* COMMAND END */

		/* TICKINGTASK START */
		this.masterThread = new Thread(this, "Wuffy master thread");
		masterThread.start();
		/* TICKINGTASK STOP */

		/* JDA START */
		this.jdaProvider = new JDAProvider();
		/* JDA STOP */
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
	protected IJDAProvider getJDAProvider0() {
		return this.jdaProvider;
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
	protected IWuffyUser getUser0(JDA jda, Long longId) {
		if(!this.users.containsKey(longId))
			this.users.put(longId, new WuffyUser(jda.getUserById(longId)));

		return this.users.get(longId);
	}

	@Override
	protected IWuffyGuild getGuild0(JDA jda, Long longId) {
		if(!this.guilds.containsKey(longId))
			this.guilds.put(longId, new WuffyGuild(jda.getGuildById(longId)));

		return this.guilds.get(longId);
	}
}