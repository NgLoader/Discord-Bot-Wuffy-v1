package de.ngloader.master.auth;

import java.io.IOException;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import de.ngloader.common.util.CryptUtil;

public class AuthManager {

	private static final Set<UUID> USED_IDS = new HashSet<UUID>();
	private static final Set<UUID> LOCK = new HashSet<UUID>();
	private static final Map<UUID, PublicKey> CREDENTIALS = new HashMap<UUID, PublicKey>();

	static {
		UUID uuid = UUID.fromString("b6b9e3a3-516e-40ba-9873-3119f54a2832");
		USED_IDS.add(uuid);
		try {
			CREDENTIALS.put(uuid, CryptUtil.generatePublicKey(Base64.getDecoder().decode("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsVrQ3OqYTuJtP2Zob7xdmli35byiEbxq6PA+lr43kXj2w7bGg+wXOy64yHSPHsi/jvdPkTVoU1ln+Xu4J5M/oVMfnxUe3FMaDpYXD82hg3nP27v8zCXW2zjOyfqzOcOVpxEVqyewYr+FsAnN9jUOvwBN5mKlO9YHPjvgET1Tk4gD4q4TjagmBpKMafhLTKtUJ3LJF6OMVXANJGlaxwXs8Z6sn6Y/AD0c7uB8WOmQwEADrPPs2XeeBHjx0cT8dIsBbFNP3dn1mgzb2nbCPNo0RwBi4V/nLoR3vKbspgYe00yqM8tZObmwW1z/kI4aR+H1E7cDCBRbfPtv0mIYolBc2QIDAQAB")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void clear() {
		USED_IDS.clear();
	}

	public static UUID generateId() {
		UUID id;
		do {
			id = UUID.randomUUID();
		} while(USED_IDS.contains(id));
		USED_IDS.add(id);
		return id;
	}

	public static void revokeId(UUID id) {
		if(!USED_IDS.remove(id))
			throw new IllegalArgumentException("Unknown id");
	}

	public static void lockId(UUID id) {
		LOCK.add(id);
	}

	public static void unlockId(UUID id) {
		LOCK.remove(id);
	}

	public static boolean verifyId(UUID id) {
		if(LOCK.contains(id))
			return false;
		return USED_IDS.contains(id);
	}

	public static boolean needsCredentials(UUID id) {
		return CREDENTIALS.containsKey(id);
	}

	public static PublicKey getPublicKey(UUID id) {
		return CREDENTIALS.get(id);
	}
}
