package de.ngloader.core.command;

import de.ngloader.core.Core;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.jda.IJDAAdapter;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public abstract class CommandTrigger <C extends Core> extends ListenerAdapter {

	protected final CommandManager<C> manager;

	public CommandTrigger(CommandManager<C> manager) {
		this.manager = manager;
		this.manager.getCore().getJdaAdapter(IJDAAdapter.class).addListener(this);
	}

	public void onTrigger(WuffyMessageRecivedEvent event, String command, String[] args) {
		this.manager.executor.queue(event, command, args);
	}

	public CommandManager<C> getManager() {
		return this.manager;
	}
}