package de.ngloader.api;

import java.util.List;

import de.ngloader.api.config.Config;

@Config(path = "./config.yml")
class WuffyConfig {

	String token;
	List<String> botAuthors;
}