package net.wuffy.bot.command.commands;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import net.dv8tion.jda.core.Permission;
import net.wuffy.bot.keys.PermissionKeys;

@Target(TYPE)
@Retention(RUNTIME)
public @interface CommandSettings {

	CommandCategory category();

	Permission[] guildPermissionRequierd() default { };

	PermissionKeys[] memberPermissionRequierd() default { };

	PermissionKeys[] memberPermissionList();

	String[] aliases();

	boolean privateChat() default false;//TODO is "admin()" only admin's are allowed to use this in private chat!

	boolean nsfw() default false;

	boolean admin() default false;

	boolean alpha() default false;

	boolean partner() default false;

	boolean hidden() default false;
}