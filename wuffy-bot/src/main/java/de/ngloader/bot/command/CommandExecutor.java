package de.ngloader.bot.command;

import java.util.LinkedList;
import java.util.Queue;

import de.ngloader.bot.WuffyBot;
import de.ngloader.core.command.CommandManager;
import de.ngloader.core.event.WuffyMessageRecivedEvent;

public class CommandExecutor extends de.ngloader.core.command.CommandExecutor<WuffyBot, BotCommand> {

	private static final Class<BotCommand> CAST_CLASS = BotCommand.class;

	private final Queue<CommandInfo> queue = new LinkedList<CommandInfo>();

	public CommandExecutor(CommandManager<WuffyBot> manager) {
		super(manager);
	}

	@Override
	public void update() {
		if(!this.queue.isEmpty())
			execute(this.queue.poll());
	}

	private void execute(CommandInfo commandInfo) {
		this.execute(commandInfo.event, commandInfo.command, commandInfo.args);
	}

	@Override
	protected void execute(WuffyMessageRecivedEvent event, BotCommand command, String[] args) {
		command.execute(event, args);
	}

	@Override
	protected void queue(WuffyMessageRecivedEvent event, String command, String[] args) {
		BotCommand commandInfo = (BotCommand) this.manager.getRegistry().getCommand(command);

		if(commandInfo != null)
			if(!commandInfo.isCommandBlocked())
				this.queue.add(new CommandInfo(event, CommandExecutor.CAST_CLASS.cast(commandInfo), args));
			else
				event.getChannel().sendMessage("This command is currently disabled.");
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