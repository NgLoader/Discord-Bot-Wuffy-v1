package net.wuffy.bot.database.mongo.impl;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import net.wuffy.bot.database.mongo.MongoStorageImpl;
import net.wuffy.bot.keys.TranslationKeys;
import net.wuffy.common.logger.Logger;
import net.wuffy.common.util.LocaleUtil;
import net.wuffy.core.Core;
import net.wuffy.core.lang.II18n;
import net.wuffy.core.lang.ILanguage;

public class MongoDBI18n implements II18n {

	private static final ILanguage DEFAULT_LANGUAGE = new ILanguage() {

		@Override
		public Locale getLocale() {
			return Locale.US;
		}

		@Override
		public void setLocale(Locale locale) { }

		@Override
		public String getTranslation(String key) {
			return key;
		}

		@Override
		public void addTranslation(String key, String translation) { };

		@Override
		public void removeTranslation(String key) { }
	};

	private MongoStorageImpl storage;
	private MongoCollection<Document> collection;

	private Map<String, ILanguage> language = new HashMap<String, ILanguage>();

	public MongoDBI18n(MongoStorageImpl storage, Core core) {
		this.storage = storage;
	}

	@Override
	public void initialize() {
		this.collection = this.storage.getStorage().getCollection("language");

		FindIterable<Document> found = this.collection.find();

		for(Document document : found)
			new Language(this, document);
	}

	@Override
	public void destroy() {
		this.language.clear();

		this.storage = null;
		this.collection = null;
		this.language = null;
	}

	@Override
	public String format(String key, String locale, String... params) {
		return this.getLanguage(locale).getTranslation(key);
	}

	@Override
	public ILanguage getLanguage(String locale) {
		return this.language.getOrDefault(locale, MongoDBI18n.DEFAULT_LANGUAGE);
	}

	@Override
	public ILanguage getLanguage(Locale locale) {
		return this.language.getOrDefault(locale.toLanguageTag(), MongoDBI18n.DEFAULT_LANGUAGE);
	}

	@Override
	public void addLanguage(String locale) {
		if(this.language.containsKey(locale)) {
			Logger.err("MongoDB I18n", String.format("Langauge \"%s\" already exit", locale));
			return;
		}

		this.language.put(locale, new Language(this, new Document("_locale", locale)));

		this.collection.insertOne(new Document("_locale", locale));

		Logger.debug("MongoDB I18n", String.format("Language \"%s\" added", locale));
	}

	@Override
	public void removeLanguage(String locale) {
		if(!this.language.containsKey(locale)) {
			Logger.err("MongoDB I18n", String.format("Langauge \"%s\" not exist", locale));
			return;
		}

		this.language.remove(locale);

		this.collection.deleteOne(Filters.eq(new Document("_locale", locale)));

		Logger.debug("MongoDB I18n", String.format("Language \"%s\" removed", locale));
	}

	//TODO send update to master
	public class Language implements ILanguage {

		private final MongoDBI18n i18n;

		private Locale locale;
		private Map<String, String> translation = new HashMap<String, String>();

		public Language(MongoDBI18n i18n, Document document) {
			this.i18n = i18n;

			try {
				if(!document.containsKey("_locale")) {
					Logger.err("MongoDB I18n", String.format("Error by locating locale in document \"%s\"", document.getObjectId("_id").toHexString()));
					return;
				}

				this.locale = LocaleUtil.getLocaleByTag(document.getString("_locale"));

				if(this.locale == null) {
					Logger.err("MongoDB I18n", String.format("Error unknown locale in document \"%s\"", document.getObjectId("_id").toHexString()));
					return;
				}

				document.get("translation", new Document()).forEach((key, value) -> this.translation.put((String) key, (String) value));
			} catch(Exception e) {
				Logger.fatal("MongoDB I18n", String.format("Error by loading document \"%s\"", document.getObjectId("_id").toHexString()), e);
				return;
			}

			this.i18n.language.put(this.locale.toLanguageTag(), this);

			Logger.debug("MongoDB I18n", String.format("Translation \"%s\" loaded with \"%s/%s\" translation's", this.locale.toLanguageTag(), Integer.toString(this.translation.size()), Integer.toString(TranslationKeys.class.getFields().length)));
		}

		@Override
		public Locale getLocale() {
			return this.locale;
		}

		@Override
		public void setLocale(Locale locale) {
			String oldLocale = this.locale.toLanguageTag();

			this.locale = locale;

			this.i18n.collection.updateOne(Filters.eq("_locale", oldLocale), new Document("$set", new Document("_locale", this.locale.toLanguageTag())));

			Logger.debug("MongoDB I18n", String.format("Translation locale was set from \"%s\" to \"%s\"", oldLocale, this.locale.toLanguageTag()));
		}

		@Override
		public String getTranslation(String key) {
			return this.translation.getOrDefault(key, key);
		}

		@Override
		public void addTranslation(String key, String translation) {
			this.translation.put(key, translation);

			this.i18n.collection.updateOne(Filters.eq("_locale", this.locale.toLanguageTag()), new Document("$set", new Document(String.format("translation.%s", key), translation)));

			Logger.debug("MongoDB I18n", String.format("Translation \"%s\" added \"%s\"", this.locale.toLanguageTag(), key));
		}

		@Override
		public void removeTranslation(String key) {
			this.translation.remove(key);

			this.i18n.collection.updateOne(Filters.eq("_locale", this.locale.toLanguageTag()), new Document("$unset", String.format("translation.%s", key)));

			Logger.debug("MongoDB I18n", String.format("Translation \"%s\" removed \"%s\"", this.locale.toLanguageTag(), key));
		}
	}
}