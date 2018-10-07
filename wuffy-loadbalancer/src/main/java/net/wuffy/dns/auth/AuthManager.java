package net.wuffy.dns.auth;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.PublicKey;
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

	static {
		KeyPair keyPair = CryptUtil.generateKeyPair();

		try (DataOutputStream outputStream = new DataOutputStream(Files.newOutputStream(Paths.get("wuffy/keys.key")))) {
			outputStream.write(keyPair.getPrivate().getEncoded());
		} catch (IOException e) {
			e.printStackTrace();
		}

		USED_UUIDS.add(UUID.fromString("7092e0bd-3202-4b36-b5d8-21b9771a7b27"));
		CREDENTIALS.put(UUID.fromString("7092e0bd-3202-4b36-b5d8-21b9771a7b27"), keyPair.getPublic());
	}

	public static void load() {
		try (Stream<Path> paths = Files.walk(Paths.get("wuffy/keys"))) {
			paths.filter(path -> Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS))
				.forEach(path -> {
					try (DataInputStream inputStream = new DataInputStream(Files.newInputStream(path))) {
						UUID uuid = new UUID(inputStream.readLong(), inputStream.readLong());

						AuthManager.USED_UUIDS.add(uuid);
						AuthManager.CREDENTIALS.put(uuid, CryptUtil.generatePublicKey(inputStream.readAllBytes()));

						Logger.debug("Auth", String.format("Loaded ID: \"%s\"", uuid.toString()));
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void add(UUID uuid, PublicKey publicKey) {
		
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
}