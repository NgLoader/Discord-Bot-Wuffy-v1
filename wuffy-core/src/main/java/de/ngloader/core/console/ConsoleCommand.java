package de.ngloader.core.console;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Ingrim4
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface ConsoleCommand {

	String[] aliases();

	String usage();

	String category() default "default";
}