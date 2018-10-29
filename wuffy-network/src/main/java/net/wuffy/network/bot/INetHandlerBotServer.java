package net.wuffy.network.bot;

import net.wuffy.network.INetHandler;
import net.wuffy.network.bot.server.SPacketBotHallo;
import net.wuffy.network.bot.server.SPacketBotStatsUpdate;
import net.wuffy.network.bot.server.SPacketBotStoppedShard;
import net.wuffy.network.bot.server.SPacketBotSystemUpdate;

public interface INetHandlerBotServer extends INetHandler {

	public void handleBotInit(SPacketBotHallo botInit);

	public void handleBotStatsUpdate(SPacketBotStatsUpdate botStatsUpdate);

	public void handleBotSystemUpdate(SPacketBotSystemUpdate botSystemUpdate);

	public void handleBotStoppedShard(SPacketBotStoppedShard botStoppedShard);
}