package net.wuffy.bot;

import java.util.Arrays;
import java.util.stream.Collectors;

import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.wuffy.common.config.Config;
import net.wuffy.core.CoreConfig;
import net.wuffy.network.bot.client.CPacketBotSettings;

@Config(path = "./wuffy/bot.json", sourcePath = "/config/bot.json")
public class BotConfig extends CoreConfig {

	public void load(CPacketBotSettings packetBotSettings) {
		//Token
		this.token = packetBotSettings.getToken();

		//Admins
		this.admins = Arrays.asList(packetBotSettings.getAdmins()).stream().map(admin -> Long.parseLong(admin)).collect(Collectors.toList());

		//Instance info's
		this.instanceName = packetBotSettings.getInstanceName();
		this.instanceVersion = packetBotSettings.getInstanceVersion();

		//Bot status
		CPacketBotSettings.Status status = packetBotSettings.getStatus();
		this.status = status.getStatusType(OnlineStatus.class);

		this.game = new GameConfig();
		this.game.gameType = status.getGameType(GameType.class);
		this.game.name = status.getGameName();

		if(this.game.gameType == GameType.STREAMING)
			this.game.url = status.getGameUrl();
	}
}