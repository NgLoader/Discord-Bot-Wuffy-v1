package net.wuffy.master;

import net.wuffy.common.config.Config;
import net.wuffy.common.config.IConfig;


@Config(path = "./wuffy/config.json", sourcePath = "/config/config.json")
public class MasterConfig implements IConfig {

	public String botToken;
	public String masterAddress;
	public Integer masterPort;

	public String loadBalancerAddress;
	public Integer loadBalancerPort;

	public String keyCertChainFile;
	public String keyFile;

	public Integer minShardCount;
}