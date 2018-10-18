package net.wuffy.network.master.server;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.master.INetHandlerMasterServer;

public class SPacketMasterHallo implements Packet<INetHandlerMasterServer> {

	private long startupTime;

	private String os;
	private String arch;
	private String javaVersion;

	private int cpuCores;
	private long totalMemory;
	private long totalSwap;
	private long totalDiskSpace;

	public SPacketMasterHallo() { }

	public SPacketMasterHallo(long startupTime) {
		this.startupTime = startupTime;
	}

	@Override
	public void read(PacketBuffer packetBuffer) throws IOException {
		this.startupTime = packetBuffer.readLong();
		this.os = packetBuffer.readString();
		this.arch = packetBuffer.readString();
		this.javaVersion = packetBuffer.readString();
		this.cpuCores = packetBuffer.readInt();
		this.totalMemory = packetBuffer.readLong();
		this.totalSwap = packetBuffer.readLong();
		this.totalDiskSpace = packetBuffer.readLong();
	}

	@Override
	public void write(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeLong(this.startupTime);
		packetBuffer.writeString(this.os);
		packetBuffer.writeString(this.arch);
		packetBuffer.writeString(this.javaVersion);
		packetBuffer.writeInt(this.cpuCores);
		packetBuffer.writeLong(this.totalMemory);
		packetBuffer.writeLong(this.totalSwap);
		packetBuffer.writeLong(this.totalDiskSpace);
	}

	@Override
	public void handle(INetHandlerMasterServer handler) {
		handler.handleMasterInit(this);
	}

	public long getStartupTime() {
		return this.startupTime;
	}

	public String getOs() {
		return this.os;
	}

	public String getArch() {
		return this.arch;
	}

	public String getJavaVersion() {
		return this.javaVersion;
	}

	public int getCpuCores() {
		return this.cpuCores;
	}

	public long getTotalMemory() {
		return this.totalMemory;
	}

	public long getTotalSwap() {
		return this.totalSwap;
	}

	public long getTotalDiskSpace() {
		return this.totalDiskSpace;
	}
}