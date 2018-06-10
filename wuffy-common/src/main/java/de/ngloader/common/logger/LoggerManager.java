package de.ngloader.common.logger;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.ngloader.api.logger.ILoggerManager;
import de.ngloader.api.logger.Logger;
import de.ngloader.api.logger.ILogger.Level;
import de.ngloader.common.util.FileUtil;

/**
 * @author Ingrim4
 */
public class LoggerManager implements ILoggerManager {

	protected static final Path LOG_DIR = Paths.get("./logs/");
	protected static final Path LATEST = LOG_DIR.resolve("latest.log");

	private static final Logger LOGGER = new Logger();

	private static final DateFormat SAVE_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	private boolean debug = false;
	private LoggerStream logStream, errStream;
	private PrintWriter printWriter;

	public LoggerManager() {
		try {
			FileUtil.delete(LATEST);
			FileUtil.create(LATEST);

			this.printWriter = new PrintWriter(Files.newBufferedWriter(LATEST), true);

			System.setOut(this.logStream = new LoggerStream(new FileOutputStream(FileDescriptor.out), Level.INFO, this.printWriter));
			System.setErr(this.errStream = new LoggerStream(new FileOutputStream(FileDescriptor.err), Level.ERROR, this.printWriter));
		} catch (IOException e) {
			throw new Error("Failed to init logger", e);
		}
	}

	@Override
	public void log0(Level level, String message) {
		if(level == Level.DEBUG && !this.debug)
			return;
		(level.isError() ? this.errStream : this.logStream).log(level, message);
	}

	@Override
	public void setDebug(boolean debug) {
		this.debug = true;
	}

	@Override
	public boolean isDebug() {
		return this.debug;
	}

	@Override
	public Logger getLogger() {
		return LoggerManager.LOGGER;
	}

	@Override
	public void close() {
		try {
			this.logStream.close();
			this.errStream.close();
			this.printWriter.close();

			var log = LOG_DIR.resolve(String.format("%s.log", SAVE_FORMAT.format(new Date())));
			FileUtil.copy(LATEST, log);
		} catch (Exception e) {
			throw new Error("Failed to close all logger", e);
		}
	}
}
