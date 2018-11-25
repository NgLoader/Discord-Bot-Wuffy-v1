package net.wuffy.bot.command.commands.information;

import net.dv8tion.jda.core.Permission;
import net.wuffy.bot.command.CommandHandler;
import net.wuffy.bot.command.commands.Command;
import net.wuffy.bot.command.commands.CommandCategory;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.command.commands.MessageType;
import net.wuffy.bot.database.guild.WuffyMember;
import net.wuffy.bot.keys.PermissionKeys;
import net.wuffy.bot.keys.TranslationKeys;
import net.wuffy.bot.lang.I18n;
import net.wuffy.common.util.HardwareUtil;
import net.wuffy.core.event.WuffyMessageRecivedEvent;

@CommandSettings(
		category = CommandCategory.INFORMATION,
		guildPermissionRequierd = { Permission.MESSAGE_EMBED_LINKS },
		memberPermissionList = { PermissionKeys.COMMAND_STATUS },
		memberPermissionRequierd = { PermissionKeys.COMMAND_STATUS},
		aliases = { "status" })
public class CommandStatus extends Command {

	private static final Double MEGABYTE = 1000000D;

	public CommandStatus(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) {
		WuffyMember member = event.getMember(WuffyMember.class);
		I18n i18n = event.getCore().getI18n();
		String locale = member.getLocale();

		this.queue(event, MessageType.INFO, event.getTextChannel().sendMessage(this.createEmbed(event, MessageType.INFO)
				.addField(i18n.format(TranslationKeys.MESSAGE_STATUS_PROCESSORS, locale), "**" + Integer.toString(HardwareUtil.getAvailableProcessors()) + "**", true)
				.addField(i18n.format(TranslationKeys.MESSAGE_STATUS_CPU, locale), "**" + Integer.toString(((Double) (HardwareUtil.getProcessCpuLoad() * 100)).intValue()) + "%**", true)
				.addField(i18n.format(TranslationKeys.MESSAGE_STATUS_MEMORY, locale),
						"``" + toMegabyte(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())
								+ "MB``/**" + toMegabyte(Runtime.getRuntime().totalMemory()) + "MB**",
						true).build()));
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) { }

	private Integer toMegabyte(double bytes) {
		return ((Double) (bytes / MEGABYTE)).intValue();
	}
}