package net.wuffy.common.util;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WebRequestBuilder {

	private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

	public static void request(Request request, Callback responseCallback) {
		WebRequestBuilder.HTTP_CLIENT.newCall(request).enqueue(responseCallback);
	}

	public static Response request(Request request) throws IOException {
		return WebRequestBuilder.HTTP_CLIENT.newCall(request).execute();
	}
}