package net.wuffy.bot.module;

import java.util.HashMap;
import java.util.Map;

import net.wuffy.bot.database.DBGuild;
import net.wuffy.bot.module.modules.command.ModuleCommand;
import net.wuffy.common.WuffyPhantomRefernce;
import net.wuffy.common.logger.Logger;
import net.wuffy.common.util.IWuffyPhantomReference;

public class GuildModule implements IWuffyPhantomReference {

	private static final Map<EnumModuleType, Class<? extends Module>> MODULE_CLASSES = new HashMap<EnumModuleType, Class<? extends Module>>();

	static {
		MODULE_CLASSES.put(EnumModuleType.COMMAND, ModuleCommand.class);
	}

	public static Class<? extends Module> getModuleClass(EnumModuleType moduleType) {
		return GuildModule.MODULE_CLASSES.get(moduleType);
	}

	private final Map<EnumModuleType, Module> modules = new HashMap<EnumModuleType, Module>();

	private DBGuild guild;

	private GuildEventManager guildModuleListenerAdapter;

	private Boolean initialized = false;

	public GuildModule(DBGuild guild) {
		this.guild = guild;

		WuffyPhantomRefernce.getInstance().add(this, String.format("GuildModule - %s", this.guild.getJDA() != null ? this.guild.getJDA().getShardInfo().getShardString() : "UNKNOWN"));
	}

	public boolean initialize() {
		if(this.initialized)
			return false;
		this.initialized = true;

		this.guildModuleListenerAdapter = new GuildEventManager(this);

		this.enable(EnumModuleType.COMMAND);
		//TODO call initialize
		return true;
	}

	public void destroy() {
		this.modules.values().forEach(Module::destroy);
		this.modules.clear();

		this.guildModuleListenerAdapter.destroy();

		this.guild = null;
		this.guildModuleListenerAdapter = null;
	}

	public boolean enable(Module module) {
		return this.enable(module.getType());
	}

	public boolean enable(EnumModuleType enumModuleType) {
		if(this.modules.containsKey(enumModuleType))
			return false;

		try {
			Module module = GuildModule.getModuleClass(enumModuleType).getConstructor(GuildModule.class).newInstance(this);

			this.modules.put(enumModuleType, module);

			module.onEnable();

			Logger.debug("GuildModule", String.format("Enable module \"%s\" for guild \"%s\"", enumModuleType.name(), this.getGuild().getJDA().getShardInfo().getShardString()));
			return true;
		} catch(Exception e) {
			Logger.fatal("GuildModule", String.format("Errror by enableing module \"%s\" for guild \"%s\"", enumModuleType.name(), this.getGuild().getJDA().getShardInfo().getShardString()), e);
			return false;
		}
	}

	public void disable(Module module) {
		this.disable(module.getType());
	}

	public void disable(EnumModuleType enumModuleType) {
		if(!this.modules.containsKey(enumModuleType))
			return;

		Module module = this.modules.remove(enumModuleType);

		if(module != null)
			module.onDisable();

		Logger.debug("GuildModule", String.format("Disable module \"%s\" for guild \"%s\"", enumModuleType.name(), this.getGuild().getJDA().getShardInfo().getShardString()));
	}

	public boolean isEnabled(Module module) {
		return this.isEnabled(module.getType());
	}

	public boolean isEnabled(EnumModuleType enumModuleType) {
		return this.modules.containsKey(enumModuleType);
	}

	public GuildEventManager getGuildModuleListenerAdapter() {
		return this.guildModuleListenerAdapter;
	}

	public DBGuild getGuild() {
		return this.guild;
	}
}