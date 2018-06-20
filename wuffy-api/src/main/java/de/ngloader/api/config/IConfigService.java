package de.ngloader.api.config;

public interface IConfigService {

	<T extends Config> void loadConfig(Class<T> configClass);
	<T extends Config> void saveConfig(Class<T> configClass, T config);
	<T extends Config> T getConfig(Class<T> configClass);
}