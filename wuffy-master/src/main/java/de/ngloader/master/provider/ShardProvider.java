package de.ngloader.master.provider;

import javax.security.auth.login.LoginException;

import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;

import de.ngloader.api.IShardProvider;
import de.ngloader.api.WuffyConfig;
import de.ngloader.api.WuffyServer;
import de.ngloader.master.ReadyListenerAdapter;
import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.utils.SessionControllerAdapter;

public class ShardProvider implements IShardProvider {

	private ShardManager shardManager;

	public ShardProvider(ReadyListenerAdapter readyListenerAdapter) {
		WuffyConfig config = WuffyServer.getConfigService().getConfig(WuffyConfig.class);

		try {
			DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder()
				.setToken(config.token)
				.setShardsTotal(config.sharding.total)
				.setGame(config.game.gameType == GameType.STREAMING ?
						Game.of(GameType.STREAMING, config.game.name, config.game.url) :
						Game.of(config.game.gameType, config.game.name))
				.setStatus(config.status)
				.setAudioSendFactory(new NativeAudioSendFactory(800))
				.setBulkDeleteSplittingEnabled(false)
				.setEnableShutdownHook(false)
				.setContextEnabled(false)
				.setSessionController(new SessionControllerAdapter())
				.addEventListeners(readyListenerAdapter);

			this.shardManager = builder.build();

			Runtime.getRuntime().addShutdownHook(new Thread() {
			});
		} catch (LoginException | IllegalArgumentException e) {
			e.printStackTrace();
		}

//		try {
//			JDABuilder builder = new JDABuilder(config.accountType);
//			builder.setToken(config.token);
//			builder.addEventListener(readyListenerAdapter);
//
//			if(config.sharding.enabled)
//				builder.useSharding(config.sharding.id, config.sharding.total);
//
//			builder.setGame(config.game.gameType == GameType.STREAMING ?
//					Game.of(GameType.STREAMING, config.game.name, config.game.url) :
//					Game.of(config.game.gameType, config.game.name));
//
//			builder.setStatus(config.status);
//
//			this.jda = builder.buildAsync();
//		} catch (LoginException e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public JDA getJDA() {
		return this.shardManager.getApplicationInfo().getJDA();
	}

	@Override
	public ShardManager getShardManager() {
		return shardManager;
	}
}