package de.ngloader.bot.command;

import java.util.LinkedList;
import java.util.Queue;

import de.ngloader.bot.WuffyBot;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.bot.util.ReplayBuilder;
import de.ngloader.core.command.CommandManager;
import de.ngloader.core.command.MessageType;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.logger.Logger;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.utils.PermissionUtil;

public class CommandExecutor extends de.ngloader.core.command.CommandExecutor<WuffyBot, BotCommand> {

	private static final Class<BotCommand> CAST_CLASS = BotCommand.class;

	private final Queue<CommandInfo> queue = new LinkedList<CommandInfo>();

	public CommandExecutor(CommandManager<WuffyBot> manager) {
		super(manager);
	}

	@Override
	public void update() {
		try {
			if(!this.queue.isEmpty())
				execute(this.queue.poll());
		} catch(Exception e) {
			e.printStackTrace();
			Logger.fatal("CommandExecutor", "Error by executing command queue", e);
		}
	}

	private void execute(CommandInfo commandInfo) {
		this.execute(commandInfo.event, commandInfo.command, commandInfo.args);
	}

	@Override
	protected void execute(WuffyMessageRecivedEvent event, BotCommand command, String[] args) {
		Long time = System.currentTimeMillis();

		event.getChannel().sendTyping().queue();

		if(PermissionUtil.checkPermission(event.getGuild().getSelfMember(), Permission.ADMINISTRATOR))
			command.execute(event, args);
		else if(event.getTextChannel().canTalk())
			new ReplayBuilder(event, MessageType.ERROR, event.getCore().getI18n().format(TranslationKeys.MESSAGE_BOT_NO_ADMIN_PERMISSION,
					event.getMember(WuffyMember.class).getLocale()))
			.queue();
		else
			event.getAuthor().openPrivateChannel().complete().sendMessage(
					event.getCore().getI18n().format(TranslationKeys.MESSAGE_BOT_NO_ADMIN_PERMISSION, event.getMember(WuffyMember.class).getLocale()))
			.queue();

		Logger.debug("CommandExecutor", String.format("Command (%s) was executed in %sms", command.getClass().getSimpleName(), Long.toString(System.currentTimeMillis() - time)));
	}

	@Override
	protected void queue(WuffyMessageRecivedEvent event, String command, String[] args) {
		BotCommand commandInfo = (BotCommand) this.manager.getRegistry().getCommand(command);

		if(commandInfo != null)
			if(!commandInfo.isCommandBlocked() || event.getGuild(WuffyGuild.class).getDisabledCommands().contains(command))
				this.queue.add(new CommandInfo(event, CommandExecutor.CAST_CLASS.cast(commandInfo), args));
			else
				event.getChannel().sendMessage(event.getCore().getI18n().format(TranslationKeys.MESSAGE_COMMAND_DISABLED, event.getMember(WuffyMember.class).getLocale()));
	}

	public class CommandInfo {

		public WuffyMessageRecivedEvent event;
		public BotCommand command;
		public String[] args;

		public CommandInfo(WuffyMessageRecivedEvent event, BotCommand command, String[] args) {
			this.event = event;
			this.command = command;
			this.args = args;
		}
	}
}