package de.ngloader.core.logger;

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

import de.ngloader.core.logger.Logger.Level;
import de.ngloader.core.util.FileUtil;

/**
 * @author Ingrim4
 */
public class LoggerManager {

	protected static final Path LOG_DIR = Paths.get("./logs/");
	protected static final Path LATEST = LOG_DIR.resolve("latest.log");

	private static final DateFormat SAVE_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	private static boolean debug = false;
	private static LoggerStream logStream, errStream;
	private static PrintWriter printWriter;

	static {
		try {
			FileUtil.delete(LATEST);
			FileUtil.create(LATEST);

			LoggerManager.printWriter = new PrintWriter(Files.newBufferedWriter(LATEST), true);

			System.setOut(LoggerManager.logStream = new LoggerStream(new FileOutputStream(FileDescriptor.out), Level.INFO, LoggerManager.printWriter));
			System.setErr(LoggerManager.errStream = new LoggerStream(new FileOutputStream(FileDescriptor.err), Level.ERROR, LoggerManager.printWriter));
		} catch (IOException e) {
			throw new Error("Failed to init logger", e);
		}
	}

	public static void log0(Level level, String message) {
		if(level == Level.DEBUG && !LoggerManager.debug)
			return;
		(level.isError() ? LoggerManager.errStream : LoggerManager.logStream).log(level, message);
	}

	public static void setDebug(boolean debug) {
		LoggerManager.debug = true;
	}

	public static boolean isDebug() {
		return LoggerManager.debug;
	}

	public static void close() {
		try {
			LoggerManager.logStream.close();
			LoggerManager.errStream.close();
			LoggerManager.printWriter.close();

			var log = LOG_DIR.resolve(String.format("%s.log", SAVE_FORMAT.format(new Date())));
			FileUtil.copy(LATEST, log);
		} catch (Exception e) {
			throw new Error("Failed to close logger", e);
		}
	}
}
