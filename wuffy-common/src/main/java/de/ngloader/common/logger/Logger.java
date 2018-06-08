package de.ngloader.common.logger;

/**
 * @author Ingrim4
 */
public class Logger implements ILogger {

	@Override
	public final void log(Level level, String message) {
		LoggerManager.log0(level, message);
	}

	@Override
	public final void info(String message) {
		log(Level.INFO, message);
	}

	@Override
	public final void warn(String message) {
		log(Level.WARN, message);
	}

	@Override
	public final void err(String message) {
		log(Level.ERROR, message);
	}

	@Override
	public final void fatal(String message, Throwable throwable) {
		log(Level.FATAL, message);
		if(throwable != null)
			throwable.printStackTrace();
	}

	@Override
	public final void debug(String prefix, String message) {
		log(Level.DEBUG, String.format("[%s]: %s", prefix, message));
	}
}
