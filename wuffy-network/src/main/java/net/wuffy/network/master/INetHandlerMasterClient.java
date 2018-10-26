package net.wuffy.network.master;

import net.wuffy.network.INetHandler;
import net.wuffy.network.master.client.CPacketMasterSettings;
import net.wuffy.network.master.client.CPacketMasterShardUpdate;
import net.wuffy.network.master.client.CPacketMasterStatsUpdate;

public interface INetHandlerMasterClient extends INetHandler {

	public void handleMasterStatsUpdate(CPacketMasterStatsUpdate masterStatsUpdate);

	public void handleMasterShardUpdate(CPacketMasterShardUpdate masterShardUpdate);

	public void handleMasterGatewayBot(CPacketMasterSettings masterGatewayBot);
}