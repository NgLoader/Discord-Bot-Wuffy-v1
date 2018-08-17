package de.ngloader.bot.command.commands;

import java.awt.Color;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import de.ngloader.bot.command.CommandHandler;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.database.user.WuffyUser;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.lang.I18n;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

public abstract class Command {

	protected static final String EMOTE_ERROR = "<a:error:473423519720538112>";
	protected static final String EMOTE_LOADING = "<a:loading:468438447573696522>";
	protected static final String EMOTE_SUCCESS = "<a:checkmark:459068723408535552>";

	public abstract void onGuild(WuffyMessageRecivedEvent event, String command, String[] args);
	public abstract void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args);

	protected final CommandHandler handler;

	protected CommandSettings settings;
	protected I18n i18n;

	public Command(CommandHandler handler) {
		this.handler = handler;
		this.i18n = this.handler.getCore().getI18n();

		this.settings = this.getClass().getAnnotation(CommandSettings.class);
	}

	public void queue(WuffyMessageRecivedEvent event, MessageType type, MessageAction message) {
		if(event.getChannelType() == ChannelType.TEXT) {
			WuffyGuild guild = event.getGuild(WuffyGuild.class);

			if(guild.isMessageDeleteExecuter())
				this.deleteMessage(event.getMessage());

			if(guild.isMessageDeleteBot() && type != MessageType.LOADING && guild.isMessageDeleteDelay(type) && guild.getSelfMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_MANAGE))
				message.queue(success -> this.deleteMessage(success, guild.getMessageDeleteDelay(type)));
			else
				message.queue();
		} else
			message.queue();
	}

	public EmbedBuilder createEmbed(WuffyMessageRecivedEvent event, MessageType type) {
		WuffyGuild guild = event.getChannelType() == ChannelType.TEXT ? event.getGuild(WuffyGuild.class) : null;
		EmbedBuilder embedBuilder = new EmbedBuilder();

		switch(type) {
		case SUCCESS:
			embedBuilder.setDescription(Command.EMOTE_SUCCESS).appendDescription(" ");
			break;

		case LOADING:
			embedBuilder.setDescription(Command.EMOTE_LOADING).appendDescription(" ");
			break;

		case ERROR:
			embedBuilder.setDescription(Command.EMOTE_ERROR).appendDescription(" ");
			break;

		default:
			break;
		}

		embedBuilder.setTimestamp(Instant.now());

		if(event.getChannelType() == ChannelType.TEXT)
			embedBuilder.setFooter(String.format("%s#%s", event.getMember().getEffectiveName(), event.getAuthor().getDiscriminator()), event.getAuthor().getEffectiveAvatarUrl());
		else
			embedBuilder.setFooter(String.format("%s#%s", event.getAuthor().getName(), event.getAuthor().getDiscriminator()), event.getAuthor().getEffectiveAvatarUrl());

		if(guild != null) {
			String colorCode = guild.getMessageColorCode(type);
			embedBuilder.setColor(colorCode != null ? Color.decode(colorCode) : type.color);
		} else
			embedBuilder.setColor(type.color);

		return embedBuilder;
	}

	public void sendMessage(WuffyMessageRecivedEvent event, MessageType type, String message) {
		this.sendMessage(event, type, message, true);
	}

	public void sendMessage(WuffyMessageRecivedEvent event, MessageType type, String message, boolean embed) {
		if(event.getChannelType() == ChannelType.TEXT && event.getTextChannel().canTalk()) {
			if(embed && event.getGuild().getSelfMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_EMBED_LINKS)) {
				this.queue(event, type, event.getTextChannel().sendMessage(this.createEmbed(event, type).appendDescription(message).build()));
			} else
				this.queue(event, type, event.getTextChannel().sendMessage(message));
		} else
			event.getAuthor().openPrivateChannel().queue(success -> success.sendMessage(this.createEmbed(event, type).appendDescription(message).build()).queue());
	}

	public void sendMessage(WuffyMessageRecivedEvent event, MessageType type, EmbedBuilder message) {
		this.sendMessage(event, type, message.build());
	}

	public void sendMessage(WuffyMessageRecivedEvent event, MessageType type, MessageEmbed message) {
		if(event.getChannelType() == ChannelType.TEXT && event.getTextChannel().canTalk()) {
			if(event.getGuild().getSelfMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_EMBED_LINKS)) {
				this.queue(event, type, event.getTextChannel().sendMessage(message));
			} else
				this.queue(event, type, event.getTextChannel().sendMessage(message.getDescription()));
		} else
			event.getAuthor().openPrivateChannel().queue(success -> success.sendMessage(message).queue());
	}

	public void deleteMessage(Message message) {
		if(message.getGuild().getSelfMember().hasPermission(message.getTextChannel(), Permission.MESSAGE_MANAGE))
			message.delete().queue(success -> { });
	}

	public void deleteMessage(Message message, int delay) {
		if(message.getGuild().getSelfMember().hasPermission(message.getTextChannel(), Permission.MESSAGE_MANAGE))
			message.delete().queueAfter(delay, TimeUnit.SECONDS, success -> { });
	}

	public void sendHelpMessage(WuffyMessageRecivedEvent event, String command, String[] args) {
		EmbedBuilder message = this.buildHelpMessage(event, command, args);

		if(message != null)
			this.sendMessage(event, MessageType.HELP, message);
	}

	public EmbedBuilder buildHelpMessage(WuffyMessageRecivedEvent event, String command, String[] args) {
		if(this.hasBotPermission(event, Permission.MESSAGE_EMBED_LINKS)) {
			EmbedBuilder embedBuilder = this.createEmbed(event, MessageType.HELP);

			List<String> prefixes = null;
			String locale = "en-US";

			if(event.getChannelType() == ChannelType.TEXT) {
				prefixes = event.getGuild(WuffyGuild.class).getPrefixes();
				locale = event.getMember(WuffyMember.class).getLocale();
			}

			String className = this.getClass().getSimpleName().substring(7).toUpperCase();
			String prefix = prefixes != null ? prefixes.isEmpty() ? String.format("<@%s> ", Long.toString(event.getJDA().getSelfUser().getIdLong())) : prefixes.get(0) : "~";

			embedBuilder.addField(
					this.i18n.format(TranslationKeys.MESSAGE_HELP_DESCRIPTION, locale),
					this.i18n.format(String.format("%s%s", TranslationKeys.HELP_DESCRIPTION, className), locale,
							"%p", prefix),
					false)
			.addField(
					this.i18n.format(TranslationKeys.MESSAGE_HELP_USAGE, locale),
					this.i18n.format(String.format("%s%s", TranslationKeys.HELP_USAGE, className), locale,
							"%p", prefix),
					true)
			.addField(
					this.i18n.format(TranslationKeys.MESSAGE_HELP_PERMISSION, locale),
					this.settings.memberPermissionList().length == 0 ?
							this.i18n.format(TranslationKeys.MESSAGE_HELP_NO_PERMISSION, locale) :
							Arrays.asList(this.settings.memberPermissionList()).stream()
								.map(perm -> String.format("``%s``", perm.key))
								.collect(Collectors.joining("\n")),
					true)
			.addField(
					this.i18n.format(TranslationKeys.MESSAGE_HELP_ALIASES, locale),
					Arrays.asList(this.getSettings().aliases()).stream()
						.map(alias -> String.format("``%s%s``", prefix, alias))
						.collect(Collectors.joining("\n")),
					true);

			return embedBuilder;
		}
		return null;
	}

	public boolean isNSFWChannel(WuffyMessageRecivedEvent event) {
		return this.isNSFWChannel(event, true);
	}

	public boolean isNSFWChannel(WuffyMessageRecivedEvent event, boolean nsfwMessage) {
		if(event.getChannelType() != ChannelType.TEXT || event.getTextChannel().isNSFW())
			return true;

		if(nsfwMessage)
			this.sendMessage(event,
					MessageType.SYNTAX,
					i18n.format(TranslationKeys.MESSAGE_NSFW_CHANNEL_NOT,
							event.getChannelType() == ChannelType.TEXT ?
									event.getMember(WuffyMember.class).getLocale() :
									event.getAuthor(WuffyUser.class).getUserLocale("en-US")));
		return false;
	}

	public boolean isAlphaTesting(WuffyMessageRecivedEvent event) {
		return this.isNSFWChannel(event, true);
	}

	public boolean isAlphaTesting(WuffyMessageRecivedEvent event, boolean alphaTesterMessage) {
		if(event.getAuthor(WuffyUser.class).isAlphaTester())
			return true;

		if(alphaTesterMessage)
			this.sendMessage(event,
					MessageType.SYNTAX,
					i18n.format(TranslationKeys.MESSAGE_ALPHA_NOT_ALPHA_TESTER,
							event.getChannelType() == ChannelType.TEXT ?
									event.getMember(WuffyMember.class).getLocale() :
									event.getAuthor(WuffyUser.class).getUserLocale("en-US")));
		return false;
	}

	public boolean hasBotPermission(WuffyMessageRecivedEvent event) {
		return this.hasBotPermission(event, true, this.settings.guildPermissionRequierd());
	}

	public boolean hasBotPermission(WuffyMessageRecivedEvent event, Permission... permissions) {
		return this.hasBotPermission(event, true, permissions);
	}

	public boolean hasBotPermission(WuffyMessageRecivedEvent event, boolean permissionMessage, Permission... permissions) {
		if(event.getChannelType() != ChannelType.TEXT)
			return true;

		if(this.settings.adminCommand())
			if(this.handler.getCore().isAdmin(event.getAuthor()))
				return true;
			else {
				String locale = event.getMember(WuffyMember.class).getLocale();

				this.sendMessage(event, MessageType.PERMISSION, i18n.format(
						TranslationKeys.MESSAGE_BOT_NO_PERMISSION,
						locale,
						"%p", i18n.format(String.format("%s%s", TranslationKeys.ENUM_PERMISSION, "BOT_ADMIN"), locale)));
				return false;
			}

		if(event.getGuild().getSelfMember().getRealMember().hasPermission(event.getTextChannel(), permissions))
			return true;

		if(permissionMessage) {
			String locale = event.getMember(WuffyMember.class).getLocale();

			List<Permission> permissionHas = event.getGuild().getSelfMember().getPermissions(event.getTextChannel());
			String permissionNeeded = Arrays.asList(permissions).stream()
					.filter(permission -> !permissionHas.contains(permission))
					.map(permission -> i18n.format(String.format("%s%s", TranslationKeys.ENUM_PERMISSION, permission.name()), locale))
					.distinct()
					.collect(Collectors.joining(", "));

			this.sendMessage(event, MessageType.PERMISSION, i18n.format(
							TranslationKeys.MESSAGE_BOT_NO_PERMISSION,
							locale,
							"%p", permissionNeeded));
		}

		return false;
	}

	public boolean hasMemberPermission(WuffyMessageRecivedEvent event) {
		return this.hasMemberPermission(event, true, this.settings.memberPermissionRequierd());
	}

	public boolean hasMemberPermission(WuffyMessageRecivedEvent event, PermissionKeys... permissions) {
		return this.hasMemberPermission(event, true, permissions);
	}

	public boolean hasMemberPermission(WuffyMessageRecivedEvent event, boolean permissionMessage, PermissionKeys... permissions) {
		WuffyMember member = event.getMember(WuffyMember.class);

		if(member.hasPermission(event.getTextChannel(), permissions))
			return true;

		if(permissionMessage) {
			String permissionNeeded = Arrays.asList(permissions).stream()
					.map(permission -> permission.key)
					.distinct()
					.collect(Collectors.joining(", "));

			this.sendMessage(event, MessageType.PERMISSION, i18n.format(
							TranslationKeys.MESSAGE_NO_PERMISSION,
							event.getMember(WuffyMember.class).getLocale(),
							"%p", permissionNeeded));
		}

		return false;
	}

	public CommandHandler getHandler() {
		return this.handler;
	}

	public CommandSettings getSettings() {
		return this.settings;
	}
}