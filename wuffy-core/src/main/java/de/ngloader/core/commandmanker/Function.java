package de.ngloader.core.commandmanker;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import de.ngloader.core.commandmanker.function.EnumFunctionComponent;

@Target(TYPE)
@Retention(RUNTIME)
public @interface Function {

	public EnumFunctionType type();

	public EnumFunctionComponent component() default EnumFunctionComponent.NULL;

	public EnumFunctionComponent[] supportedComponents() default { };
}