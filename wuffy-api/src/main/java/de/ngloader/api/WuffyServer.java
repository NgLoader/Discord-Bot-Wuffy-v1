package de.ngloader.api;

import de.ngloader.api.command.ICommandManager;
import de.ngloader.api.config.IConfigService;
import de.ngloader.api.database.IStorageService;
import de.ngloader.api.database.impl.guild.IWuffyGuild;
import de.ngloader.api.database.impl.user.IWuffyUser;
import de.ngloader.api.logger.ILogger;
import de.ngloader.api.logger.ILoggerManager;
import de.ngloader.api.util.TickingTask;

public abstract class WuffyServer extends TickingTask {

	private static WuffyServer instance;

	public static void setInstance(WuffyServer instance) {
		if (instance != null)
			throw new NullPointerException("Instance alredy set");
		WuffyServer.instance = instance;
	}

	public static WuffyServer getInstance() {
		return WuffyServer.instance;
	}

	public static ILogger getLogger() {
		return WuffyServer.instance.getLogger0();
	}

	public static ILoggerManager getLoggerManager() {
		return WuffyServer.instance.getLoggerManager0();
	}

	public static IStorageService getStorageService() {
		return WuffyServer.instance.getStorageService0();
	}

	public static IShardProvider getShardProvider() {
		return WuffyServer.instance.getShardProvider0();
	}

	public static ICommandManager getCommandManager() {
		return WuffyServer.instance.getCommandManager0();
	}

	public static IConfigService getConfigLoader() {
		return WuffyServer.instance.getConfigService0();
	}

	public static IWuffyUser getUser(Long longId) {
		return WuffyServer.instance.getUser0(longId);
	}

	public static IWuffyGuild getGuild(Long longId) {
		return WuffyServer.instance.getGuild0(longId);
	}

	protected abstract ILogger getLogger0();

	protected abstract ILoggerManager getLoggerManager0();

	protected abstract IStorageService getStorageService0();

	protected abstract IShardProvider getShardProvider0();

	protected abstract ICommandManager getCommandManager0();

	protected abstract IConfigService getConfigService0();

	protected abstract IWuffyUser getUser0(Long longId);

	protected abstract IWuffyGuild getGuild0(Long longId);
}