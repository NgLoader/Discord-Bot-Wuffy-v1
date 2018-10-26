package net.wuffy.network.master.client;

import java.io.IOException;
import java.util.EnumSet;

import net.wuffy.common.util.EnumUtil;
import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.master.INetHandlerMasterClient;

public class CPacketMasterSettings implements Packet<INetHandlerMasterClient> {

	private static final EnumSet<EnumMasterSettings> ENUMSET_CHECK_TOKEN  = EnumSet.of(EnumMasterSettings.ALL, EnumMasterSettings.TOKEN);
	private static final EnumSet<EnumMasterSettings> ENUMSET_CHECK_ADMINS  = EnumSet.of(EnumMasterSettings.ALL, EnumMasterSettings.ADMINS);
	private static final EnumSet<EnumMasterSettings> ENUMSET_CHECK_STATUS  = EnumSet.of(EnumMasterSettings.ALL, EnumMasterSettings.STATUS);
	private static final EnumSet<EnumMasterSettings> ENUMSET_CHECK_GATEWAYBOT  = EnumSet.of(EnumMasterSettings.ALL, EnumMasterSettings.GATEWAYBOT);

	private EnumSet<EnumMasterSettings> types;

	private String token;

	private String[] admins;

	private Status status;

	private GatewayBot gatewayBot;

	public CPacketMasterSettings() { }

	public CPacketMasterSettings(String token, Status status, GatewayBot gatewayBot, EnumMasterSettings type, EnumMasterSettings... moreTypes) {
		this.token = token;
		this.status = status;
		this.gatewayBot = gatewayBot;
		this.types = EnumSet.of(type, moreTypes);
	}

	@Override
	public void read(PacketBuffer packetBuffer) throws IOException {
		this.types = packetBuffer.readEnumSet(EnumMasterSettings.class);

		//Token
		if(EnumUtil.contains(this.types, CPacketMasterSettings.ENUMSET_CHECK_TOKEN))
			this.token = packetBuffer.readString();

		//Admins
		if(EnumUtil.contains(this.types, CPacketMasterSettings.ENUMSET_CHECK_ADMINS)) {
			this.admins = new String[packetBuffer.readInt()];

			for (int i = 0; i < admins.length; i++)
				this.admins[i] = packetBuffer.readString();
		}

		//Status
		if(EnumUtil.contains(this.types, CPacketMasterSettings.ENUMSET_CHECK_STATUS))
		this.status = new Status(
				packetBuffer.readInt(),
				packetBuffer.readInt(),
				packetBuffer.readString(),
				packetBuffer.readString());

		//GatewayBot
		if(EnumUtil.contains(this.types, CPacketMasterSettings.ENUMSET_CHECK_GATEWAYBOT))
		this.gatewayBot = new GatewayBot(
				packetBuffer.readString(), 
				packetBuffer.readInt(),
				packetBuffer.readInt(),
				packetBuffer.readInt(),
				packetBuffer.readInt());
	}

	@Override
	public void write(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeEnumSet(this.types);

		//Token
		if(EnumUtil.contains(this.types, CPacketMasterSettings.ENUMSET_CHECK_TOKEN))
			packetBuffer.writeString(this.token);

		//Admins
		if(EnumUtil.contains(this.types, CPacketMasterSettings.ENUMSET_CHECK_ADMINS)) {
			packetBuffer.writeInt(this.admins.length);

			for(String admin : this.admins)
				packetBuffer.writeString(admin);
		}

		//Status
		if(EnumUtil.contains(this.types, CPacketMasterSettings.ENUMSET_CHECK_STATUS)) {
			packetBuffer.writeInt(this.status.statusType);
			packetBuffer.writeInt(this.status.gameType);
			packetBuffer.writeString(this.status.gameName);
			packetBuffer.writeString(this.status.gameUrl);
		}

		//GatewayBot
		if(EnumUtil.contains(this.types, CPacketMasterSettings.ENUMSET_CHECK_GATEWAYBOT)) {
			packetBuffer.writeString(this.gatewayBot.url);
			packetBuffer.writeInt(this.gatewayBot.shards);
			packetBuffer.writeInt(this.gatewayBot.sessionStartLimitTotal);
			packetBuffer.writeInt(this.gatewayBot.sessionStartLimitRemaining);
			packetBuffer.writeInt(this.gatewayBot.sessionStartLimitRestAfter);
		}
	}

	@Override
	public void handle(INetHandlerMasterClient handler) {
		handler.handleMasterGatewayBot(this);
	}

	public String getToken() {
		return this.token;
	}

	public Status getStatus() {
		return this.status;
	}

	public GatewayBot getGatewayBot() {
		return this.gatewayBot;
	}

	public enum EnumMasterSettings {
		ALL, TOKEN, ADMINS, STATUS, GATEWAYBOT
	}

	public class Status {

		private int statusType;
		private int gameType;
		private String gameName;
		private String gameUrl;

		public Status(int statusType, int gameType, String gameName, String gameUrl) {
			this.statusType = statusType;
			this.gameType = gameType;
			this.gameName = gameName;
			this.gameUrl = gameUrl;
		}

		public <T extends Enum<T>> T getStatusType(Class<T> clazz) { //I don't import JDA for this enum class!
			return clazz.getEnumConstants()[this.statusType];
		}

		public <T extends Enum<T>> T getGameType(Class<T> clazz) { //I don't import JDA for this enum class!
			return clazz.getEnumConstants()[this.gameType];
		}

		public String getGameName() {
			return this.gameName;
		}

		public String getGameUrl() {
			return this.gameUrl;
		}
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