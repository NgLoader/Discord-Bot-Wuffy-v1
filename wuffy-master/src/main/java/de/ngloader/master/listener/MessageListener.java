package de.ngloader.master.listener;

import de.ngloader.api.WuffyServer;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

	public void onMessageReceived(net.dv8tion.jda.core.events.message.MessageReceivedEvent event) {
		WuffyServer.getCommandManager().issueCommand(event);
	};
}