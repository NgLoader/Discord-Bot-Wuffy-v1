package de.ngloader.api;

import java.util.List;

import de.ngloader.api.config.Config;
import de.ngloader.api.config.IConfig;

@Config(path = "./wuffy/config.json", sourcePath = "/config.json")
public class WuffyConfig implements IConfig {

	String token;
	List<String> botAuthors;
}