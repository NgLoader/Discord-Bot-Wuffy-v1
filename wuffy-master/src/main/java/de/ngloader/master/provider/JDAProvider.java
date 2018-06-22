package de.ngloader.master.provider;

import javax.security.auth.login.LoginException;

import de.ngloader.api.IJDAProvider;
import de.ngloader.api.WuffyConfig;
import de.ngloader.api.WuffyServer;
import de.ngloader.master.ReadyListenerAdapter;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;

public class JDAProvider implements IJDAProvider {

	private JDA jda;

	public JDAProvider(ReadyListenerAdapter readyListenerAdapter) {
		WuffyConfig config = WuffyServer.getConfigService().getConfig(WuffyConfig.class);

		try {
			JDABuilder builder = new JDABuilder(config.accountType);
			builder.setToken(config.token);
			builder.addEventListener(readyListenerAdapter);

			if(config.sharding.enabled)
				builder.useSharding(config.sharding.id, config.sharding.total);

			builder.setGame(config.game.gameType == GameType.STREAMING ?
					Game.of(GameType.STREAMING, config.game.name, config.game.url) :
					Game.of(config.game.gameType, config.game.name));

			builder.setStatus(config.status);

			this.jda = builder.buildAsync();
		} catch (LoginException e) {
			e.printStackTrace();
		}
	}

	@Override
	public JDA getJDA() {
		return this.jda;
	}
}