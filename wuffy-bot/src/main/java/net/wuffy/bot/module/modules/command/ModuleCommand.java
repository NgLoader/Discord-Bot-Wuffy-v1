package net.wuffy.bot.module.modules.command;

import net.wuffy.bot.module.EnumModuleType;
import net.wuffy.bot.module.GuildModule;
import net.wuffy.bot.module.Module;
import net.wuffy.core.event.EventGuild;

public class ModuleCommand extends Module {

	public ModuleCommand(GuildModule<EventGuild> guildModule) {
		super(EnumModuleType.COMMAND, guildModule);
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}
}