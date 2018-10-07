package net.wuffy.dns;

import net.wuffy.common.config.Config;
import net.wuffy.common.config.IConfig;

@Config(path = "./wuffy/config.json", sourcePath = "/config/config.json")
public class DNSConfig implements IConfig {

	public DNS dns;
	public Master master;

	public class DNS {
		public String host;
		public Integer port;

		public String recordName;
		public Long recordTimeToLive;
	}

	public class Master {
		public String host;
		public Integer port;

		public Integer reconnectTrys;

		public String keyCertChainFile;
		public String keyFile;
	}
}