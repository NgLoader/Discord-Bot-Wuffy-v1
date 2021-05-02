package net.wuffy.bot.command.commands;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import net.dv8tion.jda.api.Permission;
import net.wuffy.bot.keys.PermissionKeys;

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