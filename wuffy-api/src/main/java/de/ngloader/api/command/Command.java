package de.ngloader.api.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.ngloader.api.lang.TranslationKey;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

	TranslationKey name() default TranslationKey.UNKNOWN;
	TranslationKey description() default TranslationKey.UNKNOWN;

	TranslationKey trigger();

	EnumCommandCategory category() default EnumCommandCategory.UNKNOWN;
}