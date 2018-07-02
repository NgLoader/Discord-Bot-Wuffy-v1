package de.ngloader.core.commandmanker.function.logic;

import java.util.Arrays;

import de.ngloader.core.commandmanker.EnumFunctionType;
import de.ngloader.core.commandmanker.Function;
import de.ngloader.core.commandmanker.IFunction;
import de.ngloader.core.commandmanker.function.EnumFunctionComponent;

@Function(type = EnumFunctionType.LOGIC, component = EnumFunctionComponent.EQUALS, supportedComponents = { EnumFunctionComponent.EQUALS, EnumFunctionComponent.NOT_EQUALS })
public class LogicEqualsFunction implements IFunction {

	@Override
	public boolean check(Object... objects) {
		return Arrays.asList(objects).stream().distinct().count() == 1;
	}
}