package de.ngloader.api.config;

public interface IConfigService {

	<T extends IConfig> void loadConfig(Class<? extends T> configClass);
	<T extends IConfig> void saveConfig(Class<T> configClass, T config);
	<T extends IConfig> T getConfig(Class<? extends T> configClass);
}