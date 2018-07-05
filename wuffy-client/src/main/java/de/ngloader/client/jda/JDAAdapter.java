package de.ngloader.client.jda;

import de.ngloader.core.Core;
import de.ngloader.core.jda.IJDAAdapter;
import net.dv8tion.jda.core.hooks.EventListener;

public class JDAAdapter implements IJDAAdapter {

	private final Core core;

	public JDAAdapter(Core core) {
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

	public Core getCore() {
		return core;
	}
}