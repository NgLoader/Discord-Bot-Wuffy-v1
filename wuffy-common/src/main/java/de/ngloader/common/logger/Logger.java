package de.ngloader.common.logger;

import de.ngloader.api.logger.ILogger;

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
		log(Level.INFO, "Unknown", message);
	}

	@Override
	public final void warn(String message) {
		log(Level.WARN, "Unknown", message);
	}

	@Override
	public final void err(String message) {
		log(Level.ERROR, "Unknown", message);
	}

	@Override
	public final void fatal(String message, Throwable throwable) {
		log(Level.FATAL, "Unknown", message);
	}

	@Override
	public void log(Level level, String prefix, String message) {
		log(level, String.format("[%s]: %s", prefix, message));
	}

	@Override
	public void info(String prefix, String message) {
		log(Level.INFO, String.format("[%s]: %s", prefix, message));
	}

	@Override
	public void warn(String prefix, String message) {
		log(Level.WARN, String.format("[%s]: %s", prefix, message));
	}

	@Override
	public void err(String prefix, String message) {
		log(Level.ERROR, String.format("[%s]: %s", prefix, message));
	}

	@Override
	public void fatal(String prefix, String message, Throwable throwable) {
		log(Level.FATAL, String.format("[%s]: %s", prefix, message));
		if(throwable != null)
			throwable.printStackTrace();
	}

	@Override
	public final void debug(String prefix, String message) {
		log(Level.DEBUG, String.format("[%s]: %s", prefix, message));
	}
}
