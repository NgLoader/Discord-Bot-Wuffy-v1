package de.ngloader.notification;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.MongoCollection;

import de.ngloader.core.config.ConfigService;
import de.ngloader.core.logger.Logger;
import de.ngloader.core.logger.LoggerManager;
import de.ngloader.core.twitch.TwitchAPI;
import de.ngloader.core.youtube.YoutubeAPI;
import de.ngloader.notification.database.MongoExtensionGuild;
import de.ngloader.notification.types.TwitchAnnouncement;
import de.ngloader.notification.types.YoutubeAnnouncement;

public class Wuffy extends TickingTask {

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

	private final MongoExtensionGuild extensionGuild;
	private final MongoCollection<Document> guildCollection;

	private TwitchAnnouncement twitchAnnouncement;
	private YoutubeAnnouncement youtubeAnnouncement;

	private final Block<Document> block = new Block<Document>() {

		@Override
		public void apply(Document document) {
			Document notification = document.get("notification", Wuffy.this.EMPTY_DOCUMENT);
			if(!notification.isEmpty()) {
				//Twitch
				List<Document> tList = notification.get("TWITCH", Wuffy.this.EMPTY_LIST);
				if(!tList.isEmpty())
					Wuffy.this.twitchAnnouncement.add(document.getString("_guildId"), tList);

//				Youtube
				List<Document> ytList = notification.get("YOUTUBE", Wuffy.this.EMPTY_LIST);
				if(!ytList.isEmpty())
					Wuffy.this.youtubeAnnouncement.add(document.getString("_guildId"), ytList);
			}
		}
	};

	public Wuffy() {
		Logger.info("Bootstrap", "Starting wuffy notification.");

		this.config = ConfigService.getConfig(AnnouncementConfig.class);

		this.twitchAnnouncement = new TwitchAnnouncement(new TwitchAPI(this.config.twitch.token));
		this.youtubeAnnouncement = new YoutubeAnnouncement(new YoutubeAPI(this.config.youtube.token));

		this.extensionGuild = new MongoExtensionGuild(config.database.mongo);
		this.extensionGuild.connect();

		this.guildCollection = this.extensionGuild.getCollection("guild");

		Logger.info("Bootstrap", "Wuffy notification successful started.");

		new Thread(() -> {
			while(true) {
				try {
					Thread.sleep(5000); //Sleep 5 seconds.

					this.guildCollection.find().forEach(this.block);

					if(this.config.twitch.enabled)
						this.twitchAnnouncement.run();
					if(this.config.youtube.enabled)
						this.youtubeAnnouncement.run();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public MongoExtensionGuild getExtensionGuild() {
		return this.extensionGuild;
	}

	public MongoCollection<Document> getGuildCollection() {
		return this.guildCollection;
	}

	@Override
	protected void update() {
	}

	@Override
	protected void stop() {
	}
}