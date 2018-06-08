package de.ngloader.common.logger;

/**
 * @author Ingrim4
 */
public interface ILogger {

	void log(Level level, String message);

	void info(String message);

	void warn(String message);

	void err(String message);

	void fatal(String message, Throwable throwable);

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