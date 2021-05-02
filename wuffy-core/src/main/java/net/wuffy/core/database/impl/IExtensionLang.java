package net.wuffy.core.database.impl;

import java.util.List;

import net.wuffy.core.database.IStorageExtension;

public interface IExtensionLang <LANG extends ImplLang> extends IStorageExtension {

	public LANG getLang(String lang);

	public List<LANG> getLangs();
}