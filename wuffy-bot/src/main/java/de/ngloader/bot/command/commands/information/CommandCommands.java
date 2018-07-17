package de.ngloader.bot.command.commands.information;

import java.util.stream.Collectors;

import de.ngloader.bot.WuffyBot;
import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.lang.TranslationKeys;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.lang.I18n;
import de.ngloader.core.util.StringUtil;
import net.dv8tion.jda.core.EmbedBuilder;

@Command(aliases = { "commands", "cmds", "cmd" })
@CommandConfig(category = CommandCategory.INFORMATION)
public class CommandCommands extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		WuffyGuild guild = event.getGuild(WuffyGuild.class);
		I18n i18n = event.getCore().getI18n();
		String locale = event.getMember(WuffyMember.class).getLocale();

		if(guild.hasPermission(event.getTextChannel(), event.getMember(), "command.commands")) {

			var messageDisabled = i18n.format(TranslationKeys.MESSAGE_COMMANDS_DISABLED, locale);
			var prefix = guild.getPrefixes().get(0);

			EmbedBuilder embedBuilder = new EmbedBuilder();

			for(CommandCategory category : CommandCategory.values()) {
				embedBuilder.addField(i18n.format(String.format("command_category_%s", category.name().toLowerCase()), locale),
						((WuffyBot) event.getCore()).getCommandManager().getRegistry().getCommands().values().stream()
							.distinct()
							.filter(command -> command.getClass().getAnnotation(CommandConfig.class).category() == category)
							.map(command -> {
								Command annotation = command.getClass().getAnnotation(Command.class);

								return prefix +
										StringUtil.writeFirstUpperCase(annotation.aliases()[0]) +
										((((BotCommand) command).isCommandBlocked() || guild.getDisabledCommands().contains(annotation.aliases()[0])) ? String.format(" %s", messageDisabled) : "");
							})
							.collect(Collectors.joining("\n")), true);
			}

			event.getChannel().sendMessage(embedBuilder.build()).queue();
		} else
			this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale, "%p", "command.commands"));
	}
}