package de.ngloader.core.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.ngloader.core.logger.Logger;
import de.ngloader.core.util.ITickable;

/**
 * @author Ingrim4
 * @edited NgLoader
 */
public class ConsoleCommandManager implements ITickable {

	private final Map<String, ConsoleCommandInfo> commands = new HashMap<>();
	private final Queue<String> queue = new ConcurrentLinkedQueue<>();

	private boolean running = true;

	public ConsoleCommandManager() {
		this.registerExecutor(new de.ngloader.core.console.command.ConsoleCommandHelp(this));

		Thread commandThread = new Thread(() -> {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
				String input;
				while((input = reader.readLine()) != null && this.running)
					if(this.running)
						this.queue.add(input);
			} catch(IOException e) {
				throw new RuntimeException("Exception handling console input", e);
			}
		});

		commandThread.setDaemon(true);
		commandThread.start();
	}

	public void close() {
		this.running = false;
		this.queue.clear();
	}

	@Override
	public void update() {
		while(!this.queue.isEmpty())
			this.processCommand(this.queue.poll());
	}

	private void processCommand(String input) {
		var split = input.split("\\s+");
		var args = Arrays.copyOfRange(split, 1, split.length);
		var command = split[0].toLowerCase();

		var commandInfo = this.commands.get(command);
		if(commandInfo == null) {
			Logger.warn("Unknown command, please use 'help' for more information.");
			return;
		}

		var commandResult = commandInfo.executor.onCommand(args);
		switch (commandResult.getCommandResult()) {
		case SYNTAX:
			Logger.warn("Wrong syntax! Usage: '" + commandInfo.command.usage() + "'");
			break;

		case ERROR:
			Logger.fatal("An error has occured: ", commandResult.getThrowable());
			break;

		default:
			break;
		}
	}

	public void registerExecutor(IConsoleCommandExecutor executor) {
		this.registerExecutor(executor.getClass().getAnnotation(ConsoleCommand.class), executor);
	}

	public void registerExecutor(ConsoleCommand command, IConsoleCommandExecutor executor) {
		var commandInfo = new ConsoleCommandInfo(command, executor);
		Arrays.asList(command.aliases()).forEach(alias -> this.commands.put(alias.toLowerCase(), commandInfo));
	}

	public Map<String, ConsoleCommandInfo> getCommands() {
		return commands;
	}

	public class ConsoleCommandInfo {

		private ConsoleCommand command;
		private IConsoleCommandExecutor executor;

		public ConsoleCommandInfo(ConsoleCommand command, IConsoleCommandExecutor executor) {
			this.command = command;
			this.executor = executor;
		}

		public ConsoleCommand getCommand() {
			return command;
		}

		public IConsoleCommandExecutor getExecutor() {
			return executor;
		}
	}
}