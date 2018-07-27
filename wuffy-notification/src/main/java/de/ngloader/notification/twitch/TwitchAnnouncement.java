package de.ngloader.notification.twitch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.Document;

import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.WriteModel;

import de.ngloader.core.twitch.TwitchAPI;
import de.ngloader.core.twitch.response.TwitchResponse;
import de.ngloader.core.twitch.response.TwitchResponseStream;
import de.ngloader.core.twitch.response.TwitchResponseUser;
import de.ngloader.core.util.GsonUtil;
import de.ngloader.core.util.WebhookUtil;
import de.ngloader.notification.TickingTask;
import de.ngloader.notification.Wuffy;

public class TwitchAnnouncement extends TickingTask {

	private static final String DEFAULT_EMBED_MESSAGE = "{\"username\": \"Twitch\",\"avatar_url\": \"%urlpi\",\"content\": \"%n ist jetzt live auf Twitch\",\"embeds\": [{\"title\": \"%t\",\"url\": \"%urll\",\"color\": 1671309,\"timestamp\": \"%sa\",\"footer\": {\"icon_url\": \"%urlpi\",\"text\": \"%n\"},\"thumbnail\": {\"url\": \"%urlpi\"},\"image\": {\"url\": \"%urlt\"},\"author\": {\"name\": \"%n\",\"url\": \"%urll\",\"icon_url\": \"%urlpi\"},\"fields\": [{\"name\": \"Zuschauer\",\"value\": \"%vc\"}]}]}";

	private Map<String, List<Message>> queueNames = new HashMap<String, List<Message>>();

	private final TwitchAPI twitchAPI;

	public TwitchAnnouncement(TwitchAPI twitchAPI) {
		this.twitchAPI = twitchAPI;
	}

	@Override
	protected void update() {
		try {
			List<String> fetchNames = new ArrayList<String>();

			for(String name : this.queueNames.keySet())
				if(fetchNames.size() > 100)
					break;
				else
					fetchNames.add(name);

			List<Message> messages = new ArrayList<Message>();

			fetchNames.forEach(name -> messages.addAll(this.queueNames.remove(name)));

			if(fetchNames.isEmpty()) {
				this.running = false;
				return;
			}

			Map<String, TwitchResponseUser> users = this.twitchAPI.getUserHandler().getUserByName(fetchNames).data.stream().collect(Collectors.toMap(key -> key.id, value -> value));

			TwitchResponse<TwitchResponseStream> streams = this.twitchAPI.getStreamHandler().getStreamByUserId(users.keySet().stream().collect(Collectors.toList()));

			List<WriteModel<Document>> writeModels = new ArrayList<WriteModel<Document>>();

			for(TwitchResponseStream stream : streams.data) {
				if(!stream.type.equals("live"))
					continue;

				TwitchResponseUser user = users.get(stream.user_id);

				for(Message message : messages.stream().filter(message -> message.name.equalsIgnoreCase(user.display_name)).collect(Collectors.toList())) {
					if(message.lastUpdate != null && message.lastUpdate.equals(stream.started_at))
						continue;

					//Update started_at to mongodb
					writeModels.add(new UpdateOneModel<Document>(
							new Document("_guildId", message.guildId)
								.append("notification.TWITCH.name", message.name),
							new Document("$set", new Document("notification.TWITCH.$.lastUpdate", stream.started_at))));

					WebhookUtil.send(message.webhook, (message.message == null || message.message.isEmpty() ? DEFAULT_EMBED_MESSAGE : message.message)
							.replace("%vc", Integer.toString(stream.viewer_count))
							.replace("%n", user.display_name)
							.replace("%urll", String.format("https://www.twitch.tv/%s", user.display_name))
							.replace("%t", stream.title)
							.replace("%urlt", stream.thumbnail_url.replace("{width}", "320").replace("{height}", "180"))
							.replace("%urlpi", user.profile_image_url)
							.replace("%urloi", user.offline_image_url)
							.replace("%sa", stream.started_at));
				}
			}

			if(!writeModels.isEmpty())
				Wuffy.getInstance().getExtensionGuild().getPrintBatchResult().onResult(Wuffy.getInstance().getGuildCollection().bulkWrite(writeModels), null);

			if(this.queueNames.isEmpty())
				this.running = false;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void stop() {
		this.running = true; //Reset for next run
		this.queueNames.clear();
	}

	public void add(String guildId, List<Document> documents) {
		if(guildId == null || guildId.isEmpty())
			return;

		for(Document document : documents) {
			Message message = GsonUtil.GSON.fromJson(document.toJson(), Message.class);

			if(message != null && message.webhook != null && !message.webhook.isEmpty()) {
				message.guildId = guildId;

				if(!this.queueNames.containsKey(message.name.toLowerCase()))
					this.queueNames.put(message.name.toLowerCase(), new ArrayList<Message>());
				this.queueNames.get(message.name.toLowerCase()).add(message);
			}
		}
	}

	class Message {
		public String guildId;
		public String name;
		public String webhook;
		public String message;

		public String lastUpdate;

		public Message(String guildId, String name, String webhook, String message, String lastUpdate) {
			this.guildId = guildId;
			this.name = name;
			this.webhook = webhook;
			this.message = message;
			this.lastUpdate = lastUpdate;
		}
	}
}