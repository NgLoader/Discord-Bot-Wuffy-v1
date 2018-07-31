package de.ngloader.bot;

import de.ngloader.bot.database.guild.locale.LocaleExtensionGuild;
import de.ngloader.bot.database.guild.mongo.MongoExtensionGuild;
import de.ngloader.bot.database.guild.sql.SQLExtensionGuild;
import de.ngloader.bot.database.lang.LocaleExtensionLang;
import de.ngloader.bot.database.lang.MongoExtensionLang;
import de.ngloader.bot.database.lang.POEditorExtensionLang;
import de.ngloader.bot.database.lang.SQLExtensionLang;
import de.ngloader.bot.database.user.locale.LocaleExtensionUser;
import de.ngloader.bot.database.user.mongo.MongoExtensionUser;
import de.ngloader.bot.database.user.sql.SQLExtensionUser;
import de.ngloader.bot.jda.JDAAdapter;
import de.ngloader.core.Core;
import de.ngloader.core.command.CommandManager;
import de.ngloader.core.database.impl.IExtensionGuild;
import de.ngloader.core.database.impl.IExtensionLang;
import de.ngloader.core.database.impl.IExtensionUser;
import de.ngloader.core.database.locale.LocaleStorage;
import de.ngloader.core.database.mongo.MongoStorage;
import de.ngloader.core.database.poeditor.POEditorStorage;
import de.ngloader.core.database.sql.SQLStorage;
import de.ngloader.core.youtube.YoutubeAPI;
import net.dv8tion.jda.core.AccountType;

public class WuffyBot extends Core {

	private CommandManager<WuffyBot> commandManager;

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

		this.commandManager = new de.ngloader.bot.command.CommandManager(this);
	}

	@Override
	protected void onEnable() {
	}

	@Override
	protected void onDisable() {
	}

	public CommandManager<WuffyBot> getCommandManager() {
		return this.commandManager;
	}

	public YoutubeAPI getYoutube() {
		return this.youtube;
	}

	public BotConfig getConfig() {
		return this.getConfig(BotConfig.class);
	}
}