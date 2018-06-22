package de.ngloader.core.logger;

/**
 * @author Ingrim4
 */
public interface ILogger {

	void log(Level level, String message);

	void info(String message);

	void warn(String message);

	void err(String message);

	void fatal(String message, Throwable throwable);

	void log(Level level, String prefix, String message);

	void info(String prefix, String message);

	void warn(String prefix, String message);

	void err(String prefix, String message);

	void fatal(String prefix, String message, Throwable throwable);

	void debug(String prefix, String message);

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