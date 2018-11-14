package net.wuffy.bootstrap;

import java.util.List;

import net.wuffy.common.config.Config;
import net.wuffy.common.config.IConfig;

@Config(path = "./wuffy/config.json", sourcePath = "/config/config.json")
public class GlobalConfig implements IConfig {

	public Boolean useMasterSystem;

	public List<Long> admins;
}