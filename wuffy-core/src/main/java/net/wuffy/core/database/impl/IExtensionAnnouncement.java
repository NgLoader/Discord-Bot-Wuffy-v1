package net.wuffy.core.database.impl;

import java.util.List;

import net.wuffy.core.database.IStorageExtension;

public interface IExtensionAnnouncement <ANNOUNCEMENT extends ImplAnnouncement> extends IStorageExtension {

	public void addAnnouncement(String key, String id, ANNOUNCEMENT announcement);

	public List<ANNOUNCEMENT> getAnnouncement(String key, String id);

	public List<ANNOUNCEMENT> getAnnouncement(String key, Long guildId);

	public ANNOUNCEMENT getAnnouncement(String key, String id, Long guildId);

	public void removeAnnouncement(String key, String id, Long guildId);

	public void removeAnnouncement(String key, String id);

	public void removeAnnouncement(String key);

	public void removeAnnouncement(Long guildId);
}