package de.ngloader.api.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {

	public static final Gson GSON_PRETTY_PRINTING = new GsonBuilder().setPrettyPrinting().create();
	public static final Gson GSON = new GsonBuilder().create();
}