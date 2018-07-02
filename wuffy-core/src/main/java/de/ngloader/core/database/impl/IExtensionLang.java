package de.ngloader.core.database.impl;

import java.util.List;

import de.ngloader.core.database.IStorageExtension;

public interface IExtensionLang <LANG extends ImplLang> extends IStorageExtension {

	public LANG getLang(String lang);

	public List<LANG> getLangs();
}