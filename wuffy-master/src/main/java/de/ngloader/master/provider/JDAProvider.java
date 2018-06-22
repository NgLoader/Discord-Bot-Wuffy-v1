package de.ngloader.master.provider;

import javax.security.auth.login.LoginException;

import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;

import de.ngloader.api.IJDAProvider;
import de.ngloader.api.WuffyConfig;
import de.ngloader.api.WuffyServer;
import de.ngloader.master.listener.MessageListener;
import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.utils.SessionControllerAdapter;

public class JDAProvider implements IJDAProvider {

	private IJDAProvider provider;

	public JDAProvider() {
		var config = WuffyServer.getConfigService().getConfig(WuffyConfig.class);

		try {
			if(config.accountType == AccountType.BOT) {
				var builder = new DefaultShardManagerBuilder()
					.setToken(config.token)
					.setGame(config.game.gameType == GameType.STREAMING ?
							Game.of(GameType.STREAMING, config.game.name, config.game.url) :
							Game.of(config.game.gameType, config.game.name))
					.setStatus(config.status)
					.setAudioSendFactory(new NativeAudioSendFactory(800))
					.setBulkDeleteSplittingEnabled(false)
					.setEnableShutdownHook(false)
					.setContextEnabled(false)
					.setSessionController(new SessionControllerAdapter())
					.addEventListeners(new MessageListener());

				if(config.sharding.enabled)
					builder.setShardsTotal(config.sharding.total)
						.setShards(config.sharding.shardIds);

				var shardManager = builder.build();

				this.provider = new IJDAProvider() {

					@Override
					public ShardManager getShardManager() {
						return shardManager;
					}

					@Override
					public JDA getJDA(int shardId) {
						return shardManager.getShardById(shardId);
					}
				};
			} else {

				var builder = new JDABuilder(config.accountType)
				.setToken(config.token)
				.setGame(config.game.gameType == GameType.STREAMING ?
						Game.of(GameType.STREAMING, config.game.name, config.game.url) :
						Game.of(config.game.gameType, config.game.name))
				.setStatus(config.status)
				.setAudioSendFactory(new NativeAudioSendFactory(800))
				.setBulkDeleteSplittingEnabled(false)
				.setEnableShutdownHook(false)
				.setContextEnabled(false)
				.setSessionController(new SessionControllerAdapter())
				.addEventListener(new MessageListener());

				var jda = builder.buildAsync();

				this.provider = new IJDAProvider() {
					
					@Override
					public ShardManager getShardManager() {
						return null;
					}
					
					@Override
					public JDA getJDA(int shardId) {
						return jda;
					}
				};
			}

			Runtime.getRuntime().addShutdownHook(new Thread() {
			});
		} catch (LoginException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public JDA getJDA(int shardId) {
		return provider.getJDA(shardId);
	}

	@Override
	public ShardManager getShardManager() {
		return provider.getShardManager();
	}
}