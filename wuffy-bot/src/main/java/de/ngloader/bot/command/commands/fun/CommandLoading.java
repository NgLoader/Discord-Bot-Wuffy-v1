package de.ngloader.bot.command.commands.fun;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.core.command.Command;
import de.ngloader.core.command.MessageType;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.lang.I18n;
import net.dv8tion.jda.core.EmbedBuilder;

@Command(aliases = { "loading", "load" })
@CommandConfig(category = CommandCategory.FUN)
public class CommandLoading extends BotCommand {

	private static final String URL = "https://wuffy.eu/pictures/loading/";
	private static final List<String> FILES = new ArrayList<String>();
	private static final Random RANDOM = new Random();

	static {
		try {
			Document document = Jsoup.connect(CommandLoading.URL).get();

			for(Element file : document.select("a[href]")) {
				String name = file.attr("href");

				if(name.endsWith(".gif") || name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".mp4"))
					CommandLoading.FILES.add(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		I18n i18n = event.getCore().getI18n();
		String locale = event.getMember(WuffyMember.class).getLocale();

		if(event.getMember(WuffyMember.class).hasPermission(event.getTextChannel(), PermissionKeys.COMMAND_LOADING)) {
			this.replay(event, MessageType.INFO, new EmbedBuilder()
					.setTimestamp(Instant.now())
					.setImage(String.format("%s%s", CommandLoading.URL, CommandLoading.FILES.get(CommandLoading.RANDOM.nextInt(CommandLoading.FILES.size())))));
		} else
			this.replay(event, MessageType.PERMISSION, i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale,
					"%p", PermissionKeys.COMMAND_LOADING.key));
	}
}