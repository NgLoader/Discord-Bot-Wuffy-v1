package net.wuffy.common;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

public class Defaults {

	public static final String VERSION = "1.0.0_35";

	public static final String JAVA_VERSION = new StringBuilder("Java/").append(Runtime.version().version().stream().map(i -> Integer.toString(i)).collect(Collectors.joining("."))).toString();
	public static final Path JAVA = Paths.get(System.getProperty("java.home"), "bin", "java");

	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("hh:mm:ss mm.HH.yyyy");

	public static final String DISCORD_API = "https://discordapp.com/api/";

	public static final int SHARD_RAM_COST = 1024;
}