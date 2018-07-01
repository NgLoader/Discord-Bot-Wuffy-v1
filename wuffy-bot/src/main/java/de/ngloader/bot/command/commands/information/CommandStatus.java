package de.ngloader.bot.command.commands.information;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
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
		var embedBuilder = new EmbedBuilder()
				.addField("Processors", "**" + Integer.toString(HardwareUtil.getAvailableProcessors()) + "**", true)
				.addField("CPU", "**" + Integer.toString(((Double) (HardwareUtil.getProcessCpuLoad() * 100)).intValue()) + "%**", true)
				.addField("Memory", "``" + toMegabyte(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) + "MB``/**" + toMegabyte(Runtime.getRuntime().totalMemory()) + "MB**", true);
		event.getChannel().sendMessage(embedBuilder.build()).queue();
	}

	private Integer toMegabyte(double bytes) {
		return ((Double) (bytes / MEGABYTE)).intValue();
	}
}