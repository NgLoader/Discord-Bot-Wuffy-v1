package de.ngloader.common.logger;

/**
 * @author Ingrim4
 */
public class Logger {

	public static void log(Level level, String message) {
		LoggerManager.log0(level, message);
	}

	public static void info(String message) {
		log(Level.INFO, "Unknown", message);
	}

	public static void warn(String message) {
		log(Level.WARN, "Unknown", message);
	}

	public static void err(String message) {
		log(Level.ERROR, "Unknown", message);
	}

	public static void fatal(String message, Throwable throwable) {
		log(Level.FATAL, "Unknown", message);
	}

	public static void log(Level level, String prefix, String message) {
		log(level, String.format("[%s]: %s", prefix, message));
	}

	public static void info(String prefix, String message) {
		log(Level.INFO, String.format("[%s]: %s", prefix, message));
	}

	public static void warn(String prefix, String message) {
		log(Level.WARN, String.format("[%s]: %s", prefix, message));
	}

	public static void err(String prefix, String message) {
		log(Level.ERROR, String.format("[%s]: %s", prefix, message));
	}

	public static void fatal(String prefix, String message, Throwable throwable) {
		log(Level.FATAL, String.format("[%s]: %s", prefix, message));
		if(throwable != null)
			throwable.printStackTrace();
	}

	public static void debug(String prefix, String message) {
		log(Level.DEBUG, String.format("[%s]: %s", prefix, message));
	}

	public enum Level {

		INFO(false), WARN(false), ERROR(true), FATAL(true), DEBUG(false);

		private Level(boolean error) {
			this.error = error;
		}

		private boolean error;

		public boolean isError() {
			return error;
		}
	}
}
