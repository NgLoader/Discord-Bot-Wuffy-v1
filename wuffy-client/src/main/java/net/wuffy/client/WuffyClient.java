package net.wuffy.client;

import net.dv8tion.jda.core.AccountType;
import net.wuffy.client.jda.JDAAdapter;
import net.wuffy.core.Core;

public class WuffyClient extends Core {

	static {
		//TODO Add commands
	}

	public WuffyClient(ClientConfig config) {
		super(config, AccountType.CLIENT, JDAAdapter.class);

//		if(this.storageService.isStorageRegisterd(MongoStorage.class)) {
//			this.storageService.getStorage(MongoStorage.class).registerProvider(IExtensionGuild.class, new MongoExtensionGuild());
//			this.storageService.getStorage(MongoStorage.class).registerProvider(IExtensionUser.class, new MongoExtensionUser());
//			this.storageService.getStorage(MongoStorage.class).registerProvider(IExtensionLang.class, new MongoExtensionLang());
//		}
//		if(this.storageService.isStorageRegisterd(SQLStorage.class)) {
//			this.storageService.getStorage(SQLStorage.class).registerProvider(IExtensionGuild.class, new SQLExtensionGuild());
//			this.storageService.getStorage(SQLStorage.class).registerProvider(IExtensionUser.class, new SQLExtensionUser());
//			this.storageService.getStorage(SQLStorage.class).registerProvider(IExtensionLang.class, new SQLExtensionLang());
//		}
//		if(this.storageService.isStorageRegisterd(LocaleStorage.class)) {
//			this.storageService.getStorage(LocaleStorage.class).registerProvider(IExtensionGuild.class, new LocaleExtensionGuild());
//			this.storageService.getStorage(LocaleStorage.class).registerProvider(IExtensionUser.class, new LocaleExtensionUser());
//			this.storageService.getStorage(LocaleStorage.class).registerProvider(IExtensionLang.class, new LocaleExtensionLang());
//		}
	}

	@Override
	protected void onEnable() {	
	}

	@Override
	protected void onDisable() {
	}
}