package net.wuffy.client.jda;

import net.dv8tion.jda.core.hooks.EventListener;
import net.wuffy.core.CoreOLD;
import net.wuffy.core.jda.IJDA;

public class JDAAdapter implements IJDA {

	private final CoreOLD core;

	public JDAAdapter(CoreOLD core) {
		this.core = core;
	}

	@Override
	public void login() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logout() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addListener(EventListener listener) {
		// TODO Auto-generated method stub
		
	}

	public CoreOLD getCore() {
		return core;
	}
}