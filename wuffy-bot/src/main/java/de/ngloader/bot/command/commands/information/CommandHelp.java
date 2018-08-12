package de.ngloader.bot.command.commands.information;

import java.util.stream.Collectors;

import de.ngloader.bot.command.CommandHandler;
import de.ngloader.bot.command.commands.Command;
import de.ngloader.bot.command.commands.CommandCategory;
import de.ngloader.bot.command.commands.CommandSettings;
import de.ngloader.bot.command.commands.MessageType;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.database.user.WuffyUser;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.util.StringUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;

@CommandSettings(
		category = CommandCategory.INFORMATION,
		memberPermissionList = { },
		aliases = { "help", "h" },
		privateChatCommand = true)
public class CommandHelp extends Command {

	public CommandHelp(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) {
		if(args.length > 0) {
			if(this.hasBotPermission(event, Permission.MESSAGE_EMBED_LINKS)) {
				Command target = this.handler.getRegestry().getCommand(args[0]);

				if(target != null) {
					if(target.getSettings().adminCommand() && !this.getHandler().getCore().isAdmin(event.getAuthor())) {
						this.sendMessage(event, MessageType.INFO, this.i18n.format(TranslationKeys.MESSAGE_HELP_COMMAND_NOT_FOUND, event.getMember(WuffyMember.class).getLocale(),
								"%c", args[0]));
						return;
					}

					this.queue(event, MessageType.HELP, event.getTextChannel().sendMessage(target.buildHelpMessage(event, command, args).build()));
				} else
					this.sendMessage(event, MessageType.INFO, this.i18n.format(TranslationKeys.MESSAGE_HELP_COMMAND_NOT_FOUND, event.getMember(WuffyMember.class).getLocale(),
							"%c", args[0]));
			}
			return;
		}
		this.sendMessage(event, MessageType.INFO, this.i18n.format(TranslationKeys.MESSAGE_HELP_LOOK_IN_YOUR_PRIVATE_MESSAGES, event.getMember(WuffyMember.class).getLocale()));

		this.sendHelpMessage(event, event.getMember(WuffyMember.class).getLocale());
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) {
		if(args.length > 0) {
			Command target = this.handler.getRegestry().getCommand(args[0]);

			if(target != null) {
				if(target.getSettings().adminCommand() && !this.getHandler().getCore().isAdmin(event.getAuthor())) {
					this.sendMessage(event, MessageType.INFO, this.i18n.format(TranslationKeys.MESSAGE_HELP_COMMAND_NOT_FOUND, event.getMember(WuffyMember.class).getLocale(),
							"%c", args[0]));
					return;
				}

				event.getPrivateChannel().sendMessage(target.buildHelpMessage(event, command, args).build()).queue();
			} else
				this.sendMessage(event, MessageType.INFO, this.i18n.format(TranslationKeys.MESSAGE_HELP_COMMAND_NOT_FOUND, event.getMember(WuffyMember.class).getLocale(),
						"%c", args[0]));
			return;
		}

		String locale = event.getAuthor(WuffyUser.class).getUserLocale();
		sendHelpMessage(event, locale != null ? locale : "en-US");
	}

	private void sendHelpMessage(WuffyMessageRecivedEvent event, String locale) {
		WuffyGuild guild = event.getChannelType() == ChannelType.TEXT ? event.getGuild(WuffyGuild.class) : null;
		String messageDisabled = i18n.format(TranslationKeys.MESSAGE_COMMANDS_DISABLED, locale);
		String messageAlpha = i18n.format(TranslationKeys.MESSAGE_COMMANDS_ALPHA, locale);

		EmbedBuilder builder = this.createEmbed(event, MessageType.LIST);
		String prefix = guild != null ? guild.getPrefixes().isEmpty() ? String.format("<@%s> ", event.getJDA().getSelfUser().getIdLong()) : guild.getPrefixes().get(0) : "~";
		boolean alphaTester = event.getAuthor(WuffyUser.class).isAlphaTester() || this.handler.getCore().isAdmin(event.getAuthor());

		for(CommandCategory category : CommandCategory.values()) {
			if(category == CommandCategory.BOT_AUTHOR && !event.getCore().isAdmin(event.getAuthor()))
				continue;

			builder.addField(i18n.format(String.format("command_category_%s", category.name().toLowerCase()), locale),
					this.handler.getRegestry().getAllCommands().stream()
					.filter(cmd -> cmd.getSettings().category() == category)
					.filter(cmd -> cmd.getSettings().alpha() ? alphaTester : true)
					.map(cmd -> String.format("%s%s%s%s",
							prefix,
							StringUtil.writeFirstUpperCase(cmd.getSettings().aliases()[0]),
							this.handler.getRegestry().isDisabled(cmd) ? String.format(" %s", messageDisabled) : "",
							cmd.getSettings().alpha() ? String.format(" %s", messageAlpha) : ""))
						.collect(Collectors.joining("\n")), true);
		}

		builder.setDescription(this.i18n.format(TranslationKeys.MESSAGE_HELP, locale,
				"%p", "~"/*prefix DISABLED (discord display numbers)*/)); //TODO check can be used.

		event.getAuthor().openPrivateChannel().queue(success -> success.sendMessage(builder.build()).queue());
	}
}