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

	private YoutubeAPI youtube;

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

		this.youtube = new YoutubeAPI(this.getConfig(BotConfig.class).youtubeToken);

//		this.commandManager = new de.ngloader.bot.command.CommandManager(this);
		new ShardInitializer(this);
	}

	@Override
	protected void onEnable() {
	}

	@Override
	protected void onDisable() {
	}

	public YoutubeAPI getYoutube() {
		return this.youtube;
	}

	public BotConfig getConfig() {
		return this.getConfig(BotConfig.class);
	}
}