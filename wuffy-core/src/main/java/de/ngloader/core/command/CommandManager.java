package de.ngloader.core.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.ngloader.core.logger.Logger;
import de.ngloader.core.util.ITickable;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandManager implements ICommandManager, ITickable {

//	private static final String BOT_MENTION = /*String.format("<@%s>", WuffyServer.getConfigService().getConfig(WuffyConfig.class).mentionId);*/""; //TODO setBotMention

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
			Logger.warn("Command queue is full.");
			nextWarnMessage = System.currentTimeMillis() + 15000;
		}
	}

	private void processCommand(MessageReceivedEvent event) {
		try {
//			WuffyConfig config = WuffyServer.getConfigService().getConfig(WuffyConfig.class);
//
//			if(event.getAuthor().isBot() ||
//					(event.getGuild() != null && !event.getGuild().getTextChannelById(event.getChannel().getIdLong()).canTalk()) ||
//					config.accountType == AccountType.CLIENT && !config.admins.contains(Long.toString(event.getAuthor().getIdLong())))
//				return;
//
//			var guild = WuffyServer.getGuild(event.getJDA(), event.getGuild().getIdLong());
//			var user = WuffyServer.getUser(event.getJDA(), event.getAuthor().getIdLong());
//
//			if(guild.isBlocked()) {
//				guild.leave().queue();
//				return;
//			}
//
//			if(user.isBlocked())
//				return; //TODO add blocked message with reason and expire date
//
//			var message = event.getMessage().getContentRaw();
//
//			var mention = message.startsWith(BOT_MENTION);
//
//			if(mention && (!guild.isMention() && !user.isAdmin()))
//				return;
//
//			if(mention)
//				message = message.substring(BOT_MENTION.length()).trim();
//
//			for(String prefix : new String[0]) { //TODO SET new String[0] to guild.getPrefixes()
//				if(message.startsWith(prefix)) {
//					message = message.substring(prefix.length());
//
//					var split = message.split("\\s+");
//
//					var commandString = split[0].toLowerCase();
//					var command = commands.get(commandString);
//
//					if(command != null)
//						switch (command.getExecutor().onCommand(event, guild, user, Arrays.copyOfRange(split, 1, split.length)).getCommandResult()) {
//						case SUCCESS:
//							LOGGER.debug("Command executor", String.format("Successful executed command '%s'", event.getMessage().getContentRaw()));
//							event.getMessage().addReaction(Reactions.WHITE_CHECK_MARK.getAsUnicode()).queue();
//							break;
//
//						case SYNTAX:
//							LOGGER.debug("Command executor", String.format("Syntax error by executed command '%s'", event.getMessage().getContentRaw()));
//							command.getExecutor().onHelp(event, guild, user);
//							break;
//
//						case ERROR:
//							LOGGER.debug("Command executor", String.format("Error by executed command '%s'", event.getMessage().getContentRaw()));
//							event.getMessage().addReaction(Reactions.WARNING.getAsUnicode()).queue();
//							break;
//						}
//					else {
//						LOGGER.debug("Command executor", String.format("Command '%s' not found", event.getMessage().getContentRaw()));
//						event.getMessage().addReaction(Reactions.GREY_QUESTION.getAsUnicode()).queue();
//					}
//					break;
//				}
//			}
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

			Logger.debug("CommandManager", "Registered command '" + alias + "'");
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