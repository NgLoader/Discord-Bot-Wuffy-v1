package net.wuffy.dns;

import java.util.List;

import net.wuffy.common.config.Config;
import net.wuffy.common.config.IConfig;

@Config(path = "./wuffy/config.json", sourcePath = "/config/config.json")
public class DNSConfig implements IConfig {

	public String host;
	public Integer port;

	public String recordName;
	public Long recordTimeToLive;

	public List<String> adresses;
}