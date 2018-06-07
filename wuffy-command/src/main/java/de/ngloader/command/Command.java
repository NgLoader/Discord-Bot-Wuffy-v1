package de.ngloader.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.ngloader.language.TranslationKeys;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

	TranslationKeys name() default TranslationKeys.UNKNOWN;
	TranslationKeys description() default TranslationKeys.UNKNOWN;

	TranslationKeys trigger() default TranslationKeys.UNKNOWN;

	EnumCommandCategory category() default EnumCommandCategory.UNKNOWN;
}