package net.wuffy.core.event.test;

import net.dv8tion.jda.core.JDA;

public class RegisteredListener {

	private final Listener listener;
	private final JDA jda;
	private final EventExecuter executer;

	public RegisteredListener(Listener listener, JDA jda, EventExecuter executer) {
		this.listener = listener;
		this.jda = jda;
		this.executer = executer;
	}

	public EventExecuter getExecuter() {
		return this.executer;
	}

	public Listener getListener() {
		return this.listener;
	}

	public JDA getJDA() {
		return this.jda;
	}

	public void callEvent(final Event event) throws EventException {
		this.executer.execute(this.listener, event);
	}
}