package net.wuffy.core.console;

/**
 * @author Ingrim4
 */
public class ConsoleCommandResult {

	public static final ConsoleCommandResult SUCCESS = new ConsoleCommandResult(EnumConsoleCommandResult.SUCCESS, null);
	public static final ConsoleCommandResult SYNTAX = new ConsoleCommandResult(EnumConsoleCommandResult.SYNTAX, null);

	public static ConsoleCommandResult error(Throwable throwable) {
		return new ConsoleCommandResult(EnumConsoleCommandResult.ERROR, throwable);
	}

	private EnumConsoleCommandResult commandResult;
	private Throwable throwable;

	public ConsoleCommandResult(EnumConsoleCommandResult commandResult, Throwable throwable) {
		this.commandResult = commandResult;
		this.throwable = throwable;
	}

	public EnumConsoleCommandResult getCommandResult() {
		return commandResult;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	enum EnumConsoleCommandResult {
		SUCCESS, SYNTAX, ERROR;
	}
}