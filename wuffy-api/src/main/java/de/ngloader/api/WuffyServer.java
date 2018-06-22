package de.ngloader.api;

import de.ngloader.api.command.ICommandManager;
import de.ngloader.api.config.IConfigService;
import de.ngloader.api.database.IStorageService;
import de.ngloader.api.database.impl.guild.IWuffyGuild;
import de.ngloader.api.database.impl.user.IWuffyUser;
import de.ngloader.api.logger.ILogger;
import de.ngloader.api.util.TickingTask;
import net.dv8tion.jda.core.JDA;

public abstract class WuffyServer extends TickingTask {

	public static final Long START_TIME_IN_MILLIS = System.currentTimeMillis();

	private static WuffyServer instance;

	public static void setInstance(WuffyServer instance) {
		if (WuffyServer.instance != null)
			throw new NullPointerException("Instance alredy set");
		WuffyServer.instance = instance;
	}

	public static WuffyServer getInstance() {
		return WuffyServer.instance;
	}

	public static ILogger getLogger() {
		return WuffyServer.instance.getLogger0();
	}

	public static IStorageService getStorageService() {
		return WuffyServer.instance.getStorageService0();
	}

	public static IJDAProvider getJDAProvider() {
		return WuffyServer.instance.getJDAProvider0();
	}

	public static ICommandManager getCommandManager() {
		return WuffyServer.instance.getCommandManager0();
	}

	public static IConfigService getConfigService() {
		return WuffyServer.instance.getConfigService0();
	}

	public static IWuffyUser getUser(JDA jda, Long longId) {
		return WuffyServer.instance.getUser0(jda, longId);
	}

	public static IWuffyGuild getGuild(JDA jda, Long longId) {
		return WuffyServer.instance.getGuild0(jda, longId);
	}

	protected abstract ILogger getLogger0();

	protected abstract IStorageService getStorageService0();

	protected abstract IJDAProvider getJDAProvider0();

	protected abstract ICommandManager getCommandManager0();

	protected abstract IConfigService getConfigService0();

	protected abstract IWuffyUser getUser0(JDA jda, Long longId);

	protected abstract IWuffyGuild getGuild0(JDA jda, Long longId);
}