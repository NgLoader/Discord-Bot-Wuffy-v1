package net.wuffy.bot.database.lang;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import net.wuffy.common.util.LocaleUtil;
import net.wuffy.core.database.StorageProvider;
import net.wuffy.core.database.impl.IExtensionLang;
import net.wuffy.core.database.poeditor.POEditorStorage;
import net.wuffy.core.poeditor.POEditor;

public class POEditorExtensionLang extends StorageProvider<POEditorStorage> implements IExtensionLang<WuffyLang> {

	protected POEditorStorage storage;

	@Override
	protected void registered(POEditorStorage storage) {
		this.storage = storage;
	}

	@Override
	protected void unregistered() {
		this.storage = null;
	}

	@Override
	public WuffyLang getLang(String lang) {
		return new WuffyLang(this.core, Locale.forLanguageTag(lang), new POEditor(this.storage.getConfig().api_token).getTermsList(this.storage.getConfig().project_id, lang).result.terms.stream()
				.collect(Collectors.toMap(term -> term.context, term -> term.translation.content)));
	}

	@Override
	public List<WuffyLang> getLangs() {
		POEditor poeditor = new POEditor(this.storage.getConfig().api_token);

		return poeditor.getLangaugeList(this.storage.getConfig().project_id).result.languages.stream()
				.map(lang -> new WuffyLang(this.core, LocaleUtil.getLocaleByTag(this.convertToTag(lang.code)), poeditor.getTermsList(this.storage.getConfig().project_id, lang.code).result.terms.stream()
						.collect(Collectors.toMap(term -> term.context, term -> term.translation.content))))
				.filter(term -> term.translations.containsKey("finish") && term.translations.get("finish").equals("true"))
				.collect(Collectors.toList());
	}

	public String convertToTag(String code) {
		switch (code) {
		case "de":
			return "de-DE";

		default:
			return code;
		}
	}
}