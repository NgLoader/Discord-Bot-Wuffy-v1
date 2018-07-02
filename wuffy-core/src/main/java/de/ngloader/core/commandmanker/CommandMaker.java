package de.ngloader.core.commandmanker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandMaker {

	private List<FunctionInfo> block = new ArrayList<FunctionInfo>();

	public CommandMaker() {
	}

	public boolean addFunction(FunctionInfo function) {
		if(Arrays.asList(this.block.get(this.block.size() - 1).config.supportedComponents()).contains(function.config.component())) {
			this.block.add(function);
			return true;
		}
		return false;
	}

	public boolean removeFunction(FunctionInfo function) {
		if(Arrays.asList(this.block.get(this.block.indexOf(function) - 1).config.supportedComponents()).contains(this.block.get(this.block.indexOf(function) + 1).config.component())) {
			this.block.remove(this.block.indexOf(function));
			return true;
		}
		return false;
	}

	public List<FunctionInfo> getBlock() {
		return this.block;
	}

	public class FunctionInfo {

		public IFunction function;
		public Function config;
	}
}