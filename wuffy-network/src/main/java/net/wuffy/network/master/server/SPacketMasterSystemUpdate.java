package net.wuffy.network.master.server;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.master.INetHandlerMasterServer;

/**
 * @author Ingrim4
 */
public class SPacketMasterSystemUpdate implements Packet<INetHandlerMasterServer> {

	private double cpuUsage;
	private double cpuAverage;
	private long freeMemory;
	private long freeSwap;
	private long freeDiskSpace;

	public SPacketMasterSystemUpdate() { }

	public SPacketMasterSystemUpdate(double cpuUsage, double cpuAverage, long freeMemory, long freeSwap, long freeDiskSpace) {
		this.cpuUsage = cpuUsage;
		this.cpuAverage = cpuAverage;
		this.freeMemory = freeMemory;
		this.freeSwap = freeSwap;
		this.freeDiskSpace = freeDiskSpace;
	}

	@Override
	public void read(PacketBuffer packetBuffer) throws IOException {
		this.cpuUsage = packetBuffer.readDouble();
		this.cpuAverage = packetBuffer.readDouble();
		this.freeMemory = packetBuffer.readVarLong();
		this.freeSwap = packetBuffer.readVarLong();
		this.freeDiskSpace = packetBuffer.readVarLong();
	}

	@Override
	public void write(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeDouble(this.cpuUsage);
		packetBuffer.writeDouble(this.cpuAverage);
		packetBuffer.writeVarLong(this.freeMemory);
		packetBuffer.writeVarLong(this.freeSwap);
		packetBuffer.writeVarLong(this.freeDiskSpace);
	}

	@Override
	public void handle(INetHandlerMasterServer handler) {
		handler.handleMasterSystemUpdate(this);
	}

	public double getCpuUsage() {
		return cpuUsage;
	}

	public double getCpuAverage() {
		return cpuAverage;
	}

	public long getFreeMemory() {
		return freeMemory;
	}

	public long getFreeSwap() {
		return freeSwap;
	}

	public long getFreeDiskSpace() {
		return freeDiskSpace;
	}
}
