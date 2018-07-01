package de.ngloader.bot;

import de.ngloader.bot.database.guild.LocaleExtensionGuild;
import de.ngloader.bot.database.guild.MongoExtensionGuild;
import de.ngloader.bot.database.guild.SQLExtensionGuild;
import de.ngloader.bot.database.lang.LocaleExtensionLang;
import de.ngloader.bot.database.lang.MongoExtensionLang;
import de.ngloader.bot.database.lang.SQLExtensionLang;
import de.ngloader.bot.database.user.LocaleExtensionUser;
import de.ngloader.bot.database.user.MongoExtensionUser;
import de.ngloader.bot.database.user.SQLExtensionUser;
import de.ngloader.bot.jda.JDAAdapter;
import de.ngloader.core.Core;
import de.ngloader.core.command.CommandManager;
import de.ngloader.core.database.impl.IExtensionGuild;
import de.ngloader.core.database.impl.IExtensionLang;
import de.ngloader.core.database.impl.IExtensionUser;
import de.ngloader.core.database.locale.LocaleStorage;
import de.ngloader.core.database.mongo.MongoStorage;
import de.ngloader.core.database.sql.SQLStorage;
import net.dv8tion.jda.core.AccountType;

public class WuffyBot extends Core {

	private final BotConfig config;

	private final CommandManager<WuffyBot> commandManager;

	public WuffyBot(BotConfig config) {
		super(config, AccountType.BOT, JDAAdapter.class);
		this.config = config;

		if(this.storageService.isStorageRegisterd(MongoStorage.class)) {
			this.storageService.getStorage(MongoStorage.class).registerProvider(IExtensionGuild.class, new MongoExtensionGuild());
			this.storageService.getStorage(MongoStorage.class).registerProvider(IExtensionUser.class, new MongoExtensionUser());
			this.storageService.getStorage(MongoStorage.class).registerProvider(IExtensionLang.class, new MongoExtensionLang());
		}
		if(this.storageService.isStorageRegisterd(SQLStorage.class)) {
			this.storageService.getStorage(SQLStorage.class).registerProvider(IExtensionGuild.class, new SQLExtensionGuild());
			this.storageService.getStorage(SQLStorage.class).registerProvider(IExtensionUser.class, new SQLExtensionUser());
			this.storageService.getStorage(SQLStorage.class).registerProvider(IExtensionLang.class, new SQLExtensionLang());
		}
		if(this.storageService.isStorageRegisterd(LocaleStorage.class)) {
			this.storageService.getStorage(LocaleStorage.class).registerProvider(IExtensionGuild.class, new LocaleExtensionGuild());
			this.storageService.getStorage(LocaleStorage.class).registerProvider(IExtensionUser.class, new LocaleExtensionUser());
			this.storageService.getStorage(LocaleStorage.class).registerProvider(IExtensionLang.class, new LocaleExtensionLang());
		}

		this.commandManager = new de.ngloader.bot.command.CommandManager(this);
	}

	@Override
	protected void onEnable() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onDisable() {
	}

	public CommandManager<WuffyBot> getCommandManager() {
		return this.commandManager;
	}

	public BotConfig getConfig() {
		return this.config;
	}
}