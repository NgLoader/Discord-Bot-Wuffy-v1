package de.ngloader.bot.command.commands.image.rra;

import java.io.IOException;

import org.json.JSONObject;

import de.ngloader.core.util.WebRequestBuilder;
import okhttp3.Request;
import okhttp3.Response;

public class RRABuilder {

	private static final String URL_RANDOM = "https://rra.ram.moe/i/r?params&type=";
	private static final String URL_PICTURE = "https://cdn.ram.moe/";

	public static String getRandom(EnumRRATypes type) {
		try {
			Response response = WebRequestBuilder.request(new Request.Builder()
					.url(String.format("%s%s", RRABuilder.URL_RANDOM, type.value))
					.build());

			return String.format("%s%s", RRABuilder.URL_PICTURE, new JSONObject(response.body().string()).getString("path").substring(3));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}