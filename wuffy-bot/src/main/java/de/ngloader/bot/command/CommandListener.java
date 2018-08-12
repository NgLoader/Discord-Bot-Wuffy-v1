package de.ngloader.bot.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.ngloader.bot.command.commands.Command;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.core.Core;
import de.ngloader.core.database.impl.IExtensionGuild;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.logger.Logger;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {

	private final CommandHandler handler;

	private final IExtensionGuild<?, ?> guildExtension;
	private final CommandRegestry regestry;
	private final CommandExecuter executer;

	public CommandListener(CommandHandler handler) {
		this.handler = handler;
		this.regestry = this.handler.getRegestry();
		this.executer = this.handler.getExecuter();

		this.guildExtension = this.handler.getCore().getStorageService().getExtension(IExtensionGuild.class);
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		Long timeExpierd = System.currentTimeMillis();

		if(!event.getGuild().isAvailable() || event.getAuthor().isBot() || event.getAuthor().isFake() || !event.getChannel().canTalk())
			return;

		String message = event.getMessage().getContentRaw();
		String[] split = message.split("\\s+");
		String[] args = Arrays.copyOfRange(split, 1, split.length);
		String alias = split[0].toLowerCase();

		WuffyGuild guild = (WuffyGuild) this.guildExtension.getGuild(event.getGuild());

		List<String> prefixes = new ArrayList<String>(guild.getPrefixes());

		if(System.getProperty("developerMode") != null)
			if(!this.handler.getCore().isAdmin(event.getAuthor()))
				return;
			else
				prefixes.clear();

		if(guild.isMention() || event.getMember().isOwner() || this.handler.getCore().isAdmin(event.getAuthor().getIdLong()))
			prefixes.add(String.format("<@%s>", Long.toString(event.getJDA().getSelfUser().getIdLong())));

		for(String prefix : prefixes) {
			if(alias.equalsIgnoreCase(prefix)) {
				alias = alias.substring(prefix.length());

				if(alias.isEmpty() && args.length > 0) {
					alias = args[0].toLowerCase();
					args = Arrays.copyOfRange(args, 1, args.length);
				}

				String wuffyAlias = alias;
				String[] wuffyArgs = args;

				this.executer.add(new Runnable() {

					@Override
					public void run() {
						try {
							Command command = CommandListener.this.regestry.getCommand(wuffyAlias);

							if(command != null) {
								Core core = CommandListener.this.handler.getCore();

								try {
									WuffyMessageRecivedEvent wuffyEvent = new WuffyMessageRecivedEvent(core, event.getJDA(), event.getResponseNumber(), event.getMessage());

									if(command.hasMemberPermission(wuffyEvent))
										if(command.hasBotPermission(wuffyEvent))
											if(!command.getSettings().nsfw() || command.isNSFWChannel(wuffyEvent))
												if(!command.getSettings().alpha() || command.isAlphaTesting(wuffyEvent))
													command.onGuild(wuffyEvent, wuffyAlias, wuffyArgs);

									Logger.debug("CommandExecutor", String.format("Command (%s) was executed in %sms",
											command.getClass().getSimpleName(),
											Long.toString(System.currentTimeMillis() - timeExpierd)));
								} catch(Exception e) {
									Logger.fatal("CommandExecuter", String.format("Failed to execute command \"%s\" with arguments \"%s\" on shard \"%s\".",
												command.getClass().getSimpleName(),
												String.join(", ", wuffyArgs),
												Integer.toString(guild.getJDA().getShardInfo().getShardId())),
											e);
								}
							} else {
								if(guild.getSelfMember().hasPermission(event.getChannel(), Permission.MESSAGE_ADD_REACTION))
									event.getMessage().addReaction("❔").queue();

								if(guild.isMessageDeleteExecuter() && guild.getSelfMember().hasPermission(event.getChannel(), Permission.MESSAGE_MANAGE))
									event.getMessage().delete().queueAfter(2, TimeUnit.SECONDS);
							}
						} catch(Exception e) {
							Logger.fatal("CommandListener", String.format("Failed to execute command \"%s\" on shard \"%s\".",
										wuffyAlias,
										Integer.toString(guild.getJDA().getShardInfo().getShardId())),
									e);
						}
					}
				});
				break;
			}
		}
	}

	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		Long timeExpierd = System.currentTimeMillis();

		if(event.getAuthor().isBot() || event.getAuthor().isFake())
			return;

		String message = event.getMessage().getContentRaw();
		String[] split = message.split("\\s+");
		String[] args = Arrays.copyOfRange(split, 1, split.length);
		String alias = split[0];

		String mention = String.format("<@%s>", Long.toString(event.getJDA().getSelfUser().getIdLong()));

		if(alias.startsWith(mention)) {
			alias = alias.substring(mention.length());

			if(alias.isEmpty() && args.length > 0)
				alias = args[0];
		}
		if(alias.startsWith("~"))
			alias = alias.substring(1);
		if(alias.isEmpty() && args.length > 0) {
			alias = args[0];
			args = Arrays.copyOfRange(args, 1, args.length);
		}

		String wuffyAlias = alias;
		String[] wuffyArgs = args;

		this.executer.add(new Runnable() {

			@Override
			public void run() {

				try {
					Command command = CommandListener.this.regestry.getCommand(wuffyAlias);

					if(command != null) {
						Core core = CommandListener.this.handler.getCore();

						try {
							WuffyMessageRecivedEvent wuffyEvent = new WuffyMessageRecivedEvent(core, event.getJDA(), event.getResponseNumber(), event.getMessage());

							if(command.getSettings().privateChatCommand()) {
								command.onPrivate(wuffyEvent, wuffyAlias, wuffyArgs);

								Logger.debug("CommandExecutor", String.format("Command (%s) was executed in %sms",
										command.getClass().getSimpleName(),
										Long.toString(System.currentTimeMillis() - timeExpierd)));
							} else
								event.getMessage().addReaction("❓").queue();
						} catch(Exception e) {
							Logger.fatal("CommandExecuter", String.format("Failed to execute command \"%s\" with arguments \"%s\" on shard \"%s\".",
									command.getClass().getSimpleName(),
									String.join(", ", wuffyArgs),
									Integer.toString(event.getJDA().getShardInfo().getShardId())),
									e);
						}
					} else
						event.getMessage().addReaction("❔").queue();
				} catch(Exception e) {
					Logger.fatal("CommandListener", String.format("Failed to execute command \"%s\" on shard \"%s\".", wuffyAlias, Integer.toString(event.getJDA().getShardInfo().getShardId())), e);
				}
			}
		});
	}

	public CommandHandler getHandler() {
		return this.handler;
	}
}