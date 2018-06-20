package de.ngloader.master.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.google.gson.reflect.TypeToken;

import de.ngloader.api.WuffyServer;
import de.ngloader.api.config.Config;
import de.ngloader.api.config.IConfigService;
import de.ngloader.api.logger.ILogger;
import de.ngloader.api.util.GsonUtil;

public class ConfigService implements IConfigService {

	private static final ILogger LOGGER = WuffyServer.getLogger();

	private final Map<Class<? extends Config>, Class<? extends Config>> configs = new HashMap<Class<? extends Config>, Class<? extends Config>>();

	@Override
	public <T extends Config> void loadConfig(Class<T> configClass) {
		Objects.requireNonNull(configClass);

		Config config = configClass.getAnnotation(Config.class);
		Path path = Paths.get(config.path());

		try (BufferedReader reader = Files.newBufferedReader(path)) {
			configs.put(configClass, GsonUtil.GSON.fromJson(reader, TypeToken.getParameterized(ArrayList.class, configClass).getType()));
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.fatal(String.format("Error loading file '%s'", config.path()), e);
		}
	}

	@Override
	public <T extends Config> void saveConfig(Class<T> configClass, T config) {
		Objects.requireNonNull(configClass);
		Objects.requireNonNull(config);

		
	}

	@Override
	public <T extends Config> T getConfig(Class<T> configClass) {
		Objects.requireNonNull(configClass);

		return configs.get(configClass);
	}

	private void notExists(Path path) {
		if(Files.notExists(path))
			try {
				Files.createDirectories(path);
			} catch(IOException e) {
				LOGGER.fatal(String.format("Error creating file '%s'", path.getFileName().toString()), e);
			}
	}
}