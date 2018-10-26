package net.wuffy.loadbalancer.command;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.util.Set;
import java.util.UUID;

import net.wuffy.common.logger.Logger;
import net.wuffy.common.util.CryptUtil;
import net.wuffy.console.ConsoleCommand;
import net.wuffy.console.ConsoleCommandResult;
import net.wuffy.console.IConsoleCommandExecutor;
import net.wuffy.loadbalancer.auth.AuthManager;

@ConsoleCommand(usage = "master <Create|Remove|List> [ID]", aliases = { "master" })
public class CommandMaster implements IConsoleCommandExecutor {

	@Override
	public ConsoleCommandResult onCommand(String[] args) {
		if(args.length > 0) {
			if(args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("c")) {
				Logger.info("Command", "Searching free id.");
				UUID id = AuthManager.generateId();
				Logger.info("Command", String.format("Free id found \"%s\"", id.toString()));

				Logger.info("Command", "Generating Public and Private key...");
				KeyPair keyPair = CryptUtil.generateKeyPair();
				Logger.info("Command", "Generated.");

				Logger.info("Command", "Saving...");
				AuthManager.saveId(id, keyPair.getPublic());

				try {
					Files.createDirectories(Paths.get("wuffy"));
				} catch (IOException e) {
					Logger.fatal("Command", "Failed by creating path \"./wuffy/\"", e);
				}

				try (DataOutputStream outputStream = new DataOutputStream(Files.newOutputStream(Paths.get(String.format("wuffy/%s#lb.key", id.toString()))))) {
					outputStream.writeLong(id.getMostSignificantBits());
					outputStream.writeLong(id.getLeastSignificantBits());
					outputStream.write(keyPair.getPrivate().getEncoded());

					outputStream.close();
					Logger.info("Command", "Saved.");

					Logger.info("Command", String.format("Login data saved in \"wuffy/%s.key\" copy this file into the Master server.", id.toString()));
				} catch (IOException e) {
					Logger.fatal("AuthManager", String.format("Failed to save id \"%s\".", id.toString()), e);
				}
				return ConsoleCommandResult.SUCCESS;
			} else if(args.length > 1 && (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("r"))) {
				UUID id = null;

				try {
					id = UUID.fromString(args[1]);
				} catch(IllegalArgumentException e) {
					Logger.info("Command", "ID was not a vailed UUID!");
				}

				if(AuthManager.verifyId(id)) {
					AuthManager.deleteId(id);
					Logger.info("Command", String.format("Successful removed ID \"%s\".", id.toString()));
				} else
					Logger.info("Command", String.format("ID \"%s\" was not found.", id.toString()));

				return ConsoleCommandResult.SUCCESS;
			} else if(args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("l")) {
				Logger.info("Command", "[] --- --- --- { Id's } --- --- --- []");
				Logger.info("Command", "");

				Set<UUID> set = AuthManager.getAllUsedIds();

				if(set.isEmpty())
					Logger.info("Command", "No ID's exist.");
				else
					set.forEach(id -> Logger.info("Command", String.format("    - %s", id.toString())));

				Logger.info("Command", "");
				Logger.info("Command", "[] --- --- --- { Id's } --- --- --- []");
				return ConsoleCommandResult.SUCCESS;
			}
		}
		return ConsoleCommandResult.SYNTAX;
	}
}