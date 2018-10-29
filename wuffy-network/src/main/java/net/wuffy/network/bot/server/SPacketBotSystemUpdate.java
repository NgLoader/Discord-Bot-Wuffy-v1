package net.wuffy.network.bot.server;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.bot.INetHandlerBotServer;

/**
 * @author Ingrim4
 */
public class SPacketBotSystemUpdate implements Packet<INetHandlerBotServer> {

	private double cpuUsage;
	private double cpuAverage;
	private long freeMemory;
	private long freeSwap;
	private long freeDiskSpace;

	public SPacketBotSystemUpdate() { }

	public SPacketBotSystemUpdate(double cpuUsage, double cpuAverage, long freeMemory, long freeSwap, long freeDiskSpace) {
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
	public void handle(INetHandlerBotServer handler) {
		handler.handleBotSystemUpdate(this);
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
