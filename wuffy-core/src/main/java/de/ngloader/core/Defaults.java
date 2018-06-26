package de.ngloader.core;

import java.util.stream.Collectors;

public class Defaults {

	public static final String JAVA_VERSION = new StringBuilder("Java/").append(Runtime.version().version().stream().map(i -> Integer.toString(i)).collect(Collectors.joining("."))).toString();
}