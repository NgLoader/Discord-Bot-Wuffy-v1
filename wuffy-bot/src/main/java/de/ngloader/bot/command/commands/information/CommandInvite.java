package de.ngloader.bot.command.commands.information;

import de.ngloader.bot.command.CommandHandler;
import de.ngloader.bot.command.commands.Command;
import de.ngloader.bot.command.commands.CommandCategory;
import de.ngloader.bot.command.commands.CommandSettings;
import de.ngloader.bot.command.commands.MessageType;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.core.event.WuffyMessageRecivedEvent;

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
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) {
		if(event.getGuild(WuffyGuild.class).isMessageDeleteExecuter())
			event.getMessage().delete().queue();

		event.getAuthor().openPrivateChannel().queue(success ->
				success.sendMessage(
						this.createEmbed(event, MessageType.INFO).setDescription(this.i18n.format(TranslationKeys.MESSAGE_INVITE, event.getMember(WuffyMember.class).getLocale())).build())
				.queue());
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) {
		event.getPrivateChannel().sendMessage(
				this.createEmbed(event, MessageType.INFO).setDescription(this.i18n.format(TranslationKeys.MESSAGE_INVITE, event.getMember(WuffyMember.class).getLocale())).build())
		.queue();
	}
}