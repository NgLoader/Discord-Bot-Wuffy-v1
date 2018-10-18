package net.wuffy.network.master.client;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.master.INetHandlerMasterClient;

public class CPacketMasterSettings implements Packet<INetHandlerMasterClient> {

	private String token;

	

	private GatewayBot gatewayBot;

	public CPacketMasterSettings() { }

	public CPacketMasterSettings(String token, GatewayBot gatewayBot) {
		this.token = token;
		this.gatewayBot = gatewayBot;
	}

	@Override
	public void read(PacketBuffer packetBuffer) throws IOException {
		this.token = packetBuffer.readString();
		this.gatewayBot = new GatewayBot(
				packetBuffer.readString(), 
				packetBuffer.readInt(),
				packetBuffer.readInt(),
				packetBuffer.readInt(),
				packetBuffer.readInt());
	}

	@Override
	public void write(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeString(this.token);
		packetBuffer.writeString(this.gatewayBot.getUrl());
		packetBuffer.writeInt(this.gatewayBot.getShards());
		packetBuffer.writeInt(this.gatewayBot.getSessionStartLimitTotal());
		packetBuffer.writeInt(this.gatewayBot.getSessionStartLimitRemaining());
		packetBuffer.writeInt(this.gatewayBot.getSessionStartLimitRestAfter());
	}

	@Override
	public void handle(INetHandlerMasterClient handler) {
		handler.handleMasterGatewayBot(this);
	}

	public String getToken() {
		return this.token;
	}

	public class GatewayBot {

		private String url;
		private int shards;
		private int sessionStartLimitTotal;
		private int sessionStartLimitRemaining;
		private int sessionStartLimitRestAfter;

		public GatewayBot(String url, int shards, int sessionStartLimitTotal, int sessionStartLimitRemaining, int sessionStartLimitRestAfter) {
			this.url = url;
			this.shards = shards;
			this.sessionStartLimitTotal = sessionStartLimitTotal;
			this.sessionStartLimitRemaining = sessionStartLimitRemaining;
			this.sessionStartLimitRestAfter = sessionStartLimitRestAfter;
		}

		public String getUrl() {
			return this.url;
		}

		public int getShards() {
			return this.shards;
		}

		public int getSessionStartLimitTotal() {
			return this.sessionStartLimitTotal;
		}

		public int getSessionStartLimitRemaining() {
			return this.sessionStartLimitRemaining;
		}

		public int getSessionStartLimitRestAfter() {
			return this.sessionStartLimitRestAfter;
		}
	}
}