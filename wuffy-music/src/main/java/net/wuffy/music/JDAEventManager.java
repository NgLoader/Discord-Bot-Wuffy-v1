package net.wuffy.music;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.ShutdownEvent;
import net.dv8tion.jda.core.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.hooks.IEventManager;
import net.wuffy.common.logger.Logger;
import net.wuffy.music.audio.WuffyAudioGuild;
import net.wuffy.music.audio.WuffyAudioHandler;

public class JDAEventManager implements IntFunction<IEventManager> {

	private IEventManager eventManager = new IEventManager() {

		private List<Object> registeredListeners = new ArrayList<Object>();

		@Override
		public void unregister(Object listener) { }

		@Override
		public void register(Object listener) { }

		@Override
		public void handle(Event event) {
			if(event instanceof ReadyEvent) { //TODO find a better way (JDA automaticly starting one shard...)
				ReadyEvent readyEvent = (ReadyEvent) event;

				Logger.debug("JDAHandler", String.format("Shard \"%s\" started.", readyEvent.getJDA().getShardInfo().getShardId()));
			} else if(event instanceof ShutdownEvent) {
				ShutdownEvent shutdownEvent = (ShutdownEvent) event;

				Logger.debug("JDAHandler", String.format("Shard \"%s\" stopped.", shutdownEvent.getJDA().getShardInfo().getShardId()));
			} else if(event instanceof TextChannelDeleteEvent) {
				TextChannelDeleteEvent textChannelDeleteEvent = (TextChannelDeleteEvent) event;
				WuffyAudioGuild audioGuild = WuffyAudioHandler.getGuild(textChannelDeleteEvent.getGuild().getIdLong());

				if(audioGuild != null && audioGuild.getTextChannel().getIdLong() == textChannelDeleteEvent.getChannel().getIdLong())
					audioGuild.setTextChannel(null);
			} else if(event instanceof GenericGuildEvent) {
				WuffyAudioGuild audioGuild = WuffyAudioHandler.getGuild(((GenericGuildEvent) event).getGuild().getIdLong());

				if(audioGuild != null)
					audioGuild.getEventManager().onEvent(event);
			}
		}

		@Override
		public List<Object> getRegisteredListeners() {
			return this.registeredListeners;
		}
	};

	@Override
	public IEventManager apply(int value) {
		return this.eventManager;
	}
}