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
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.util.StringUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;

@CommandSettings(
		category = CommandCategory.INFORMATION,
		guildPermissionRequierd = { Permission.MESSAGE_EMBED_LINKS },
		memberPermissionRequierd = { PermissionKeys.COMMAND_COMMANDS },
				memberPermissionList = { PermissionKeys.COMMAND_COMMANDS },
		aliases = { "commands", "cmds", "cmd" },
		privateChatCommand = true)
public class CommandCommands extends Command {

	public CommandCommands(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) {
		this.queue(event, MessageType.LIST, event.getTextChannel().sendMessage(this.createCommandList(event).build()));
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) {
		event.getPrivateChannel().sendMessage(this.createCommandList(event).build()).queue();
	}

	private EmbedBuilder createCommandList(WuffyMessageRecivedEvent event) {
		WuffyGuild guild = event.getChannelType() == ChannelType.TEXT ? event.getGuild(WuffyGuild.class) : null;
		String locale = guild != null ? event.getMember(WuffyMember.class).getLocale() : event.getAuthor(WuffyUser.class).getUserLocale("en-US");

		String messageDisabled = i18n.format(TranslationKeys.MESSAGE_COMMANDS_DISABLED, locale);
		String messageAlpha = i18n.format(TranslationKeys.MESSAGE_COMMANDS_ALPHA, locale);
		String prefix = guild != null ? guild.getPrefixes().isEmpty() ? String.format("<@%s> ", event.getJDA().getSelfUser().getIdLong()) : guild.getPrefixes().get(0) : "~";
		boolean alphaTester = event.getAuthor(WuffyUser.class).isAlphaTester() || this.handler.getCore().isAdmin(event.getAuthor());

		EmbedBuilder builder = this.createEmbed(event, MessageType.LIST);

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

		return builder;
	}
}