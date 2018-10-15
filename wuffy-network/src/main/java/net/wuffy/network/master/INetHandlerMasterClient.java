package net.wuffy.network.master;

import net.wuffy.network.INetHandler;
import net.wuffy.network.master.client.CPacketMasterStartShard;
import net.wuffy.network.master.client.CPacketMasterStatsUpdate;
import net.wuffy.network.master.client.CPacketMasterStopShard;

public interface INetHandlerMasterClient extends INetHandler {

	public void handleMasterStartShard(CPacketMasterStartShard masterStartShard);

	public void handleMasterStopShard(CPacketMasterStopShard masterStopShard);

	public void handleMasterStatsUpdate(CPacketMasterStatsUpdate masterStatsUpdate);
}