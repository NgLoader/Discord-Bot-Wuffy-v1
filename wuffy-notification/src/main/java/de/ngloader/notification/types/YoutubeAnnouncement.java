package de.ngloader.notification.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.google.api.services.youtube.model.SearchResult;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.WriteModel;

import de.ngloader.core.util.GsonUtil;
import de.ngloader.core.util.WebhookUtil;
import de.ngloader.core.youtube.YoutubeAPI;
import de.ngloader.core.youtube.YoutubeOptionalParameterBuilder;
import de.ngloader.core.youtube.YoutubeOptionalParameters;
import de.ngloader.core.youtube.YoutubePart;
import de.ngloader.notification.TickingTask;
import de.ngloader.notification.Wuffy;

public class YoutubeAnnouncement extends TickingTask {

	private static final String DEFAULT_EMBED_MESSAGE = "{\"username\":\"Youtube\",\"avatar_url\":\"%tm300x300\",\"embeds\":[{\"title\":\"%ti\",\"image\":{\"url\":\"%tm580x900\"},\"url\":\"%u\",\"color\":16711680,\"timestamp\":\"%pa\",\"footer\":{\"icon_url\":\"%tm300x300\",\"text\":\"%ct\"},\"author\":{\"name\":\"%ct\",\"url\":\"%u\",\"icon_url\":\"%tm300x300\"}}]}";

	private Map<String, List<Message>> queueNames = new HashMap<String, List<Message>>();

	private final YoutubeAPI youtubeAPI;

	public YoutubeAnnouncement(YoutubeAPI youtubeAPI) {
		this.youtubeAPI = youtubeAPI;
	}

	@Override
	protected void update() {
		try {
			if(this.queueNames.isEmpty()) {
				this.running = false;
				return;
			}

			String id = this.queueNames.keySet().toArray()[0].toString();
			List<Message> messages = this.queueNames.remove(id);

			List<SearchResult> searches = this.youtubeAPI.getSearchHandler().get(new YoutubeOptionalParameterBuilder()
					.add(YoutubeOptionalParameters.CHANNELID, id)
					.add(YoutubeOptionalParameters.ORDER, "date")
					.add(YoutubeOptionalParameters.TYPE, "video")
					.add(YoutubeOptionalParameters.MAXRESULTS, 5),
					YoutubePart.SNIPPET).getItems();

			List<WriteModel<Document>> writeModels = new ArrayList<WriteModel<Document>>();

			for(Message message : messages) {
				String last = null;
				String lastLive = null;

				for(SearchResult result : searches) {
					if(result.getSnippet().getLiveBroadcastContent().equals("live")) {
						if(message.lastUpdateLiveStream != null && message.lastUpdateLiveStream.equals(result.getId().getVideoId()))
							break;

						lastLive = result.getId().getVideoId();

						WebhookUtil.send(message.webhook, (message.message == null || message.message.isEmpty() ? DEFAULT_EMBED_MESSAGE : message.message)
								.replace("%ct", result.getSnippet().getChannelTitle())
								.replace("%ti", result.getSnippet().getTitle())
								.replace("%pa", result.getSnippet().getPublishedAt().toStringRfc3339())
								.replace("%d", result.getSnippet().getDescription())
								.replace("%u", String.format("https://www.youtube.com/watch?v=%s", result.getId().getVideoId()))
								.replace("%td300x300", result.getSnippet().getThumbnails().getDefault().setWidth(300L).setHeight(300L).getUrl())
								.replace("%tm300x300", result.getSnippet().getThumbnails().getMedium().setWidth(300L).setHeight(300L).getUrl())
								.replace("%th300x300", result.getSnippet().getThumbnails().getHigh().setWidth(300L).setHeight(300L).getUrl())
								.replace("%td580x900", result.getSnippet().getThumbnails().getDefault().setWidth(580L).setHeight(900L).getUrl())
								.replace("%tm580x900", result.getSnippet().getThumbnails().getMedium().setWidth(580L).setHeight(900L).getUrl())
								.replace("%th580x900", result.getSnippet().getThumbnails().getHigh().setWidth(580L).setHeight(900L).getUrl()));

						if(message.lastUpdate == null)
							break;
					} else {
						if(message.lastUpdate != null && message.lastUpdate.equals(result.getId().getVideoId()))
							break;

						last = result.getId().getVideoId();

						WebhookUtil.send(message.webhook, (message.message == null || message.message.isEmpty() ? DEFAULT_EMBED_MESSAGE : message.message)
								.replace("%ct", result.getSnippet().getChannelTitle())
								.replace("%ti", result.getSnippet().getTitle())
								.replace("%pa", result.getSnippet().getPublishedAt().toStringRfc3339())
								.replace("%d", result.getSnippet().getDescription())
								.replace("%u", String.format("https://www.youtube.com/watch?v=%s", result.getId().getVideoId()))
								.replace("%td300x300", result.getSnippet().getThumbnails().getDefault().setWidth(300L).setHeight(300L).getUrl())
								.replace("%tm300x300", result.getSnippet().getThumbnails().getMedium().setWidth(300L).setHeight(300L).getUrl())
								.replace("%th300x300", result.getSnippet().getThumbnails().getHigh().setWidth(300L).setHeight(300L).getUrl())
								.replace("%td580x900", result.getSnippet().getThumbnails().getDefault().setWidth(580L).setHeight(900L).getUrl())
								.replace("%tm580x900", result.getSnippet().getThumbnails().getMedium().setWidth(580L).setHeight(900L).getUrl())
								.replace("%th580x900", result.getSnippet().getThumbnails().getHigh().setWidth(580L).setHeight(900L).getUrl()));

						if(message.lastUpdate == null)
							break;
					}
				}

				if(last != null)
					writeModels.add(new UpdateOneModel<Document>(
							new Document("_guildId", message.guildId)
								.append("notification.YOUTUBE.channelId", message.channelId),
							new Document("$set", new Document("notification.YOUTUBE.$.lastUpdate", last))));

				if(lastLive != null)
					writeModels.add(new UpdateOneModel<Document>(
							new Document("_guildId", message.guildId)
								.append("notification.YOUTUBE.channelId", message.channelId),
							new Document("$set", new Document("notification.YOUTUBE.$.lastUpdateLiveStream", lastLive))));
			}

			if(!writeModels.isEmpty())
				Wuffy.getInstance().getExtensionGuild().getPrintBatchResult().onResult(Wuffy.getInstance().getGuildCollection().bulkWrite(writeModels), null);

			if(this.queueNames.isEmpty())
				this.running = false;
		} catch(Exception e) {
			e.printStackTrace();
			this.running = false;
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

				if(message.channelId == null)
					continue;

				if(!this.queueNames.containsKey(message.channelId))
					this.queueNames.put(message.channelId, new ArrayList<Message>());
				this.queueNames.get(message.channelId).add(message);
			}
		}
	}

	class Message {
		public String guildId;
		public String name;
		public String webhook;
		public String message;

		public String lastUpdate;
		public String lastUpdateLiveStream;
		public String channelId;

		public Message(String guildId, String name, String webhook, String message, String lastUpdate, String lastUpdateLiveStream, String channelId) {
			this.guildId = guildId;
			this.name = name;
			this.webhook = webhook;
			this.message = message;
			this.lastUpdate = lastUpdate;
			this.lastUpdateLiveStream = lastUpdateLiveStream;
			this.channelId = channelId;
		}
	}
}