package de.ngloader.common.util;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * @author Ingrim4
 */
public class CryptUtil {

	public static final SecureRandom SECURE_RANDOM = new SecureRandom();
	public static final Random RANDOM = new Random();

	public static KeyPair generateKeyPair() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(2048);
			return keyGen.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			throw new SecurityException("Failed to generate key pair", e);
		}
	}

	public static SecretKey generateSharedKey() {
		try {
			KeyGenerator gen = KeyGenerator.getInstance("AES");
			gen.init(128);
			return gen.generateKey();
		} catch (NoSuchAlgorithmException e) {
			throw new SecurityException("Failed to generate shared key.", e);
		}
	}

	public static PublicKey generatePublicKey(byte[] input) throws IOException {
		try {
			return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(input));
		} catch (GeneralSecurityException e) {
			throw new IOException("Could not generate public key from byte[]", e);
		}
	}

	public static PrivateKey generatePrivateKey(byte[] input) throws IOException {
		try {
			return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(input));
		} catch (GeneralSecurityException e) {
			throw new IOException("Could not generate private key from byte[]", e);
		}
	}

	public static Cipher createNetworkCipher(int opmode, Key key) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
			cipher.init(opmode, key, new IvParameterSpec(key.getEncoded()));
			return cipher;
		} catch (GeneralSecurityException e) {
			throw new SecurityException("Failed to create cipher", e);
		}
	}

	public static Cipher createCipher(int opmode, String transformation, Key key) {
		try {
			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(opmode, key);
			return cipher;
		} catch (GeneralSecurityException e) {
			throw new SecurityException("Failed to create cipher", e);
		}
	}

	public static byte[] crytpBytes(int opmode, Key key, byte[] input) {
		try {
			return createCipher(opmode, key.getAlgorithm(), key).doFinal(input);
		} catch (GeneralSecurityException e) {
			throw new SecurityException("Failed to run encryption", e);
		}
	}

	public static byte[] generateSignature(PrivateKey key, byte[] value) {
		try {
			Signature dsa = Signature.getInstance("SHA256withRSA");
			dsa.initSign(key);
			dsa.update(value);
			return dsa.sign();
		} catch (GeneralSecurityException e) {
			throw new SecurityException("Failed to generate signature", e);
		}
	}

	public static boolean isSignatureValid(PublicKey key, byte[] value, byte[] signature) {
		try {
			Signature sig = Signature.getInstance("SHA256withRSA");
			sig.initVerify(key);
			sig.update(value);
			return sig.verify(signature);
		} catch (GeneralSecurityException e) {
			throw new SecurityException("Failed to generate signature", e);
		}
	}

	public static byte[] computeDigest(String algorithm, byte[]...input) {
		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm);
			for(byte[] in : input)
				digest.update(in);
			return digest.digest();
		} catch (Exception e) {
			throw new SecurityException("Failed hash bytes", e);
		}
	}
}