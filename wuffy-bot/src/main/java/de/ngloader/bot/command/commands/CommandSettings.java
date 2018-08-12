package de.ngloader.bot.command.commands;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import de.ngloader.bot.keys.PermissionKeys;
import net.dv8tion.jda.core.Permission;

@Target(TYPE)
@Retention(RUNTIME)
public @interface CommandSettings {

	CommandCategory category();

	Permission[] guildPermissionRequierd() default { };

	PermissionKeys[] memberPermissionRequierd() default { };

	PermissionKeys[] memberPermissionList();

	String[] aliases();

	boolean adminCommand() default false;

	boolean privateChatCommand() default false;

	boolean nsfw() default false;

	boolean alpha() default false;
}