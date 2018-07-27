package de.ngloader.core.twitch.response;

@TwitchResponseAnnotation(url = "https://api.twitch.tv/helix/streams/metadata")
public class TwitchResponseStreamMetadata extends TwitchResponse<TwitchResponseStreamMetadata> {

	public String user_id;
	public String game_id;

	public Overwatch overwatch;

	public Object hearthstone;

	class Overwatch {
		public Broadcaster broadcaster;
	}

	class Broadcaster {
		public OverwatchHero hero;
	}

	class OverwatchHero {
		public String role;
		public String name;
		public String ability;
	}

	class Hearthstone {
		public Broadcaster broadcaster;
		public Opponent opponent;
	}

	class Opponent {
		public HearthstoneHero hero;
	}

	class HearthstoneHero {
		public String type;
//		public String class;
		public String name;
	}
}