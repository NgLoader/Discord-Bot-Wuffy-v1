package de.ngloader.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.ngloader.common.logger.Logger;
import de.ngloader.common.logger.LoggerManager;
import de.ngloader.common.util.ITickable;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandManager implements ITickable {

	private static final Logger LOGGER = LoggerManager.getLogger();

	private final Map<String, Map<String, CommandInfo>> commands = new HashMap<String, Map<String, CommandInfo>>();
	private final Queue<GuildMessageReceivedEvent> commandQueue = new ConcurrentLinkedQueue<>();

	private long nextWarnMessage = 0;

	@Override
	public void update() {
		if(!this.commandQueue.isEmpty())
			this.processCommand(commandQueue.poll());
	}

	public void issueCommand(GuildMessageReceivedEvent event) {
		if(this.commandQueue.size() < 100)
			this.commandQueue.add(event);
		else if(nextWarnMessage < System.currentTimeMillis()) {
			LOGGER.warn("Command queue is full.");
			nextWarnMessage = System.currentTimeMillis() + 15000;
		}
	}

	private void processCommand(GuildMessageReceivedEvent event) {
		//TODO fill out
		var message = event.getMessage().getContentRaw();

		var split = message.split("\\s+");
		var args = Arrays.copyOfRange(split, 1, split.length);
		var command = split[0].toLowerCase();

//		var commandInfo = null;
//
//		if(commandInfo == null) {
//			LOGGER.debug("CommandExecutor", "Command '" + command + "' not found.");
//			return;
//		}
//
//		CommandResult commandResult = commandInfo.executor.onCommand(event, args);
//		switch (commandResult.getCommandResult()) {
//		case SYNTAX:
//			break;
//
//		case ERROR:
//			break;
//
//		default:
//			break;
//		}
	}

	public void close() {
		this.commandQueue.clear();
	}

	public void registerExecutor(ICommandExecutor executor) {
		Commands commands = executor.getClass().getAnnotation(Commands.class);
		if(commands != null)
			for(int i = 0; i < commands.value().length; i++)
				this.registerExecutor(commands.value()[i], executor);
		else
			this.registerExecutor(executor.getClass().getAnnotation(Command.class), executor);
	}

	public void registerExecutor(Command command, ICommandExecutor executor) {
		CommandInfo commandInfo = new CommandInfo(command, executor);

		//TODO register commands with langauge
	}

	class CommandInfo {

		private Command command;
		private ICommandExecutor executor;

		public CommandInfo(Command command, ICommandExecutor executor) {
			this.command = command;
			this.executor = executor;
		}

		public Command getCommand() {
			return command;
		}

		public ICommandExecutor getExecutor() {
			return executor;
		}
	}
}