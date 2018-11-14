package net.wuffy.common.util;

import java.io.IOException;

import com.google.gson.JsonSyntaxException;

import net.wuffy.common.Defaults;
import okhttp3.Headers;
import okhttp3.Request;

public class DiscordRequest {

	/**
	 * Request discord bot default settings
	 * 
	 * @see https://discordapp.com/developers/docs/topics/gateway#get-gateway-bot
	 * 
	 * @param token
	 * @return DiscordRequestGateway
	 * @throws JsonSyntaxException
	 * @throws IOException
	 */
	public static DiscordRequestGateway gatewayBot(String token) throws JsonSyntaxException, IOException {
		return GsonUtil.GSON.fromJson(
				WebRequestBuilder.request(
					new Request.Builder()
						.headers(Headers.of(
								"Authorization", String.format("Bot %s", token),
								"User-agent", "Wuffy"))
						.url(String.format("%s%s", Defaults.DISCORD_API, "gateway/bot"))
						.build()).body().string(), DiscordRequestGateway.class);
	}

	public class DiscordRequestGateway {
		public String url;
		public Integer shards;
		public DiscordRequestGatewaySettionStartLimit session_start_limit;

		public class DiscordRequestGatewaySettionStartLimit {
			public Integer total;
			public Integer remaining;
			public Integer reset_after;
		}
	}
}