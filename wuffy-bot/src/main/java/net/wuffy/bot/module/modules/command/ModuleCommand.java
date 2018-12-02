package net.wuffy.bot.module.modules.command;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;
import net.wuffy.bot.command.Command;
import net.wuffy.bot.command.CommandExecuter;
import net.wuffy.bot.command.CommandRegistry;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.database.DBGuild;
import net.wuffy.bot.database.DBMember;
import net.wuffy.bot.module.EnumModuleType;
import net.wuffy.bot.module.GuildModule;
import net.wuffy.bot.module.Module;
import net.wuffy.common.logger.Logger;
import net.wuffy.core.util.ArgumentBuffer;
import net.wuffy.core.util.EmoteList;

public class ModuleCommand extends Module {

	private CommandExecuter commandExecuter;

	private String mentionPrefix;

	public ModuleCommand(GuildModule guildModule) {
		super(EnumModuleType.COMMAND, guildModule);
	}

	@Override
	public void onEnable() {
		this.commandExecuter = this.getGuild().getCore().getCommandHandlerAdapter().apply(this.getGuild().getJDA().getShardInfo().getShardId());

		this.mentionPrefix = String.format("<@%s>", Long.toString(this.getGuild().getJDA().getSelfUser().getIdLong()));

		this.registerListener(false);
	}

	@Override
	public void onDisable() {
		this.unregisterListener(false);

		this.commandExecuter = null;
		this.mentionPrefix = null;
	}

	@Override
	protected void onDestroy() {
	}

	@SubscribeEvent
	public void onMessageRecived(GuildMessageReceivedEvent event) {
		if(event.getAuthor().isBot() || event.getAuthor().isFake())
			return;

		DBGuild guild = this.getGuild();
		List<String> prefixes = guild.getPrefixes();

		if(System.getProperty("developerMode").equals("true"))
			if(!guild.getCore().getConfig().admins.contains(event.getAuthor().getIdLong()))
				return;
			else
				prefixes.clear();

		if(guild.isMention() || event.getMember().isOwner() || guild.getCore().getConfig().admins.contains(event.getAuthor().getIdLong())) {
			prefixes = new ArrayList<String>(prefixes);
			prefixes.add(this.mentionPrefix);
		}

		ArgumentBuffer argumentBuffer = new ArgumentBuffer(event.getMessage().getContentRaw());
		String firstArgument = argumentBuffer.removeArgument("").toLowerCase();

		for(String prefix : prefixes) {
			if(firstArgument.startsWith(prefix)) {
				firstArgument = firstArgument.substring(prefix.length());

				if(firstArgument.isEmpty() && argumentBuffer.getSize() > 0)
					firstArgument = argumentBuffer.removeArgument("");
	
				if(firstArgument.isEmpty())
					return;
	
				String alias = firstArgument;
	
				this.commandExecuter.add(new Runnable() {
					
					@Override
					public void run() {
						try {
							Command command = CommandRegistry.getCommand(alias);
	
							if(command != null) {
								CommandSettings settings = command.getSettings();

								if((settings.hidden() || settings.admin()) && !guild.getCore().getConfig().admins.contains(event.getAuthor().getIdLong())) {
									ModuleCommand.this.noPermission(guild, event.getMessage());
									return;
								} else if(settings.alpha() && !guild.isPartner() && !ModuleCommand.this.getUser(event.getAuthor()).isAlphaTester()) {
									ModuleCommand.this.noPermission(guild, event.getMessage());
									return;
								} else if(settings.partner() && !guild.isPartner()) {
									ModuleCommand.this.noPermission(guild, event.getMessage());
									return;
								}

								DBMember member = ModuleCommand.this.getMember(event.getMember());

								if(guild.hasPermission(event.getChannel(), member, settings.memberPermissionRequierd()))
									command.onGuild(event, guild, member, alias, argumentBuffer);
								else
									ModuleCommand.this.noPermission(guild, event.getMessage());
							} else {
								if(guild.getSelfMember().hasPermission(event.getChannel(), Permission.MESSAGE_ADD_REACTION))
									event.getMessage().addReaction(EmoteList.GREY_QUESTION).queue();
	
								if(guild.isDeleteExecuterMessage() && guild.getSelfMember().hasPermission(event.getChannel(), Permission.MESSAGE_MANAGE))
									event.getMessage().delete().queueAfter(2, TimeUnit.SECONDS);
							}
						} catch(Exception e) {
							Logger.fatal(
								"ModuleCommand",
								String.format("Error by executing command \"%s\" in guild \"%s\". Command: \"%s\"",
									alias,
									Long.toString(guild.getIdLong()),
									event.getMessage().getContentRaw()),
								e);
						}
					}
				});
			}
		}
	}

	public void noPermission(DBGuild guild, Message message) {
		//TODO send no permission message
		if(guild.getSelfMember().hasPermission(Permission.MESSAGE_ADD_REACTION))
			message.addReaction(EmoteList.EXCLAMATION).queue();
	}
}