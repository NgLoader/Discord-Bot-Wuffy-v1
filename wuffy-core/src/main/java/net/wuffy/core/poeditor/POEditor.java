package net.wuffy.core.poeditor;

import java.io.IOException;

import net.wuffy.core.util.GsonUtil;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class POEditor {

	private static final String API_URL_TERMS_LIST = "https://api.poeditor.com/v2/terms/list";
	private static final String API_URL_LANGUAGES_LIST = "https://api.poeditor.com/v2/languages/list";

	private final OkHttpClient httpClient = new OkHttpClient();

	private final String token;

	public POEditor(String token) {
		this.token = token;
	}

	public POEditorElementBody getTermsList(String id, String language) {
		try {
			Response response = httpClient.newCall(new Request.Builder()
					.post(new MultipartBody.Builder()
							.setType(MultipartBody.FORM)
							.addFormDataPart("api_token", this.token)
							.addFormDataPart("id", id)
							.addFormDataPart("language", language)
							.build())
					.url(POEditor.API_URL_TERMS_LIST)
					.build()).execute();

			return GsonUtil.GSON.fromJson(response.body().string(), POEditorElementBody.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public POEditorElementBody getLangaugeList(String id) {
		try {
			Response response = httpClient.newCall(new Request.Builder()
					.post(new MultipartBody.Builder()
							.setType(MultipartBody.FORM)
							.addFormDataPart("api_token", this.token)
							.addFormDataPart("id", id)
							.build())
					.url(POEditor.API_URL_LANGUAGES_LIST)
					.build()).execute();

			return GsonUtil.GSON.fromJson(response.body().string(), POEditorElementBody.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}