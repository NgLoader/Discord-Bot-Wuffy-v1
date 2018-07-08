package de.ngloader.bot.command.commands.information;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.lang.TranslationKeys;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.util.HardwareUtil;
import net.dv8tion.jda.core.EmbedBuilder;

@Command(aliases = { "status" })
@CommandConfig(category = CommandCategory.INFORMATION)
public class CommandStatus extends BotCommand {

	private static final Double MEGABYTE = 1000000D;

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		var locale = event.getGuild(WuffyGuild.class).getLocale();
		var i18n = event.getCore().getI18n();

		var embedBuilder = new EmbedBuilder()
				.addField(i18n.format(TranslationKeys.MESSAGE_STATUS_PROCESSORS, locale), "**" + Integer.toString(HardwareUtil.getAvailableProcessors()) + "**", true)
				.addField(i18n.format(TranslationKeys.MESSAGE_STATUS_CPU, locale), "**" + Integer.toString(((Double) (HardwareUtil.getProcessCpuLoad() * 100)).intValue()) + "%**", true)
				.addField(i18n.format(TranslationKeys.MESSAGE_STATUS_MEMORY, locale), "``" + toMegabyte(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) + "MB``/**" + toMegabyte(Runtime.getRuntime().totalMemory()) + "MB**", true);
		event.getChannel().sendMessage(embedBuilder.build()).queue();
	}

	private Integer toMegabyte(double bytes) {
		return ((Double) (bytes / MEGABYTE)).intValue();
	}
}