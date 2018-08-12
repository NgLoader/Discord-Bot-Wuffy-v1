package de.ngloader.bot.command.commands.fun;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import de.ngloader.bot.command.CommandHandler;
import de.ngloader.bot.command.commands.Command;
import de.ngloader.bot.command.commands.CommandCategory;
import de.ngloader.bot.command.commands.CommandSettings;
import de.ngloader.bot.command.commands.MessageType;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.core.event.WuffyMessageRecivedEvent;

@CommandSettings(
		category = CommandCategory.FUN,
		memberPermissionList = { PermissionKeys.COMMAND_TABLEFLIP },
		memberPermissionRequierd = { PermissionKeys.COMMAND_TABLEFLIP },
		aliases = { "tableflip", "tf" },
		privateChatCommand = true)
public class CommandTableflip extends Command {

	private static final String URL = "https://wuffy.eu/pictures/tableflip/";
	private static final List<String> FILES = new ArrayList<String>();
	private static final Random RANDOM = new Random();

	static {
		try {
			Document document = Jsoup.connect(CommandTableflip.URL).get();

			for(Element file : document.select("a[href]")) {
				String name = file.attr("href");

				if(name.endsWith(".gif") || name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".mp4"))
					CommandTableflip.FILES.add(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CommandTableflip(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) {
		this.queue(event, MessageType.INFO, event.getTextChannel().sendMessage(this.createEmbed(event, MessageType.PICTURE)
				.setTimestamp(Instant.now())
				.setImage(String.format("%s%s", CommandTableflip.URL, CommandTableflip.FILES.get(CommandTableflip.RANDOM.nextInt(CommandTableflip.FILES.size())))).build()));
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) {
		this.queue(event, MessageType.INFO, event.getPrivateChannel().sendMessage(this.createEmbed(event, MessageType.PICTURE)
				.setTimestamp(Instant.now())
				.setImage(String.format("%s%s", CommandTableflip.URL, CommandTableflip.FILES.get(CommandTableflip.RANDOM.nextInt(CommandTableflip.FILES.size())))).build()));
	}
}