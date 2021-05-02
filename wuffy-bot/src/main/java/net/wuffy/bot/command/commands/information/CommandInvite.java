package net.wuffy.bot.command.commands.information;

import net.wuffy.bot.command.CommandHandler;
import net.wuffy.bot.command.commands.Command;
import net.wuffy.bot.command.commands.CommandCategory;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.command.commands.MessageType;
import net.wuffy.bot.database.guild.WuffyGuild;
import net.wuffy.bot.database.guild.WuffyMember;
import net.wuffy.bot.keys.TranslationKeys;
import net.wuffy.core.event.WuffyMessageRecivedEvent;
import net.wuffy.core.util.ArgumentBuffer;

@CommandSettings(
		category = CommandCategory.INFORMATION,
		memberPermissionList = { },
		aliases = { "invite", "inv" },
		privateChatCommand = true)
public class CommandInvite extends Command {

	public CommandInvite(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, ArgumentBuffer args) {
		if(event.getGuild(WuffyGuild.class).isMessageDeleteExecuter())
			event.getMessage().delete().queue();

		event.getAuthor().openPrivateChannel().queue(success ->
				success.sendMessage(
						this.createEmbed(event, MessageType.INFO).setDescription(this.i18n.format(TranslationKeys.MESSAGE_INVITE, event.getMember(WuffyMember.class).getLocale())).build())
				.queue());
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, ArgumentBuffer args) {
		event.getPrivateChannel().sendMessage(
				this.createEmbed(event, MessageType.INFO).setDescription(this.i18n.format(TranslationKeys.MESSAGE_INVITE, event.getMember(WuffyMember.class).getLocale())).build())
		.queue();
	}
}