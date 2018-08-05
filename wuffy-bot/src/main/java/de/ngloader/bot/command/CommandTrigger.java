package de.ngloader.bot.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.ngloader.bot.WuffyBot;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.core.command.CommandManager;
import de.ngloader.core.database.impl.IExtensionGuild;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.logger.Logger;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandTrigger extends de.ngloader.core.command.CommandTrigger<WuffyBot> {

	public CommandTrigger(CommandManager<WuffyBot> manager) {
		super(manager);
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		if(event.getAuthor().isBot() || event.getAuthor().isFake() || !event.getChannel().canTalk() || event.getChannel().getType() != ChannelType.TEXT)
			return;

		var message = event.getMessage().getContentRaw();
		var split = message.split("\\s+");
		var args = Arrays.copyOfRange(split, 1, split.length);
		var command = split[0].toLowerCase();

		WuffyGuild guild = (WuffyGuild) this.manager.getCore().getStorageService().getExtension(IExtensionGuild.class).getGuild(event.getGuild());

		if(System.getProperty("developerMode") != null)
			if(!this.manager.getCore().isAdmin(event.getAuthor()))
				return;

		List<String> prefixes = new ArrayList<String>(guild.getPrefixes());

		if(guild.isMention() || event.getMember().isOwner() || this.manager.getCore().isAdmin(event.getAuthor().getIdLong()))
			prefixes.add(String.format("<@%s>", Long.toString(event.getGuild().getSelfMember().getUser().getIdLong())));

		for(String prefix : prefixes)
			if(command.startsWith(prefix)) {
				command = command.substring(prefix.length());

				if(command.isEmpty() && args.length > 0) {
					command = args[0].toLowerCase();
					args = Arrays.copyOfRange(args, 1, args.length);
				}

				Logger.debug("Command Trigger", String.format("Command: '%s' args: '%s'", command, String.join(", ", args)));

				onTrigger(new WuffyMessageRecivedEvent(this.manager.getCore(), event.getJDA(), event.getResponseNumber(), event.getMessage()), command, args);
				return;
			}
	}

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		WuffyGuild guild = (WuffyGuild) this.manager.getCore().getStorageService().getExtension(IExtensionGuild.class).getGuild(event.getGuild());

		if(!guild.getAutoRole().isEmpty()) {
			List<Role> roles = new ArrayList<Role>();

			for(String string : guild.getAutoRole()) {
				Role found = guild.getRoleById(string);

				if(found != null)
					roles.add(found);
				else
					guild.removeAutoRole(string);
			}

			guild.getController().addRolesToMember(event.getMember(), roles).queue();
		}
	}
}