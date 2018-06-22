package de.ngloader.core.util;

public enum Reactions {
	
	NO_ENTRY("⛔"),
	HEART("❤"),
	TADA("🎉"),
	WHITE_CHECK_MARK("✅"),
	THUMBSUP("👍"),
	INTERROBANG("⁉"),
	GREY_QUESTION("❔"),
	WARNING("⚠"),
	ANGER("💢"),
	HEARTPUlSE("💗"),
	HEART_DECORATION("💟"),
	HEART_EXCLAMATION("❣"),
	SPARKLING_HEART("💖"),
	BROKEN_HEART("💔"),
	HEARTS("♥"),
	RED_CIRCLE("🔴"),
	RADIO_BUTTION("🔘"),
	WHITE_LINE("▬"),
	REWIND("⏪"),
	FAST_FORWARD("⏩"),
	STOP_BUTTON("⏹"),
	TRACK_PREVIOUS("⏮"),
	TRACK_NEXT("⏭");
	
	private String emoji;
	
	private Reactions(String emoji) {
		this.emoji = emoji;
	}
	
	public String getAsUnicode() {
		return this.emoji;
	}
}