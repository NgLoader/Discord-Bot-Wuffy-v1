package net.wuffy.bot.module;

import java.util.HashMap;
import java.util.Map;

import net.wuffy.bot.module.modules.command.ModuleCommand;
import net.wuffy.common.WuffyPhantomRefernce;
import net.wuffy.common.logger.Logger;
import net.wuffy.common.util.IWuffyPhantomReference;
import net.wuffy.core.event.EventGuild;

public class GuildModule<T extends EventGuild> implements IWuffyPhantomReference {

	private static final Map<EnumModuleType, Class<? extends Module>> MODULE_CLASSES = new HashMap<EnumModuleType, Class<? extends Module>>();

	static {
		MODULE_CLASSES.put(EnumModuleType.COMMAND, ModuleCommand.class);
	}

	public static Class<? extends Module> getModuleClass(EnumModuleType moduleType) {
		return GuildModule.MODULE_CLASSES.get(moduleType);
	}

	private final Map<EnumModuleType, Module> modules = new HashMap<EnumModuleType, Module>();

	private final T guild;

	private GuildModule(T guild) {
		this.guild = guild;

		WuffyPhantomRefernce.getInstance().add(this, String.format("GuildModule - %s", this.guild != null ? Long.toString(this.guild.getIdLong()) : "UNKNOWN"));
	}

	public void initialize() {
		//TODO call initialize
	}

	public boolean enable(Module module) {
		return this.enable(module.getType());
	}

	public boolean enable(EnumModuleType enumModuleType) {
		if(this.modules.containsKey(enumModuleType))
			return false;

		try {
			Module module = GuildModule.getModuleClass(enumModuleType).getConstructor().newInstance(this.guild);

			this.modules.put(enumModuleType, module);

			module.onEnable();

			Logger.debug("GuildModule", String.format("Enable module \"%s\" for guild \"%s\"", enumModuleType.name(), Long.toString(this.guild.getIdLong())));
			return true;
		} catch(Exception e) {
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

		Logger.debug("GuildModule", String.format("Disable module \"%s\" for guild \"%s\"", enumModuleType.name(), Long.toString(this.guild.getIdLong())));
	}

	public boolean isEnabled(Module module) {
		return this.isEnabled(module.getType());
	}

	public boolean isEnabled(EnumModuleType enumModuleType) {
		return this.modules.containsKey(enumModuleType);
	}

	public T getGuild() {
		return this.guild;
	}
}