package net.wuffy.network.master;

import net.wuffy.network.INetHandler;
import net.wuffy.network.master.server.SPacketMasterHallo;
import net.wuffy.network.master.server.SPacketMasterStatsUpdate;
import net.wuffy.network.master.server.SPacketMasterStoppedShard;
import net.wuffy.network.master.server.SPacketMasterSystemUpdate;

public interface INetHandlerMasterServer extends INetHandler {

	public void handleMasterInit(SPacketMasterHallo masterInit);

	public void handleMasterStatsUpdate(SPacketMasterStatsUpdate masterStatsUpdate);

	public void handleMasterSystemUpdate(SPacketMasterSystemUpdate masterSystemUpdate);

	public void handleMasterStoppedShard(SPacketMasterStoppedShard masterStoppedShard);
}