package net.wuffy.master;

import net.wuffy.common.config.Config;
import net.wuffy.common.config.IConfig;


@Config(path = "./wuffy/config.json", sourcePath = "/config/config.json")
public class MasterConfig implements IConfig {

	public String token;
}