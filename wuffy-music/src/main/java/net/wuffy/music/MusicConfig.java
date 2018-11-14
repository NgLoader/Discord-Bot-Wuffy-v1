package net.wuffy.music;

import net.wuffy.common.config.Config;
import net.wuffy.common.config.IConfig;

@Config(path = "./wuffy/config.json", sourcePath = "/config/config.json")
public class MusicConfig implements IConfig {

	public String masterAddress;
	public Integer masterPort;

	public String keyCertChainFile;
	public String keyFile;
}