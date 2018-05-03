package edu.buaa.sem.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

/**
 * 该类包含对称和非对称加密
 */
public class EncryptionUtils {
	private static String salt = "B18C75DF38";

	private static byte[] keyBytes = { 0x11, (byte) 0x88, 0x11, 0x40,
			(byte) 0xDD, 0x54, 0x66, 0x78 }; // 对称加密密钥

	/*------Spring Security 的加密实现 SHA-256-------*/
	private static final PasswordEncoder encoder = new StandardPasswordEncoder(
			salt);

	public static String encrypt(String rawPassword) {
		return encoder.encode(rawPassword);
	}

	public static boolean match(String rawPassword, String password) {
		return encoder.matches(rawPassword, password);
	}

	/*-----------------------------------------------*/

	// MD5加密
	public static String getMD5(String password) {
		StringBuffer buf = new StringBuffer("");
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			byte b[] = md.digest();
			int i;
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return buf.toString();// 32位字符
	}

	// 加盐MD5加密
	public static String getMD5Front(String frontParameter) {
		String userName = "38B01A12R9";
		Object userDetails = SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		if (userDetails instanceof UserDetails) {
			userName = ((UserDetails) userDetails).getUsername();
		}

		String saltString = salt + frontParameter + userName;
		return getMD5(saltString);
	}

	// 对称加密
	public static String encryptAES(String content) {
		try {

			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(keyBytes);
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, secureRandom);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			byte[] byteContent = content.getBytes("UTF-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(byteContent);
			return parseByte2HexStr(result); // 加密
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 对称解密
	public static String decryptAES(String content) {
		try {

			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(keyBytes);
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, secureRandom);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(parseHexStr2Byte(content));
			return new String(result, "UTF-8"); // 加密
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 将二进制转换成16进制
	private static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	// 将16进制转换为二进制
	private static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}
}
