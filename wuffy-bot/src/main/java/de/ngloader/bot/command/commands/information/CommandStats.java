package de.ngloader.bot.command.commands.information;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.jda.JDAAdapter;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import net.dv8tion.jda.core.EmbedBuilder;

@Command(aliases = { "stats" })
@CommandConfig(category = CommandCategory.INFORMATION)
public class CommandStats extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		var embedBuilder = new EmbedBuilder()
				.addField("Version", event.getCore().getConfig().instanceVersion, true)
				.addField("Library", "JDA", true)
				.addField("Owner", "Nils#0001", true)
				.addField("Guilds", Integer.toString(event.getCore().getJdaAdapter(JDAAdapter.class).getShardManager().getGuilds().size()), true)
				.addField("Users", Integer.toString(event.getCore().getJdaAdapter(JDAAdapter.class).getShardManager().getUsers().size()), true)
				.addField("Voice Streams", "Unkown", true);

		event.getChannel().sendMessage(embedBuilder.build()).queue();
	}
}