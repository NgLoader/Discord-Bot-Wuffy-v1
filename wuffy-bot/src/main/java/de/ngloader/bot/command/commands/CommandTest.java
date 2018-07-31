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
//		event.getChannel().sendMessage("Loading <a:loading:468438447573696522>").queue();
//		new ReplayBuilder(event, MessageType.INFO, new EmbedBuilder()
//				.setImage("https://static-cdn.jtvnw.net/jtv_user_pictures/ccd1bfdd-16e9-4697-9ac9-0927d5cbfeda-profile_image-300x300.jpg")
//				.setTitle("H0lly", "https://www.twitch.tv/h0llylp")
//				.addField("Game", "Enter the Gungeon", true)
//				.addField("Viewers", "947", true)
//				.setDescription("[(ETG Update) Runtime Presents Enter the Gungeon: Advanced Gungeons](https://www.twitch.tv/h0llylp)")
//				.setThumbnail("https://static-cdn.jtvnw.net/jtv_user_pictures/ccd1bfdd-16e9-4697-9ac9-0927d5cbfeda-profile_image-300x300.jpg")
//				.setImage("https://static-cdn.jtvnw.net/previews-ttv/live_user_h0llylp-320x180.jpg")
//				.build())
//		.queue();
//		WebhookMessageBuilder webhookMessageBuilder = new WebhookMessageBuilder();
//
//		EmbedBuilder builder = new EmbedBuilder()
//			.setImage("https://static-cdn.jtvnw.net/jtv_user_pictures/ccd1bfdd-16e9-4697-9ac9-0927d5cbfeda-profile_image-300x300.jpg")
//			.setTitle("H0lly", "https://www.twitch.tv/h0llylp")
//			.addField("Game", "Enter the Gungeon", true)
//			.addField("Viewers", "947", true)
//			.setDescription("[(ETG Update) Runtime Presents Enter the Gungeon: Advanced Gungeons](https://www.twitch.tv/h0llylp)")
//			.setThumbnail("https://static-cdn.jtvnw.net/jtv_user_pictures/ccd1bfdd-16e9-4697-9ac9-0927d5cbfeda-profile_image-300x300.jpg")
//			.setImage("https://static-cdn.jtvnw.net/previews-ttv/live_user_h0llylp-320x180.jpg");
//
//		webhookMessageBuilder.addEmbed(builder);
//		webhookMessageBuilder.setAvatar_url("https://zockercraft.net/Pictures/loading.gif");
//		webhookMessageBuilder.setContent("Moin dieses ist ein TEST ;D");
//		webhookMessageBuilder.setUsername("Wuffy WEBHOOK");
//
//		System.out.println("RESULT: " + WebhookUtil.send("https://ptb.discordapp.com/api/webhooks/470050310539313174/3OjGuQAUul9Jw-cUyEGEEs9gpRTbedeECRwaV7Thf4HbVP94dNHYc2dilaX2hEZ9zcrj", webhookMessageBuilder));
//		event.getCore().getI18n().loadLangs(event.getCore().getStorageService().getExtension(IExtensionLang.class));
//		event.getChannel().sendMessage(new EmbedBuilder().setDescription("<a:checkmark:459068723408535552> Successful executed test.").setColor(Color.RED).build()).queue();
//		event.getGuild(WuffyGuild.class).addPermissionMode(EnumPermissionMode.CHANNEL_RANKING, EnumPermissionMode.CHANNEL_ROLE, EnumPermissionMode.CHANNEL_USER, EnumPermissionMode.GLOBAL_RANKING, EnumPermissionMode.CHANNEL_ROLE, EnumPermissionMode.GLOBAL_USER);
//		event.getGuild(WuffyGuild.class).addPermissionChannel(EnumPermissionType.USER, 343187634388205568L, Long.toString(128293854733402112L), "command.vckick");
//		event.getGuild(WuffyGuild.class).addPermissionGlobal(EnumPermissionType.USER, Long.toString(128293854733402112L), "command.shards");
//		System.out.println("hey");
//		event.getGuild(WuffyGuild.class).addBanHistory(new Document("test", 1234));
//		event.getGuild(WuffyGuild.class).addBanHistory(new Document("test", 4321));
//		System.out.println(event.getGuild(WuffyGuild.class).getBanHistory().stream().map(doc -> Integer.toString(doc.get("test", -1))).collect(Collectors.joining(", ")));
//		event.getChannel().sendMessage("Info: \n" +  event.getGuild(WuffyGuild.class).getPermissionChannel(EnumPermissionType.USER, 0L, "Nils", "Pascal").stream().collect(Collectors.joining("\n"))).queue();
//
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
//
//		event.getGuild(WuffyGuild.class).addPermissionMode(EnumPermissionMode.CHANNEL_RANKING);
	}
}