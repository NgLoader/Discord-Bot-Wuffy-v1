package net.wuffy.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

public class WebhookMessageBuilder {

	public static WebhookMessageBuilder fromJson(String json) {
		if(json != null)
			try {
				return GsonUtil.GSON.fromJson(json, WebhookMessageBuilder.class);
			} catch(Exception e) {
				e.printStackTrace();
			}

		return null;
	}

	private String content;
	private String username;
	private String avatar_url;
	private boolean tts;
	private List<MessageEmbed> embeds = new ArrayList<MessageEmbed>();

	public WebhookMessageBuilder setContent(String content) {
		this.content = content;

		return this;
	}

	public WebhookMessageBuilder setUsername(String username) {
		this.username = username;

		return this;
	}

	public WebhookMessageBuilder setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;

		return this;
	}

	public WebhookMessageBuilder setTts(boolean tts) {
		this.tts = tts;

		return this;
	}

	public WebhookMessageBuilder setEmbeds(List<MessageEmbed> embeds) {
		this.embeds = embeds;

		return this;
	}

	public WebhookMessageBuilder addEmbed(MessageEmbed embed) {
		this.embeds.add(embed);

		return this;
	}

	public WebhookMessageBuilder addEmbed(EmbedBuilder builder) {
		this.embeds.add(builder.build());

		return this;
	}

	public String build() {
		JSONObject object = new JSONObject();

		if(this.content != null)
			object.put("content", this.content);
		if(this.username != null)
			object.put("username", this.username);
		if(this.avatar_url != null)
			object.put("avatar_url", this.avatar_url);
		if(this.tts)
			object.put("tts", this.tts);
		if(!this.embeds.isEmpty())
			object.put("embeds", new JSONArray(this.embeds.stream().map(embed -> embed.toJSONObject()).collect(Collectors.toList())));

		return object.toString();
	}
}