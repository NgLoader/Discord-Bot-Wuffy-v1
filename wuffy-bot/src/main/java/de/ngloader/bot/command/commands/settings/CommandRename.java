package de.ngloader.bot.command.commands.settings;

import java.util.Arrays;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.bot.util.ReplayBuilder;
import de.ngloader.core.command.Command;
import de.ngloader.core.command.MessageType;
import de.ngloader.core.database.impl.ImplMember;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.lang.I18n;
import de.ngloader.core.util.DiscordUtil;

@Command(aliases = { "rename", "rname" })
@CommandConfig(category = CommandCategory.SETTINGS)
public class CommandRename extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		WuffyMember member = event.getMember(WuffyMember.class);
		WuffyGuild guild = event.getGuild(WuffyGuild.class);
		I18n i18n = event.getCore().getI18n();

		if(member.hasPermission(event.getTextChannel(), PermissionKeys.COMMAND_RENAME)) {
			if(args.length > 0) {
				ImplMember target = DiscordUtil.searchMember(event.getCore(), guild, args[0]);

				if(target != null) {
					if(guild.getSelfMember().canInteract(target)) {
						if(args.length > 1) {
							String name = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

							if(name.length() > 0 && name.length() < 33) {
								String oldName = member.getEffectiveName();

								guild.getController().setNickname(target.getRealMember(), name).queue();

								new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_RENAME, member.getLocale(),
										"%on", oldName,
										"%nn", name,
										"%rn", target.getUser().getName()))
								.queue();
							} else
								new ReplayBuilder(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_RENAME_SPEICAL_CHARACTER, member.getLocale(),
										"%n", name))
								.queue();
						} else {
							guild.getController().setNickname(target.getRealMember(), "").queue();

							new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_RENAME, member.getLocale(),
									"%on", target.getEffectiveName(),
									"%nn", target.getUser().getName(),
									"%rn", target.getUser().getName()))
							.queue();
						}
					} else
						this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_LOWER_ROLE, member.getLocale(), "%m", target.getEffectiveName()));
				} else
					new ReplayBuilder(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_RENAME_NOT_FOUND, member.getLocale(),
							"%t", args[0]))
					.queue();
			} else
				new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_RENAME_SYNTAX, member.getLocale())).queue();
		} else
			new ReplayBuilder(event, MessageType.PERMISSION, i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, member.getLocale(), "%p", PermissionKeys.COMMAND_RENAME.key)).queue();
	}
}