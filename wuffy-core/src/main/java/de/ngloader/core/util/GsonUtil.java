package de.ngloader.core.util;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {

	public static final Gson GSON_PRETTY_PRINTING = new GsonBuilder().setPrettyPrinting().create();
	public static final Gson GSON = new GsonBuilder().create();

	public static final Document EMPTY_DOCUMENT = new Document();

	public static <T extends Object> List<T> arrayGet(Document document, String key) {
		return new ArrayList<>(GsonUtil.getDotNotation(document, key, new ArrayList<T>()));
	}

	public static <T extends Object> List<T> arrayAdd(Document document, String key, List<T> add) {
		List<T> array = new ArrayList<>(GsonUtil.getDotNotation(document, key, new ArrayList<T>()));

		array.addAll(add);

		return array;
	}

	public static <T extends Object> List<T> arrayAdd(Document document, String key, T[] add) {
		List<T> array = new ArrayList<>(GsonUtil.getDotNotation(document, key, new ArrayList<T>()));

		for(T obj : add)
			array.add(obj);

		return array;
	}

	public static <T extends Object> List<T> arrayAdd(Document document, String key, T add) {
		List<T> array = new ArrayList<>(GsonUtil.getDotNotation(document, key, new ArrayList<T>()));

		array.add(add);

		return array;
	}

	public static <T extends Object> List<T> arrayRemove(Document document, String key, List<T> remove) {
		List<T> array = new ArrayList<>(GsonUtil.getDotNotation(document, key, new ArrayList<T>()));

		array.removeAll(remove);

		return array;
	}

	public static <T extends Object> List<T> arrayRemove(Document document, String key, T[] remove) {
		List<T> array = new ArrayList<>(GsonUtil.getDotNotation(document, key, new ArrayList<T>()));

		for(Object obj : remove)
			array.remove(obj);

		return array;
	}

	public static <T extends Object> List<T> arrayRemove(Document document, String key, T remove) {
		List<T> array = new ArrayList<>(GsonUtil.getDotNotation(document, key, new ArrayList<T>()));

		array.remove(remove);

		return array;
	}

	public static <T extends Object> T getDotNotation(Document document, String key, T defaultValue) {
		String[] keys = key.split("\\.");

		Document result = document;

		for(int i = 0; i < keys.length - 1; i++)
			result = result.get(keys[i], EMPTY_DOCUMENT);

		return result.get(keys[keys.length - 1], defaultValue);
	}
}