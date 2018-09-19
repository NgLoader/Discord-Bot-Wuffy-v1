package net.wuffy.dns;

import io.netty.channel.epoll.Epoll;
import io.netty.channel.kqueue.KQueue;
import net.wuffy.common.config.ConfigService;
import net.wuffy.common.logger.Logger;
import net.wuffy.common.logger.LoggerManager;
import net.wuffy.common.util.WebUtil;

public class DNS {

	public static final boolean KQUEUE = KQueue.isAvailable();
	public static final boolean EPOLL = Epoll.isAvailable();

	public static int currentlyMasterAddress = WebUtil.convertIpToInt("127.0.0.1"); //TODO add master

	public static void main(String[] args) throws Exception {
		if(Float.parseFloat(System.getProperty( "java.class.version" )) < 54) {
			System.err.println("*** ERROR *** Wuffy equires Java 10 or above to work! Please download and install it!");
			return;
		}

		Logger.info("Bootstrap", "Starting wuffy.");

		System.setProperty("developerMode", "false");

		var threads = 32;

		for(int i = 0; i < args.length; i++) {
			var arg = args[i];

			if(arg.equalsIgnoreCase("--debug"))
				LoggerManager.setDebug(true);
			else if(arg.equalsIgnoreCase("--threads"))
				threads = Integer.valueOf(args[i + 1]);
		}

		System.setProperty("io.netty.eventLoopThreads", Integer.toString(threads));

		Logger.info("Bootstrap", "Loading config");

		DNSConfig config = ConfigService.getConfig(DNSConfig.class);

		Thread.currentThread().setName("Wuffy DNS - Main");

		Logger.info("Bootstrap", "Starting DNS Server");
		new Thread(new DNSServer(config), "Wuffy DNS - Server").start();
	}
}