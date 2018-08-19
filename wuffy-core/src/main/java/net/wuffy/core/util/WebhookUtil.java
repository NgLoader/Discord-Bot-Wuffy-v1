package net.wuffy.core.util;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class WebhookUtil {

	private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

	public static String send(String webhookURL, WebhookMessageBuilder builder) {
		return WebhookUtil.send(webhookURL, builder.build());
	}

	public static String send(String webhookURL, String message) {
		Request request = new Request.Builder()
				.header("Content-Type", "application/json")
				.url(webhookURL)
				.post(RequestBody.create(WebhookUtil.MEDIA_TYPE_JSON, message))
				.build();

		try {
			return WebRequestBuilder.request(request).body().string();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}