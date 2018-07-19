package de.ngloader.core.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class WebRequestBuilder {

	private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

	public static void request(Request request, Callback responseCallback) {
		WebRequestBuilder.HTTP_CLIENT.newCall(request).enqueue(responseCallback);
	}
}