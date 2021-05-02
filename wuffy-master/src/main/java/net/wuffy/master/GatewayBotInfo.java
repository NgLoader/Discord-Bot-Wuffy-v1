package net.wuffy.master;

import java.util.concurrent.atomic.AtomicInteger;

import net.wuffy.common.logger.Logger;
import net.wuffy.common.util.DiscordRequest;
import net.wuffy.common.util.DiscordRequest.DiscordRequestGateway;

public class GatewayBotInfo {

	private static String discord_wss_url;
	private static AtomicInteger discord_shards;
	private static AtomicInteger discord_session_start_limit_total;
	private static AtomicInteger discord_session_start_limit_remaining;
	private static AtomicInteger discord_session_start_limit_reset_after;

	static {
		GatewayBotInfo.refresh();
	}

	public static void refresh() {
		while(true) {
			try {
				DiscordRequestGateway gateway = DiscordRequest.gatewayBot(Master.getInstance().getConfig().botToken);

				GatewayBotInfo.discord_wss_url = gateway.url;
				GatewayBotInfo.discord_shards = new AtomicInteger(gateway.shards);
				GatewayBotInfo.discord_session_start_limit_total = new AtomicInteger(gateway.session_start_limit.total);
				GatewayBotInfo.discord_session_start_limit_remaining = new AtomicInteger(gateway.session_start_limit.remaining);
				GatewayBotInfo.discord_session_start_limit_reset_after = new AtomicInteger(gateway.session_start_limit.reset_after);
				break;
			} catch (Exception e) {
				Logger.fatal("ShardHandler", "Failed by requesting gateway! Next try in 30 seconds.", e);
				try {
					Thread.sleep(30000);
				} catch (InterruptedException ex2) {
					ex2.printStackTrace();
				}
			}
		}
	}

	public static String getDiscord_wss_url() {
		return GatewayBotInfo.discord_wss_url;
	}

	public static AtomicInteger getDiscord_shards() {
		return GatewayBotInfo.discord_shards;
	}

	public static AtomicInteger getDiscord_session_start_limit_total() {
		return GatewayBotInfo.discord_session_start_limit_total;
	}

	public static AtomicInteger getDiscord_session_start_limit_remaining() {
		return GatewayBotInfo.discord_session_start_limit_remaining;
	}

	public static AtomicInteger getDiscord_session_start_limit_reset_after() {
		return GatewayBotInfo.discord_session_start_limit_reset_after;
	}
}