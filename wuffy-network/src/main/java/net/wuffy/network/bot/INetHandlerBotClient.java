package net.wuffy.network.bot;

import net.wuffy.network.INetHandler;
import net.wuffy.network.bot.client.CPacketBotSettings;
import net.wuffy.network.bot.client.CPacketBotShardUpdate;
import net.wuffy.network.bot.client.CPacketBotStatsUpdate;

public interface INetHandlerBotClient extends INetHandler {

	public void handleBotStatsUpdate(CPacketBotStatsUpdate botStatsUpdate);

	public void handleBotShardUpdate(CPacketBotShardUpdate botShardUpdate);

	public void handleBotGatewayBot(CPacketBotSettings botGatewayBot);
}