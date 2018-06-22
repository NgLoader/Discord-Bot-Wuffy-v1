package de.ngloader.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import de.ngloader.api.WuffyConfig;
import de.ngloader.api.WuffyServer;
import de.ngloader.api.command.Command;
import de.ngloader.api.command.Commands;
import de.ngloader.api.command.ICommandExecutor;
import de.ngloader.api.command.ICommandManager;
import de.ngloader.api.logger.ILogger;
import de.ngloader.api.util.ITickable;
import de.ngloader.api.util.Reactions;
import de.ngloader.common.logger.LoggerManager;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandManager implements ICommandManager, ITickable {

	private static final ILogger LOGGER = WuffyServer.getLogger();

	private static final String BOT_MENTION = String.format("<@%s>", Long.toString(WuffyServer.getJDA().getSelfUser().getIdLong()));

	private final Map<String, CommandInfo> commands = new HashMap<String, CommandInfo>();
	private final Queue<MessageReceivedEvent> commandQueue = new ConcurrentLinkedQueue<>();

	private long nextWarnMessage = 0;

	@Override
	public void update() {
		if(!this.commandQueue.isEmpty())
			this.processCommand(commandQueue.poll());
	}

	public void issueCommand(MessageReceivedEvent event) { //TODO create auto blacklist system and check it here before adding to queue
		if(this.commandQueue.size() < 100)
			this.commandQueue.add(event);
		else if(nextWarnMessage < System.currentTimeMillis()) {
			LOGGER.warn("Command queue is full.");
			nextWarnMessage = System.currentTimeMillis() + 15000;
		}
	}

	private void processCommand(MessageReceivedEvent event) {
		try {
			WuffyConfig config = WuffyServer.getConfigService().getConfig(WuffyConfig.class);

			if(event.getAuthor().isBot() ||
					(event.getGuild() != null && !event.getGuild().getTextChannelById(event.getChannel().getIdLong()).canTalk()) ||
					config.accountType == AccountType.CLIENT && !config.admins.contains(Long.toString(event.getAuthor().getIdLong())))
				return;

			var guild = WuffyServer.getGuild(event.getGuild().getIdLong());
			var user = WuffyServer.getUser(event.getAuthor().getIdLong());

			if(guild.isBlocked()) {
				guild.leave().queue();
				return;
			}

			System.out.println(guild.getPrefixes().stream().collect(Collectors.joining(", ")));

			if(user.isBlocked())
				return; //TODO add blocked message with reason and expire date

			var message = event.getMessage().getContentRaw();

			var mention = (user.isAdmin() || guild.isMention()) && message.startsWith(BOT_MENTION);

			if(mention)
				message = message.substring(0, BOT_MENTION.length()).trim();

			System.out.println(message);

			for(String prefix : guild.getPrefixes()) {
				if(message.startsWith(prefix)) {
					message = message.substring(0, prefix.length());

					var split = message.split("\\s+");

					var commandString = split[0].toLowerCase();
					var command = commands.get(commandString);

					if(command != null)
						switch (command.getExecutor().onCommand(event, guild, user, Arrays.copyOfRange(split, 1, split.length)).getCommandResult()) {
						case SUCCESS:
							LOGGER.debug("Command executor", String.format("Successful executed command '%s'", event.getMessage().getContentRaw()));
							event.getMessage().addReaction(Reactions.WHITE_CHECK_MARK.getAsUnicode()).queue();
							break;

						case SYNTAX:
							LOGGER.debug("Command executor", String.format("Syntax error by executed command '%s'", event.getMessage().getContentRaw()));
							command.getExecutor().onHelp(event, guild, user);
							break;

						case ERROR:
							LOGGER.debug("Command executor", String.format("Error by executed command '%s'", event.getMessage().getContentRaw()));
							event.getMessage().addReaction(Reactions.WARNING.getAsUnicode()).queue();
							break;
						}
					else {
						LOGGER.debug("Command executor", String.format("Command '%s' not found", event.getMessage().getContentRaw()));
						event.getMessage().addReaction(Reactions.GREY_QUESTION.getAsUnicode()).queue();
					}
					break;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		this.commandQueue.clear();
	}

	public void registerExecutor(ICommandExecutor executor) {
		Commands commands = executor.getClass().getAnnotation(Commands.class);
		if(commands != null)
			for(int i = 0; i < commands.value().length; i++)
				this.registerExecutor(commands.value()[i], executor);
		else
			this.registerExecutor(executor.getClass().getAnnotation(Command.class), executor);
	}

	public void registerExecutor(Command command, ICommandExecutor executor) {
		CommandInfo commandInfo = new CommandInfo(command, executor);

		for(String alias : Arrays.asList(commandInfo.command.aliases())) {
			this.commands.put(alias, commandInfo);

			LoggerManager.getLogger().debug("CommandManager", "Registered command '" + alias + "'");
		}
	}

	class CommandInfo {

		private Command command;
		private ICommandExecutor executor;

		public CommandInfo(Command command, ICommandExecutor executor) {
			this.command = command;
			this.executor = executor;
		}

		public Command getCommand() {
			return command;
		}

		public ICommandExecutor getExecutor() {
			return executor;
		}
	}
}