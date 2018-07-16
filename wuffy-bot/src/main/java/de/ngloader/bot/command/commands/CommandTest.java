package de.ngloader.bot.command.commands;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;

@Command(aliases = { "test" })
@CommandConfig(category = CommandCategory.OTHER)
public class CommandTest extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		event.getChannel().sendMessage("Test").queue();
//		System.out.println("hey");
//		event.getGuild(WuffyGuild.class).addBanHistory(new Document("test", 1234));
//		event.getGuild(WuffyGuild.class).addBanHistory(new Document("test", 4321));
//		System.out.println(event.getGuild(WuffyGuild.class).getBanHistory().stream().map(doc -> Integer.toString(doc.get("test", -1))).collect(Collectors.joining(", ")));
//		event.getChannel().sendMessage("Info: \n" +  event.getGuild(WuffyGuild.class).getPermissionChannel(EnumPermissionType.USER, 0L, "Nils", "Pascal").stream().collect(Collectors.joining("\n"))).queue();

//		event.getGuild(WuffyGuild.class).addPermissionChannel(EnumPermissionType.USER, 0L, Arrays.asList("Nils", "Pascal"), "perm1", "perm2");
//		event.getGuild(WuffyGuild.class).addPermissionChannel(EnumPermissionType.USER, 0L, "Nils", "perm3", "perm4");
//		event.getGuild(WuffyGuild.class).removePermissionChannel(EnumPermissionType.USER, 0L, Arrays.asList("Nils", "Pascal"), "perm1", "perm2");
//		event.getGuild(WuffyGuild.class).removePermissionChannel(EnumPermissionType.USER, 0L, "Nils", "perm5", "perm4");
//		event.getGuild(WuffyGuild.class).setPermissionChannel(EnumPermissionType.USER, 0L, Arrays.asList("Nils", "Pascal"), "perm1", "perm2");
//		event.getGuild(WuffyGuild.class).setPermissionChannel(EnumPermissionType.USER, 0L, "Nils", "perm3", "perm4");
//
//		event.getGuild(WuffyGuild.class).addPermissionGlobal(EnumPermissionType.USER, Arrays.asList("Nils", "Pascal"), "perm1", "perm2");
//		event.getGuild(WuffyGuild.class).addPermissionGlobal(EnumPermissionType.USER, "Nils", "perm3", "perm4");
//		event.getGuild(WuffyGuild.class).removePermissionGlobal(EnumPermissionType.USER, Arrays.asList("Nils", "Pascal"), "perm1", "perm2");
//		event.getGuild(WuffyGuild.class).removePermissionGlobal(EnumPermissionType.USER, "Nils", "perm3", "perm4");
//		event.getGuild(WuffyGuild.class).setPermissionGlobal(EnumPermissionType.USER, Arrays.asList("Nils", "Pascal"), "perm1", "perm2");
//		event.getGuild(WuffyGuild.class).setPermissionGlobal(EnumPermissionType.USER, "Nils", "perm3", "perm4");

//		event.getGuild(WuffyGuild.class).addPermissionMode(EnumPermissionMode.CHANNEL_RANKING);
	}
}