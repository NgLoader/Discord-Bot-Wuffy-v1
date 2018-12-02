package net.wuffy.bot.commandOLD.commands.settings;

import java.util.Arrays;

import net.dv8tion.jda.core.Permission;
import net.wuffy.bot.command.Command;
import net.wuffy.bot.command.CommandHandler;
import net.wuffy.bot.command.commands.CommandCategory;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.command.commands.MessageType;
import net.wuffy.bot.database.guild.WuffyGuild;
import net.wuffy.bot.database.guild.WuffyMember;
import net.wuffy.bot.keys.PermissionKeys;
import net.wuffy.bot.keys.TranslationKeys;
import net.wuffy.core.database.impl.ImplMember;
import net.wuffy.core.event.WuffyMessageRecivedEvent;
import net.wuffy.core.util.DiscordUtil;

@CommandSettings(
		category = CommandCategory.SETTINGS,
		guildPermissionRequierd = { Permission.NICKNAME_CHANGE, Permission.NICKNAME_MANAGE },
		memberPermissionList = { PermissionKeys.COMMAND_RENAME },
		memberPermissionRequierd = { PermissionKeys.COMMAND_RENAME },
		aliases = { "rename" })
public class CommandRename extends Command {

	public CommandRename(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) {
		WuffyMember member = event.getMember(WuffyMember.class);
		WuffyGuild guild = event.getGuild(WuffyGuild.class);

		if(args.length > 0) {
			ImplMember target = DiscordUtil.searchMember(event.getCore(), guild, args[0]);

			if(target != null) {
				if(guild.getSelfMember().canInteract(target)) {
					if(args.length > 1) {
						String name = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

						if(name.length() > 0 && name.length() < 33) {
							String oldName = member.getEffectiveName();

							guild.getController().setNickname(target.getRealMember(), name).queue();

							this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_RENAME, member.getLocale(),
									"%on", oldName,
									"%nn", name,
									"%rn", target.getUser().getName()));
						} else
							this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_RENAME_SPEICAL_CHARACTER, member.getLocale(),
									"%n", name));
					} else {
						guild.getController().setNickname(target.getRealMember(), "").queue();

						this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_RENAME, member.getLocale(),
								"%on", target.getEffectiveName(),
								"%nn", target.getUser().getName(),
								"%rn", target.getUser().getName()));
					}
				} else
					this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_LOWER_ROLE, member.getLocale(), "%m", target.getEffectiveName()));
			} else
				this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_RENAME_NOT_FOUND, member.getLocale(),
						"%t", args[0]));
		} else
			this.sendHelpMessage(event, command, args);
		
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) { }
}