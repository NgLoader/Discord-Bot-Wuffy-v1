package de.ngloader.common.logger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.ngloader.common.logger.ILogger.Level;

/**
 * @author Ingrim4
 */
public class LoggerStream extends PrintStream {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");

	private final Level defaultLevel;
	private final PrintWriter printWriter;

	public LoggerStream(OutputStream out, Level defaultLevel, PrintWriter printWriter) throws IOException {
		super(out);
		this.defaultLevel = defaultLevel;
		this.printWriter = printWriter;
	}

	private String format(Level level, String str) {
		return String.format("[%s][%s/%s]: %s", DATE_FORMAT.format(new Date()), Thread.currentThread().getName(), level.toString().toUpperCase(), str);
	}

	private void file0(String str) {
		if(this.printWriter != null)
			this.printWriter.println(str);
	}

	private void print0(String str) {
		str = this.format(defaultLevel, String.format("[STDOUT]: %s", str));
		super.print(str);
		this.file0(str);
	}

	public void file(Level level, String str) {
		this.file0(this.format(level, str));
	}

	public void log(Level level, String str) {
		str = this.format(level, str);
		super.print(str);
		super.println();
		this.file0(str);
	}

	@Override
	public void print(boolean b) {
		this.print0(String.valueOf(b));
	}

	@Override
	public void print(char c) {
		this.print0(String.valueOf(c));
	}

	@Override
	public void print(char[] str) {
		this.print0(String.valueOf(str));
	}

	@Override
	public void print(double d) {
		this.print0(String.valueOf(d));
	}

	@Override
	public void print(float f) {
		this.print0(String.valueOf(f));
	}

	@Override
	public void print(int i) {
		this.print0(String.valueOf(i));
	}

	@Override
	public void print(long l) {
		this.print0(String.valueOf(l));
	}

	@Override
	public void print(Object obj) {
		this.print0(String.valueOf(obj));
	}

	@Override
	public void print(String str) {
		this.print0(str);
	}

	@Override
	public void println() {
		this.print0("");
		super.println();
	}
}
