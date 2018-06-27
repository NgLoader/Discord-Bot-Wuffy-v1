package de.ngloader.core.command;

import de.ngloader.core.Core;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public abstract class CommandTrigger <C extends Core> extends ListenerAdapter {

	protected final CommandManager<C> manager;

	public CommandTrigger(CommandManager<C> manager) {
		this.manager = manager;

		//TODO !!!add this to jda listener!!! "this.manager.getCore().getJDA();"
	}

	public void onTrigger(WuffyMessageRecivedEvent event, String[] args) {
		this.manager.executor.queue(event, args);
	}

	public CommandManager<C> getManager() {
		return this.manager;
	}
}