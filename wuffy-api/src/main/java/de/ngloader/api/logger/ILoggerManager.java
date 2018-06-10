package de.ngloader.api.logger;

import de.ngloader.api.logger.ILogger.Level;

public interface ILoggerManager {

	public void log0(Level level, String message);

	public void setDebug(boolean debug);

	public boolean isDebug();

	public Logger getLogger();

	public void close();
}