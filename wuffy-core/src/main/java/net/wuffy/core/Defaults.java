package net.wuffy.core;

import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

public class Defaults {

	public static final String JAVA_VERSION = new StringBuilder("Java/").append(Runtime.version().version().stream().map(i -> Integer.toString(i)).collect(Collectors.joining("."))).toString();

	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("hh:mm:ss mm.HH.yyyy");
}