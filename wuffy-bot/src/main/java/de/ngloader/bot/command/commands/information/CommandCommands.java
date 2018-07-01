package de.ngloader.bot.command.commands.information;

import java.util.stream.Collectors;

import de.ngloader.bot.WuffyBot;
import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import net.dv8tion.jda.core.EmbedBuilder;

@Command(aliases = { "commands", "cmds", "cmd" })
@CommandConfig(category = CommandCategory.INFORMATION)
public class CommandCommands extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		for(CommandCategory category : CommandCategory.values()) {
			embedBuilder.addField(writeFirstUpperCase(category.name()), ((WuffyBot) event.getCore()).getCommandManager().getRegistry().getCommands().values().stream()
					.distinct()
					.filter(command -> command.getClass().getAnnotation(CommandConfig.class).category() == category)
					.map(command -> writeFirstUpperCase(command.getClass().getAnnotation(Command.class).aliases()[0]) + (((BotCommand) command).isCommandBlocked() ? " *[DISABLED]*" : ""))
					.collect(Collectors.joining("\n")), true);
		}

		event.getChannel().sendMessage(embedBuilder.build()).queue();
	}

	public String writeFirstUpperCase(String message) {
		return message.substring(0, 1).toUpperCase() + message.substring(1).toLowerCase();
	}
}