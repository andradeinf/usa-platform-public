package br.com.usasistemas.usaplatform.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;

import br.com.usasistemas.usaplatform.model.data.PasswordData;


public class PasswordUtil {

	public static PasswordData encrypt(String planPassword)
			throws Exception {

		PasswordData pd = new PasswordData();

		byte[] salt = new byte[8];
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.nextBytes(salt);

		byte[] hash = getHash(planPassword, salt);
		pd.setPasswordHash(Base64.encodeBase64String(hash));
		pd.setPasswordSalt(Base64.encodeBase64String(salt));

		return pd;
	}

	private static byte[] getHash(String password, byte[] salt)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {

		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		digest.reset();
		digest.update(salt);

		byte[] input = digest.digest(password.getBytes("UTF-8"));
		digest.reset();
		input = digest.digest(input);

		return input;

	}

	public static boolean authenticate(PasswordData userPassword,
			String planPassword) {

		byte[] digest = Base64.decodeBase64(userPassword.getPasswordHash());
		byte[] salt = Base64.decodeBase64(userPassword.getPasswordSalt());
		byte[] proposedDigest = null;

		try {
			proposedDigest = getHash(planPassword, salt);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace(); // TODO add some log information
			return false;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace(); // TODO add some log information
			return false;
		}

		return Arrays.equals(proposedDigest, digest);
	}

}
