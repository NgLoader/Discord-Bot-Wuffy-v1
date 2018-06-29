package de.ngloader.core.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.ngloader.core.logger.Logger;
import de.ngloader.core.util.GsonUtil;

public class ConfigService {

	private static final Map<Class<? extends IConfig>, IConfig> configs = new HashMap<Class<? extends IConfig>, IConfig>();

	public static <T extends IConfig> void loadConfig(Class<? extends T> configClass) {
		Objects.isNull(configClass);

		Config config = configClass.getDeclaredAnnotation(Config.class);
		if(config == null)
			throw new NullPointerException("'" + configClass.getSimpleName() + "' has not the annotation '" + Config.class.getSimpleName() + "'");

		Path path = Paths.get(config.path());

		if(Files.notExists(path))
			try {
				Files.createDirectories(path.getParent());

				try (InputStream inputStream = ConfigService.class.getResourceAsStream(config.sourcePath())) {
					if(inputStream == null)
						throw new FileNotFoundException(String.format("Source file '%s' not exits. Checkout '%s'", config.sourcePath(), configClass.getSimpleName()));

					Files.copy(inputStream, path);
				}
			} catch(IOException e) {
				e.printStackTrace();
				Logger.fatal("Failed to create " + config.sourcePath(), e);
			}

		try (BufferedReader reader = Files.newBufferedReader(path)) {
			configs.put(configClass, GsonUtil.GSON_PRETTY_PRINTING.fromJson(reader, configClass));
		} catch (IOException e) {
			e.printStackTrace();
			Logger.fatal(String.format("Error loading file '%s'", config.path()), e);
		}
	}

	public static <T extends IConfig> void saveConfig(Class<T> configClass, T config) {
		Objects.isNull(configClass);
		Objects.isNull(config);
		//TODO fill out
	}

	public static <T extends IConfig> T getConfig(Class<? extends T> configClass) {
		Objects.isNull(configClass);

		if(!configs.containsKey(configClass))
			loadConfig(configClass);

		return configClass.cast(configs.get(configClass));
	}
}