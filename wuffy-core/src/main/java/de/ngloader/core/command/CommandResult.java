package de.ngloader.core.command;

public class CommandResult {

	public static final CommandResult SUCCESS = new CommandResult(EnumCommandResult.SUCCESS, null);
	public static final CommandResult SYNTAX = new CommandResult(EnumCommandResult.SYNTAX, null);

	public static CommandResult error(Throwable throwable) {
		return new CommandResult(EnumCommandResult.ERROR, throwable);
	}

	private EnumCommandResult commandResult;
	private Throwable throwable;

	public CommandResult(EnumCommandResult commandResult, Throwable throwable) {
		this.commandResult = commandResult;
		this.throwable = throwable;
	}

	public EnumCommandResult getCommandResult() {
		return commandResult;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public static enum EnumCommandResult {
		SUCCESS, SYNTAX, ERROR;
	}
}