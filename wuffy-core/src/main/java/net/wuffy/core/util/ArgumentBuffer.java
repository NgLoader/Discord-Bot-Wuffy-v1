package net.wuffy.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgumentBuffer {

	private int index = 0;
	private List<String> arguments;

	public ArgumentBuffer(List<String> arguments) {
		this.arguments = new ArrayList<>(arguments);
	}

	public ArgumentBuffer(String[] arguments) {
		this(Arrays.asList(arguments));
	}

	public ArgumentBuffer(String argumentString) {
		this(argumentString.split("\\s+"));
	}

	public ArgumentBuffer addArgument(String arg) {
		this.arguments.add(arg);
		return this;
	}

	public ArgumentBuffer addArgument(String... args) {
		this.arguments.addAll(Arrays.asList(args));
		return this;
	}

	public ArgumentBuffer addArgument(List<String> args) {
		this.arguments.addAll(args);
		return this;
	}

	public String removeArgument() {
		return this.removeArgument(0, null);
	}

	public String removeArgument(String defaultValue) {
		return this.removeArgument(0, defaultValue);
	}

	public String removeArgument(int index) {
		return this.removeArgument(index, null);
	}

	public String removeArgument(int index, String defaultValue) {
		if(this.arguments.size() > index)
			return this.arguments.remove(index);
		return defaultValue;
	}

	public String get(int index) {
		if(this.arguments.size() > index)
			return this.arguments.get(index);
		return null;
	}

	public String next() {
		if(this.arguments.size() > index)
			return this.arguments.get(this.index++);
		return null;
	}

	public String last() {
		if(this.arguments.size() > index)
			return this.arguments.get(this.index--);
		return null;
	}

	public String join(CharSequence delimiter) {
		return String.join(delimiter, this.arguments);
	}

	public int getIndex() {
		return this.index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String[] getArguments() {
		return this.arguments.toArray(new String[this.arguments.size()]);
	}

	public int getSize() {
		return this.arguments.size();
	}
}