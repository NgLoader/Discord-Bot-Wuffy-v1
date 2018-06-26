package de.ngloader.bootstrap;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.ngloader.client.WuffyClient;
import de.ngloader.core.Core;
import de.ngloader.core.config.ConfigService;
import de.ngloader.core.logger.Logger;
import de.ngloader.core.logger.LoggerManager;
import de.ngloader.core.util.GsonUtil;

public class Wuffy {

	public static final Long START_TIME_IN_MILLIS = System.currentTimeMillis();

	private static final List<Core> BOTS = new LinkedList<Core>();
	private static final List<Core> CLIENTS = new LinkedList<Core>();

	public static void main(String[] args) {
		if(Float.parseFloat(System.getProperty( "java.class.version" )) < 54) {
			System.err.println("*** ERROR *** Wuffy equires Java 10 or above to work! Please download and install it!");
			return;
		}

		Logger.info("Bootstrap", "Starting wuffy.");

		var threads = 32;

		for(int i = 0; i < args.length; i++) {
			var arg = args[i];

			if(arg.equalsIgnoreCase("--debug"))
				LoggerManager.setDebug(true);
			else if(arg.equalsIgnoreCase("--threads"))
				threads = Integer.valueOf(args[i + 1]);
		}

		System.setProperty("io.netty.eventLoopThreads", Integer.toString(threads));

		GlobalConfig config = ConfigService.getConfig(GlobalConfig.class);

		Logger.info("Bootstrap", "Initializing bots and clients");

//		config.bots.stream().filter(bot -> bot.enabled).forEach(bot -> Wuffy.BOTS.add(new WuffyBot(bot)));
		config.clients.stream().filter(client -> client.enabled).forEach(client -> Wuffy.CLIENTS.add(new WuffyClient(client)));

		Logger.info("Bootstrap", "Starting tickingtask's");

//		Wuffy.BOTS.forEach(bot -> bot);
	}

	public static List<Core> getBots() {
		return Collections.unmodifiableList(Wuffy.BOTS);
	}

	public static List<Core> getClients() {
		return Collections.unmodifiableList(Wuffy.CLIENTS);
	}
}