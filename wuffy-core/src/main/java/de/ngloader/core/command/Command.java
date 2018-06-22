package de.ngloader.core.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.ngloader.api.lang.TranslationKey;
import net.dv8tion.jda.core.AccountType;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

	AccountType accountType() default AccountType.BOT;

	TranslationKey name() default TranslationKey.UNKNOWN;

	TranslationKey description() default TranslationKey.UNKNOWN;

	String[] aliases();

	EnumCommandCategory category() default EnumCommandCategory.UNKNOWN;
}