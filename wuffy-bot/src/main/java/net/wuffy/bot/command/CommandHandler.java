package net.wuffy.bot.command;

import net.dv8tion.jda.core.JDA;
import net.wuffy.bot.WuffyBot;

public class CommandHandler {

	private final WuffyBot core;
	private final JDA shard;

	private CommandRegestry regestry;
	private CommandExecuter executer;
	private CommandListener listener;

	public CommandHandler(WuffyBot core, JDA shard) {
		this.core = core;
		this.shard = shard;

		this.regestry = new CommandRegestry(this);
		this.executer = new CommandExecuter(this);
		this.listener = new CommandListener(this);

		this.shard.addEventListener(this.listener);
	}

	public CommandRegestry getRegestry() {
		return this.regestry;
	}

	public CommandExecuter getExecuter() {
		return this.executer;
	}

	public CommandListener getListener() {
		return this.listener;
	}

	public WuffyBot getCore() {
		return this.core;
	}

	public JDA getShard() {
		return this.shard;
	}
}