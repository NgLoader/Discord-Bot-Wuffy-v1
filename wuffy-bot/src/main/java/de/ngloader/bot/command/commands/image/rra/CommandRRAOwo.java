package de.ngloader.bot.command.commands.image.rra;

import java.time.Instant;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.bot.util.ReplayBuilder;
import de.ngloader.core.command.Command;
import de.ngloader.core.command.MessageType;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.lang.I18n;
import net.dv8tion.jda.core.entities.Message;

@Command(aliases = { "owo" })
@CommandConfig(category = CommandCategory.IMAGE)
public class CommandRRAOwo extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		I18n i18n = event.getCore().getI18n();
		String locale = event.getMember(WuffyMember.class).getLocale();

		if(event.getMember(WuffyMember.class).hasPermission(event.getTextChannel(), PermissionKeys.COMMAND_OWO)) {
			Message message = event.getTextChannel().sendMessage(new ReplayBuilder(event, MessageType.PICTURE, false)
					.setupDefault(false, false)
					.setDescription(i18n.format(TranslationKeys.MESSAGE_IMAGE_SEARCHING, locale))
					.getEmbedBuilder()
					.build())
				.complete();

			String picture = RRABuilder.getRandom(EnumRRATypes.OWO);

			if(picture != null)
				ReplayBuilder.queue(event, MessageType.PICTURE, message.editMessage(new ReplayBuilder(event, MessageType.PICTURE, false)
						.setupDefault(false, false)
						.setImage(picture)
						.setTimestamp(Instant.now())
					.getEmbedBuilder()
					.build()));
			else
				ReplayBuilder.queue(event, MessageType.PICTURE, message.editMessage(new ReplayBuilder(event, MessageType.PICTURE, false)
						.setupDefault(false, false)
						.setDescription(i18n.format(TranslationKeys.MESSAGE_IMAGE_NOTHING_FOUND, locale))
						.setTimestamp(Instant.now())
					.getEmbedBuilder()
					.build()));
		} else
			this.replay(event, MessageType.PERMISSION, i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale,
					"%p", PermissionKeys.COMMAND_OWO.key));
	}
}