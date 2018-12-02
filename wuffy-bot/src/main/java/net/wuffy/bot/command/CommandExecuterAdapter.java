package net.wuffy.bot.command;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntFunction;

public class CommandExecuterAdapter implements IntFunction<CommandExecuter> {

	private Map<Integer, CommandExecuter> commandHandlers = new HashMap<Integer, CommandExecuter>();

	@Override
	public CommandExecuter apply(int shardId) {
		if(!this.commandHandlers.containsKey(shardId))
			this.commandHandlers.put(shardId, new CommandExecuter());

		return this.commandHandlers.get(shardId);
	}
}