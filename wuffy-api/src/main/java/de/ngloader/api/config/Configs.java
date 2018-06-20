package de.ngloader.api.config;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import de.ngloader.api.command.Command;

@Retention(RUNTIME)
@Target(TYPE)
public @interface Configs {

	Command[] value();
}