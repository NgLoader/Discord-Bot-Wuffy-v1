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

import de.ngloader.api.WuffyServer;
import de.ngloader.api.config.Config;
import de.ngloader.api.config.IConfig;
import de.ngloader.api.config.IConfigService;
import de.ngloader.api.logger.ILogger;
import de.ngloader.api.util.GsonUtil;

public class ConfigService implements IConfigService {

	private static final ILogger LOGGER = WuffyServer.getLogger();

	private final Map<Class<? extends IConfig>, IConfig> configs = new HashMap<Class<? extends IConfig>, IConfig>();

	@Override
	public <T extends IConfig> void loadConfig(Class<? extends T> configClass) {
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
				LOGGER.fatal("Failed to create " + config.sourcePath(), e);
			}

		try (BufferedReader reader = Files.newBufferedReader(path)) {
			configs.put(configClass, GsonUtil.GSON_PRETTY_PRINTING.fromJson(reader, configClass));
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.fatal(String.format("Error loading file '%s'", config.path()), e);
		}
	}

	@Override
	public <T extends IConfig> void saveConfig(Class<T> configClass, T config) {
		Objects.isNull(configClass);
		Objects.isNull(config);
	}

	@Override
	public <T extends IConfig> T getConfig(Class<? extends T> configClass) {
		Objects.isNull(configClass);

		if(!configs.containsKey(configClass))
			loadConfig(configClass);

		return configClass.cast(configs.get(configClass));
	}

//	@Override
//	public <T extends Config> void loadConfig(T configClass) {
//		Objects.requireNonNull(configClass);
//
//		Config config = configClass.getClass().getAnnotation(Config.class);
//		Path path = Paths.get(config.path());
//
//		if(Files.notExists(path))
//			try {
//				Files.createDirectories(path.getParent());
//
//				try {
//					Files.copy(ConfigService.class.getResourceAsStream(config.sourcePath()), path);
//				} catch(NullPointerException e) {
//					try (BufferedWriter writer = Files.newBufferedWriter(path)) {
//						writer.write(GsonUtil.GSON_PRETTY_PRINTING.toJson(configClass));
//					} catch(IOException ioe) {
//						ioe.printStackTrace();
//						LOGGER.fatal(String.format("Error saving file '%s'", config.path()), ioe);
//					}
//				}
//			} catch(IOException e) {
//				e.printStackTrace();
//				LOGGER.fatal("Failed to create " + config.sourcePath(), e);
//			}
//
//		try (BufferedReader reader = Files.newBufferedReader(path)) {
//			configs.put(configClass, GsonUtil.GSON.fromJson(reader, TypeToken.getParameterized(ArrayList.class, configClass).getType()));
//		} catch (IOException e) {
//			e.printStackTrace();
//			LOGGER.fatal(String.format("Error loading file '%s'", config.path()), e);
//		}
//	}
//
//	@Override
//	public <T extends Config> void saveConfig(T configClass, T config) {
//		Objects.requireNonNull(configClass);
//		Objects.requireNonNull(config);
//
//		Config configConfig = configClass.getClass().getAnnotation(Config.class);
//		Path path = Paths.get(configConfig.path());
//
//		if(Files.notExists(path))
//			try {
//				Files.createDirectories(path.getParent());
//				Files.createFile(path);
//			} catch(IOException e) {
//				LOGGER.fatal("Failed to create " + configConfig.sourcePath(), e);
//			}
//
//		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
//			writer.write(GsonUtil.GSON_PRETTY_PRINTING.toJson(config));
//		} catch(IOException e) {
//			e.printStackTrace();
//			LOGGER.fatal(String.format("Error saving file '%s'", configConfig.path()), e);
//		}
//	}
//
//	@Override
//	public <T extends Config> T getConfig(Class<T> configClass) {
//		Objects.requireNonNull(configClass);
//
//		if(!this.configs.containsKey(configClass))
//			loadConfig(configClass);
//
//		return configClass.cast(configs.get(configClass));
//	}
}