package net.wuffy.notification;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.MongoCollection;

import net.wuffy.common.config.ConfigService;
import net.wuffy.common.logger.Logger;
import net.wuffy.common.logger.LoggerManager;
import net.wuffy.core.console.ConsoleCommandManager;
import net.wuffy.core.twitch.TwitchAPI;
import net.wuffy.core.youtube.YoutubeAPI;
import net.wuffy.notification.database.MongoExtensionGuild;
import net.wuffy.notification.types.Announcement;
import net.wuffy.notification.types.TwitchAnnouncement;
import net.wuffy.notification.types.YoutubeAnnouncement;

public class Wuffy {

	public static final Long START_TIME_IN_MILLIS = System.currentTimeMillis();

	private static Wuffy instance;

	public static Wuffy getInstance() {
		return Wuffy.instance;
	}

	public static void main(String[] args) throws Exception {
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

		Wuffy.instance = new Wuffy();
	}

	private final Document EMPTY_DOCUMENT = new Document();
	private final List<Document> EMPTY_LIST = new ArrayList<Document>();

	private final AnnouncementConfig config;

	private final ConsoleCommandManager consoleCommandManager;

	private final MongoExtensionGuild extensionGuild;
	private final MongoCollection<Document> guildCollection;

	private final List<Announcement> announcements = new ArrayList<Announcement>();

	private final Block<Document> block = new Block<Document>() {

		@Override
		public void apply(Document document) {
			Document notification = document.get("notification", Wuffy.this.EMPTY_DOCUMENT);
			if(!notification.isEmpty()) {
				for(Announcement announcement : Wuffy.this.announcements)
					if(notification.containsKey(announcement.getTypeName()))
						announcement.add(document.getString("_guildId"), notification.get(announcement.getTypeName(), Wuffy.this.EMPTY_LIST));
			}
		}
	};

	public Wuffy() {
		Logger.info("Bootstrap", "Starting wuffy notification.");

		this.config = ConfigService.getConfig(AnnouncementConfig.class);
	
		this.consoleCommandManager = new ConsoleCommandManager();

		this.extensionGuild = new MongoExtensionGuild(config.database.mongo);
		this.extensionGuild.connect();

		this.guildCollection = this.extensionGuild.getCollection("guild");

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			Logger.info("Shutdown hook", "Shutting down wuffy notification.");
			LoggerManager.close();
		}));

		this.announcements.add(new TwitchAnnouncement(this.guildCollection, new TwitchAPI(this.config.twitch.token)));
		this.announcements.add(new YoutubeAnnouncement(this.guildCollection, new YoutubeAPI(this.config.youtube.token)));

		Logger.info("Bootstrap", "Wuffy notification successful started.");

		new Thread(() -> {
			while(true) {
				try {
					Thread.sleep(30000); //Request every 30 seconds all database data.

					this.guildCollection.find().forEach(this.block);

					this.announcements.forEach(Announcement::tryStart);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}, "Wuffy Notification - Database queue").start();
	}

	public MongoExtensionGuild getExtensionGuild() {
		return this.extensionGuild;
	}

	public MongoCollection<Document> getGuildCollection() {
		return this.guildCollection;
	}

	public ConsoleCommandManager getConsoleCommandManager() {
		return this.consoleCommandManager;
	}

	public AnnouncementConfig getConfig() {
		return this.config;
	}
}