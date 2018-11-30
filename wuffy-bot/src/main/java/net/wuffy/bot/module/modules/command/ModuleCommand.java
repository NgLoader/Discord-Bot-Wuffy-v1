package net.wuffy.bot.module.modules.command;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;
import net.wuffy.bot.module.EnumModuleType;
import net.wuffy.bot.module.GuildModule;
import net.wuffy.bot.module.Module;

public class ModuleCommand extends Module {

	public ModuleCommand(GuildModule guildModule) {
		super(EnumModuleType.COMMAND, guildModule);
	}

	@Override
	public void onEnable() {
		this.registerListener(false);
	}

	@Override
	public void onDisable() {
	}

	@Override
	protected void onDestroy() {
	}

	@SubscribeEvent
	public void onMessageRecived(GuildMessageReceivedEvent event) {
		event.getChannel().sendMessage("Test (Is alphaTester): " + this.getGuild().getTest()).queue();
	}
}