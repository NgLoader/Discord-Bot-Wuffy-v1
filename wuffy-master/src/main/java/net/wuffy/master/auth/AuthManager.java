package net.wuffy.master.auth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import net.wuffy.common.logger.Logger;
import net.wuffy.common.util.CryptUtil;

public class AuthManager {

	private static final Set<UUID> USED_UUIDS = new HashSet<UUID>();
	private static final Set<UUID> LOCK = new HashSet<UUID>();
	private static final Map<UUID, PublicKey> CREDENTIALS = new HashMap<UUID, PublicKey>();
	private static final Map<UUID, String> NAMES = new HashMap<UUID, String>();

	private static boolean initialized = false;

	public static void initialize() {
		if(AuthManager.initialized)
			throw new Error("AuthManager already initialized!");
		AuthManager.initialized = true;

		Path keyPath = Paths.get("wuffy/keys");

		if(Files.exists(keyPath))
			try (Stream<Path> paths = Files.walk(keyPath)) {
				paths.filter(path -> Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS))
					.forEach(path -> {
						try (DataInputStream inputStream = new DataInputStream(Files.newInputStream(path))) {
							UUID uuid = new UUID(inputStream.readLong(), inputStream.readLong());
	
							AuthManager.USED_UUIDS.add(uuid);
							AuthManager.NAMES.put(uuid, inputStream.readUTF());
							AuthManager.CREDENTIALS.put(uuid, CryptUtil.generatePublicKey(inputStream.readAllBytes()));
	
							Logger.debug("Auth", String.format("Loaded ID: \"%s\"", uuid.toString()));
						} catch (IOException e) {
							e.printStackTrace();
						}
					});
	
				Logger.info("AuthManager", "Initialized.");
			} catch (IOException e) {
				Logger.fatal("AuthManager", "Failed by initializing.", e);
			}
	}

	public static void saveId(UUID uuid, PublicKey publicKey, String name) {
		try {
			Files.createDirectories(Paths.get("wuffy/keys"));
		} catch (IOException e) {
			Logger.fatal("Command", "Failed by creating path \"./wuffy/keys/\"", e);
		}

		try (DataOutputStream outputStream = new DataOutputStream(Files.newOutputStream(Paths.get(String.format("wuffy/keys/%s.key", uuid.toString()))))) {
			outputStream.writeLong(uuid.getMostSignificantBits());
			outputStream.writeLong(uuid.getLeastSignificantBits());
			outputStream.writeUTF(name);
			outputStream.write(publicKey.getEncoded());

			outputStream.close();

			if(!AuthManager.USED_UUIDS.contains(uuid))
				AuthManager.USED_UUIDS.add(uuid);

			AuthManager.CREDENTIALS.put(uuid, publicKey);
			AuthManager.NAMES.put(uuid, name);
		} catch (IOException e) {
			Logger.fatal("AuthManager", String.format("Failed to save id \"%s\".", uuid.toString()), e);
		}
	}

	public static void deleteId(UUID uuid) {
		Path path = (Paths.get(String.format("wuffy/keys/%s.key", uuid.toString())));

		if(Files.exists(path))
			try {
				Files.delete(path);
			} catch (IOException e) {
				Logger.fatal("AuthManager", String.format("Failed by deleting ID \"%s\"", uuid.toString()), e);
			}

		if(AuthManager.CREDENTIALS.containsKey(uuid))
			AuthManager.CREDENTIALS.remove(uuid);

		if(AuthManager.LOCK.contains(uuid))
			AuthManager.LOCK.remove(uuid);

		if(AuthManager.USED_UUIDS.contains(uuid))
			AuthManager.USED_UUIDS.remove(uuid);
	}

	public static UUID generateId() {
		UUID uuid;

		do {
			uuid = UUID.randomUUID();
		} while(AuthManager.USED_UUIDS.contains(uuid));

		AuthManager.USED_UUIDS.add(uuid);
		return uuid;
	}

	public static void revokeId(UUID id) {
		if(!AuthManager.USED_UUIDS.remove(id))
			throw new IllegalArgumentException("Unknown id");
	}

	public static void lockId(UUID id) {
		AuthManager.LOCK.add(id);
	}

	public static void unlockId(UUID id) {
		AuthManager.LOCK.remove(id);
	}

	public static boolean verifyId(UUID id) {
		if(AuthManager.LOCK.contains(id))
			return false;
		return AuthManager.USED_UUIDS.contains(id);
	}

	public static boolean hasCredentials(UUID id) {
		return AuthManager.CREDENTIALS.containsKey(id);
	}

	public static PublicKey getPublicKey(UUID id) {
		return AuthManager.CREDENTIALS.get(id);
	}

	public static String getName(UUID id) {
		return AuthManager.NAMES.get(id);
	}

	public static Set<UUID> getAllUsedIds() {
		return Collections.unmodifiableSet(AuthManager.USED_UUIDS);
	}
}