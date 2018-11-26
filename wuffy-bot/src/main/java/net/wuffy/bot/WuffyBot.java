package net.wuffy.bot;

import net.dv8tion.jda.core.AccountType;
import net.wuffy.bot.database.guild.locale.LocaleExtensionGuild;
import net.wuffy.bot.database.guild.mongo.MongoExtensionGuild;
import net.wuffy.bot.database.guild.sql.SQLExtensionGuild;
import net.wuffy.bot.database.lang.LocaleExtensionLang;
import net.wuffy.bot.database.lang.MongoExtensionLang;
import net.wuffy.bot.database.lang.POEditorExtensionLang;
import net.wuffy.bot.database.lang.SQLExtensionLang;
import net.wuffy.bot.database.user.locale.LocaleExtensionUser;
import net.wuffy.bot.database.user.mongo.MongoExtensionUser;
import net.wuffy.bot.database.user.sql.SQLExtensionUser;
import net.wuffy.bot.jda.JDAAdapter;
import net.wuffy.bot.jda.ShardInitializer;
import net.wuffy.common.config.ConfigService;
import net.wuffy.common.logger.Logger;
import net.wuffy.common.logger.LoggerManager;
import net.wuffy.core.Core;
import net.wuffy.core.database.impl.IExtensionGuild;
import net.wuffy.core.database.impl.IExtensionLang;
import net.wuffy.core.database.impl.IExtensionUser;
import net.wuffy.core.database.locale.LocaleStorage;
import net.wuffy.core.database.mongo.MongoStorage;
import net.wuffy.core.database.poeditor.POEditorStorage;
import net.wuffy.core.database.sql.SQLStorage;
import net.wuffy.core.youtube.YoutubeAPI;

public class WuffyBot extends Core {

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

		if(!System.getProperty("useMasterSystem").equals("true"))
			new WuffyBot(ConfigService.getConfig(BotConfig.class));
		else
			
	}

	public WuffyBot(BotConfig config) {
		super(config, AccountType.BOT, JDAAdapter.class);

		POEditorStorage poeditorStorage = this.storageService.getStorage(POEditorStorage.class);
		LocaleStorage localeStorage = this.storageService.getStorage(LocaleStorage.class);
		MongoStorage mongoStorage = this.storageService.getStorage(MongoStorage.class);
		SQLStorage sqlStorage = this.storageService.getStorage(SQLStorage.class);

		if(poeditorStorage != null)
			poeditorStorage.registerProvider(IExtensionLang.class, new POEditorExtensionLang());

		if(localeStorage != null) {
			localeStorage.registerProvider(IExtensionGuild.class, new LocaleExtensionGuild());
			localeStorage.registerProvider(IExtensionUser.class, new LocaleExtensionUser());
			localeStorage.registerProvider(IExtensionLang.class, new LocaleExtensionLang());
		}
		if(mongoStorage != null) {
			mongoStorage.registerProvider(IExtensionGuild.class, new MongoExtensionGuild());
			mongoStorage.registerProvider(IExtensionUser.class, new MongoExtensionUser());
			mongoStorage.registerProvider(IExtensionLang.class, new MongoExtensionLang());
		}
		if(sqlStorage != null) {
			sqlStorage.registerProvider(IExtensionGuild.class, new SQLExtensionGuild());
			sqlStorage.registerProvider(IExtensionUser.class, new SQLExtensionUser());
			sqlStorage.registerProvider(IExtensionLang.class, new SQLExtensionLang());
		}
	}

	public BotConfig getConfig() {
		return this.getConfig(BotConfig.class);
	}
}