package net.wuffy.network.bot.client;

import java.io.IOException;
import java.util.EnumSet;

import net.wuffy.common.util.EnumUtil;
import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.bot.INetHandlerBotClient;

public class CPacketBotSettings implements Packet<INetHandlerBotClient> {

	private static final EnumSet<EnumMasterSettings> ENUMSET_CHECK_TOKEN = EnumSet.of(EnumMasterSettings.ALL, EnumMasterSettings.TOKEN);
	private static final EnumSet<EnumMasterSettings> ENUMSET_CHECK_ADMINS = EnumSet.of(EnumMasterSettings.ALL, EnumMasterSettings.ADMINS);
	private static final EnumSet<EnumMasterSettings> ENUMSET_CHECK_STATUS = EnumSet.of(EnumMasterSettings.ALL, EnumMasterSettings.STATUS);
	private static final EnumSet<EnumMasterSettings> ENUMSET_CHECK_GATEWAYBOT = EnumSet.of(EnumMasterSettings.ALL, EnumMasterSettings.GATEWAYBOT);
	private static final EnumSet<EnumMasterSettings> ENUMSET_CHECK_INSTANCENAME = EnumSet.of(EnumMasterSettings.ALL, EnumMasterSettings.INSTANCENAME);
	private static final EnumSet<EnumMasterSettings> ENUMSET_CHECK_INSTANCEVERSION = EnumSet.of(EnumMasterSettings.ALL, EnumMasterSettings.INSTANCEVERSION);
	private static final EnumSet<EnumMasterSettings> ENUMSET_CHECK_DATABASE = EnumSet.of(EnumMasterSettings.ALL, EnumMasterSettings.DATABASE);

	private EnumSet<EnumMasterSettings> types;

	private String token;

	private String[] admins;

	private Status status;

	private GatewayBot gatewayBot;

	private String instanceName;
	private String instanceVersion;

	private Database database;

	public CPacketBotSettings() { }

	public CPacketBotSettings(String token, Status status, GatewayBot gatewayBot,
			String instanceName, String instanceVersion,
			Database database,
			EnumMasterSettings type, EnumMasterSettings... moreTypes) {
		this.token = token;
		this.status = status;
		this.gatewayBot = gatewayBot;
		this.instanceName = instanceName;
		this.instanceVersion = instanceVersion;
		this.database = database;
		this.types = EnumSet.of(type, moreTypes);
	}

	@Override
	public void read(PacketBuffer packetBuffer) throws IOException {
		this.types = packetBuffer.readEnumSet(EnumMasterSettings.class);

		//Token
		if(EnumUtil.contains(this.types, CPacketBotSettings.ENUMSET_CHECK_TOKEN))
			this.token = packetBuffer.readString();

		//Admins
		if(EnumUtil.contains(this.types, CPacketBotSettings.ENUMSET_CHECK_ADMINS)) {
			this.admins = new String[packetBuffer.readInt()];

			for (int i = 0; i < admins.length; i++)
				this.admins[i] = packetBuffer.readString();
		}

		//Status
		if(EnumUtil.contains(this.types, CPacketBotSettings.ENUMSET_CHECK_STATUS))
			this.status = new Status(
					packetBuffer.readInt(),
					packetBuffer.readInt(),
					packetBuffer.readString(),
					packetBuffer.readString());

		//GatewayBot
		if(EnumUtil.contains(this.types, CPacketBotSettings.ENUMSET_CHECK_GATEWAYBOT))
			this.gatewayBot = new GatewayBot(
					packetBuffer.readString(), 
					packetBuffer.readInt(),
					packetBuffer.readInt(),
					packetBuffer.readInt(),
					packetBuffer.readInt());

		//InstanceName
		if(EnumUtil.contains(this.types, CPacketBotSettings.ENUMSET_CHECK_INSTANCENAME))
			this.instanceName = packetBuffer.readString();

		//InstanceVersion
		if(EnumUtil.contains(this.types, CPacketBotSettings.ENUMSET_CHECK_INSTANCEVERSION))
			this.instanceVersion = packetBuffer.readString();

		//Database
		if(EnumUtil.contains(this.types, CPacketBotSettings.ENUMSET_CHECK_DATABASE))
			this.database = new Database(
					packetBuffer.readString(),
					packetBuffer.readInt(),
					packetBuffer.readString(),
					packetBuffer.readString(),
					packetBuffer.readString(),
					packetBuffer.readString(),
					new DatabaseSsl(
							packetBuffer.readBoolean(),
							packetBuffer.readBoolean(),
							packetBuffer.readString(),
							packetBuffer.readString(),
							packetBuffer.readString(),
							packetBuffer.readString()));
	}

	@Override
	public void write(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeEnumSet(this.types);

		//Token
		if(EnumUtil.contains(this.types, CPacketBotSettings.ENUMSET_CHECK_TOKEN))
			packetBuffer.writeString(this.token);

		//Admins
		if(EnumUtil.contains(this.types, CPacketBotSettings.ENUMSET_CHECK_ADMINS)) {
			packetBuffer.writeInt(this.admins.length);

			for(String admin : this.admins)
				packetBuffer.writeString(admin);
		}

		//Status
		if(EnumUtil.contains(this.types, CPacketBotSettings.ENUMSET_CHECK_STATUS)) {
			packetBuffer.writeInt(this.status.statusType);
			packetBuffer.writeInt(this.status.gameType);
			packetBuffer.writeString(this.status.gameName);
			packetBuffer.writeString(this.status.gameUrl);
		}

		//GatewayBot
		if(EnumUtil.contains(this.types, CPacketBotSettings.ENUMSET_CHECK_GATEWAYBOT)) {
			packetBuffer.writeString(this.gatewayBot.url);
			packetBuffer.writeInt(this.gatewayBot.shards);
			packetBuffer.writeInt(this.gatewayBot.sessionStartLimitTotal);
			packetBuffer.writeInt(this.gatewayBot.sessionStartLimitRemaining);
			packetBuffer.writeInt(this.gatewayBot.sessionStartLimitRestAfter);
		}

		//InstanceName
		if(EnumUtil.contains(this.types, CPacketBotSettings.ENUMSET_CHECK_INSTANCENAME))
			packetBuffer.writeString(this.instanceName);

		//InstanceVersion
		if(EnumUtil.contains(this.types, CPacketBotSettings.ENUMSET_CHECK_INSTANCEVERSION))
			packetBuffer.writeString(this.instanceVersion);

		//Database
		if(EnumUtil.contains(this.types, CPacketBotSettings.ENUMSET_CHECK_DATABASE)) {
			packetBuffer.writeString(this.database.address);
			packetBuffer.writeInt(this.database.port);
			packetBuffer.writeString(this.database.username);
			packetBuffer.writeString(this.database.password);
			packetBuffer.writeString(this.database.database);
			packetBuffer.writeString(this.database.collectionPrefix);
			packetBuffer.writeBoolean(this.database.ssl.enabled);
			packetBuffer.writeBoolean(this.database.ssl.invalidHostNames);
			packetBuffer.writeString(this.database.ssl.trustStoreFile);
			packetBuffer.writeString(this.database.ssl.trustStorePasswordFile);
			packetBuffer.writeString(this.database.ssl.keyStoreFile);
			packetBuffer.writeString(this.database.ssl.keyStorePassword);
		}
	}

	@Override
	public void handle(INetHandlerBotClient handler) {
		handler.handleBotGatewayBot(this);
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

	public String[] getAdmins() {
		return this.admins;
	}

	public Database getDatabase() {
		return this.database;
	}

	public String getInstanceName() {
		return this.instanceName;
	}

	public String getInstanceVersion() {
		return this.instanceVersion;
	}

	public boolean isContainsType(EnumMasterSettings type) {
		return this.types.contains(type) || this.types.contains(EnumMasterSettings.ALL);
	}

	public enum EnumMasterSettings {
		ALL, TOKEN, ADMINS, STATUS, GATEWAYBOT, INSTANCENAME, INSTANCEVERSION, DATABASE
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

	public class Database {

		private String address;
		private Integer port;
		private String username;
		private String password;
		private String database;
		private String collectionPrefix;
		private DatabaseSsl ssl;

		public Database(String address, Integer port, String username, String password, String database, String collectionPrefix, DatabaseSsl ssl) {
			this.address = address;
			this.port = port;
			this.username = username;
			this.password = password;
			this.database = database;
			this.collectionPrefix = collectionPrefix;
			this.ssl = ssl;
		}

		public String getAddress() {
			return this.address;
		}

		public Integer getPort() {
			return this.port;
		}

		public String getUsername() {
			return this.username;
		}

		public String getPassword() {
			return this.password;
		}

		public String getDatabase() {
			return this.database;
		}

		public String getCollectionPrefix() {
			return this.collectionPrefix;
		}

		public DatabaseSsl getSSL() {
			return this.ssl;
		}
	}

	public class DatabaseSsl {

		private Boolean enabled;
		private Boolean invalidHostNames;
		private String trustStoreFile;
		private String trustStorePasswordFile;
		private String keyStoreFile;
		private String keyStorePassword;

		public DatabaseSsl(Boolean enabled, Boolean invalidHostNames, String trustStoreFile, String trustStorePasswordFile, String keyStoreFile, String keyStorePassword) {
			this.enabled = enabled;
			this.invalidHostNames = invalidHostNames;
			this.trustStoreFile = trustStoreFile;
			this.trustStorePasswordFile = trustStorePasswordFile;
			this.keyStoreFile = keyStoreFile;
			this.keyStorePassword = keyStorePassword;
		}

		public Boolean getEnabled() {
			return this.enabled;
		}

		public Boolean getInvalidHostNames() {
			return this.invalidHostNames;
		}

		public String getTrustStoreFile() {
			return this.trustStoreFile;
		}

		public String getTrustStorePasswordFile() {
			return this.trustStorePasswordFile;
		}

		public String getKeyStoreFile() {
			return this.keyStoreFile;
		}

		public String getKeyStorePassword() {
			return this.keyStorePassword;
		}
	}
}