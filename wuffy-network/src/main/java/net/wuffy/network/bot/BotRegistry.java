package net.wuffy.network.bot;

import net.wuffy.network.bot.client.CPacketBotShardUpdate;
import net.wuffy.network.bot.client.CPacketBotStatsUpdate;
import net.wuffy.network.bot.server.SPacketBotHallo;
import net.wuffy.network.bot.server.SPacketBotStatsUpdate;
import net.wuffy.network.bot.server.SPacketBotStoppedShard;
import net.wuffy.network.bot.server.SPacketBotSystemUpdate;
import net.wuffy.network.universal.UniversalRegistry;

public final class BotRegistry extends UniversalRegistry {

	public static final BotRegistry INSTANCE = new BotRegistry();

	protected BotRegistry() {
		this.addPacket(EnumProtocolDirection.CLIENTBOUND, CPacketBotShardUpdate.class);
		this.addPacket(EnumProtocolDirection.CLIENTBOUND, CPacketBotStatsUpdate.class);

		this.addPacket(EnumProtocolDirection.SERVERBOUND, SPacketBotStoppedShard.class);
		this.addPacket(EnumProtocolDirection.SERVERBOUND, SPacketBotSystemUpdate.class);
		this.addPacket(EnumProtocolDirection.SERVERBOUND, SPacketBotStatsUpdate.class);
		this.addPacket(EnumProtocolDirection.SERVERBOUND, SPacketBotHallo.class);
	}
}