package de.ngloader.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.ngloader.api.WuffyServer;
import de.ngloader.api.command.Command;
import de.ngloader.api.command.Commands;
import de.ngloader.api.command.ICommandExecutor;
import de.ngloader.api.command.ICommandManager;
import de.ngloader.api.logger.ILogger;
import de.ngloader.api.util.ITickable;
import de.ngloader.api.util.Reactions;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandManager implements ICommandManager, ITickable {

	private static final ILogger LOGGER = WuffyServer.getLogger();

	private static final String BOT_MENTION = String.format("<@%s>", Long.toString(WuffyServer.getShardProvider().getJDA().getSelfUser().getIdLong()));

	private final Map<String, Map<String, CommandInfo>> commands = new HashMap<String, Map<String, CommandInfo>>();
	private final Queue<GuildMessageReceivedEvent> commandQueue = new ConcurrentLinkedQueue<>();

	private long nextWarnMessage = 0;

	@Override
	public void update() {
		if(!this.commandQueue.isEmpty())
			this.processCommand(commandQueue.poll());
	}

	public void issueCommand(GuildMessageReceivedEvent event) { //TODO create auto blacklist system and check it here before adding to queue
		if(this.commandQueue.size() < 100)
			this.commandQueue.add(event);
		else if(nextWarnMessage < System.currentTimeMillis()) {
			LOGGER.warn("Command queue is full.");
			nextWarnMessage = System.currentTimeMillis() + 15000;
		}	
	}

	private void processCommand(GuildMessageReceivedEvent event) {
		try {
			if(event.getAuthor().isBot() || !event.getChannel().canTalk())
				return;

			var guild = WuffyServer.getGuild(event.getGuild().getIdLong());
			var user = WuffyServer.getUser(event.getAuthor().getIdLong());

			if(guild.isBlocked()) {
				guild.leave().queue();
				return;
			}

			if(user.isBlocked())
				return; //TODO add blocked message with reason and expire date

			var message = event.getMessage().getContentRaw();

			var mention = message.startsWith(BOT_MENTION);

			var locale = user.getLocale() != null ? user.getLocale() : guild.getLocale();

			if(())

			if(commands.containsKey(locale)) {
				var split = message.split("\\s+");

				var commandString = split[0].toLowerCase();
				var command = commands.get(locale).get(commandString);

				if(command != null)
					switch (command.getExecutor().onCommand(event, guild, user, Arrays.copyOfRange(split, 1, split.length)).getCommandResult()) {
					case SUCCESS:
						LOGGER.debug("Command executor", String.format("Successful executed command '%s'", message));
						break;

					case SYNTAX:
						LOGGER.debug("Command executor", String.format("Syntax error by executed command '%s'", message));
						command.getExecutor().onHelp(event, guild, user);
						break;

					case ERROR:
						LOGGER.debug("Command executor", String.format("Error by executed command '%s'", message));
						event.getMessage().addReaction(Reactions.WARNING.getAsUnicode()).queue();
						break;
					}
				else {
					LOGGER.debug("Command executor", String.format("Command '%s' not found", message));
					event.getMessage().addReaction(Reactions.GREY_QUESTION.getAsUnicode()).queue();
				}
			} else
				LOGGER.info(String.format("Locale '%s' not exist. Used by user '%s' in guild '%s'.", locale, event.getGuild().getId(), event.getAuthor().getId()));
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

		//TODO register commands with langauge
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