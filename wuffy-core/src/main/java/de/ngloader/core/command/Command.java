package de.ngloader.core.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.ngloader.core.lang.TranslationKeys;
import net.dv8tion.jda.core.AccountType;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

	AccountType accountType() default AccountType.BOT;

	TranslationKeys name() default TranslationKeys.UNKNOWN;

	TranslationKeys description() default TranslationKeys.UNKNOWN;

	String[] aliases();

	EnumCommandCategory category() default EnumCommandCategory.UNKNOWN;
}