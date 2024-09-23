package com.server.account.Configurations;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AESCrypto {
	
	Logger log = (Logger) LoggerFactory.getLogger(this.getClass());
	
	private String key;
	private String trans;
	private static byte[] iv;
	private static Key keySpec;
	
	public void init() {
		try {
			this.key = "KEY VALUE";
			this.trans = "AES/ECB/PKCS5Padding";
			this.iv = org.apache.commons.codec.binary.Base64.decodeBase64("KEY VALUE");
			byte[] keyBytes = new byte[32];
			int len = 0;
			byte[] b = null;
			try {
				b = key.getBytes("UTF-8");
				len = b.length;
				if (len > keyBytes.length) {
					len = keyBytes.length;
				}
			} catch (UnsupportedEncodingException e) {
				log.info(">> Init Error: {}", e.getMessage());
			}
			System.arraycopy(b, 0, keyBytes, 0, len);
			this.keySpec = new SecretKeySpec(keyBytes, "AES");
		} catch (Exception e) {
			log.info(">> Init Error: {}", e.getMessage());
		}
	}

	// 암호화
	public String aesEncode(String str) {
		if(str != null) {
			
			init();
			Cipher c;
			byte[] encrypted = null;
			try {
				c = Cipher.getInstance(trans);
				if (trans.equals("AES/ECB/PKCS5Padding")) {
					c.init(Cipher.ENCRYPT_MODE, keySpec);
				} else {
					c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv));
				}
				encrypted = c.doFinal(str.getBytes(StandardCharsets.UTF_8));
			} catch (NoSuchAlgorithmException e) {
				log.info(">> Encodeing Error: {}", e.getMessage());
			} catch (NoSuchPaddingException e) {
				log.info(">> Encodeing Error: {}", e.getMessage());
			} catch (InvalidKeyException e) {
				log.info(">> Encodeing Error: {}", e.getMessage());
			} catch (InvalidAlgorithmParameterException e) {
				log.info(">> Encodeing Error: {}", e.getMessage());
			} catch (IllegalBlockSizeException e) {
				log.info(">> Encodeing Error: {}", e.getMessage());
			} catch (BadPaddingException e) {
				log.info(">> Encodeing Error: {}", e.getMessage());
			}
			return new String(Base64.getEncoder().encodeToString(encrypted));
		}else {
			return "";
		}
	}

	// 복호화
	public String aesDecode(String str) {
		if(str != null) {
			init();
			String decrypt = "";
			String encodedText = str.replace(" ", "+");
			Cipher c;
			try {
				c = Cipher.getInstance(trans);
				if (trans.equals("AES/ECB/PKCS5Padding")) {
					c.init(Cipher.DECRYPT_MODE, keySpec);
				} else {
					c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv));
				}
				byte[] byteStr = Base64.getDecoder().decode(encodedText);
				decrypt = new String(c.doFinal(byteStr), StandardCharsets.UTF_8);
			} catch (NoSuchAlgorithmException e) {
				log.info(">> Decodeing Error: {}", e.getMessage());
			} catch (NoSuchPaddingException e) {
				log.info(">> Decodeing Error: {}", e.getMessage());
			} catch (InvalidKeyException e) {
				log.info(">> Decodeing Error: {}", e.getMessage());
			} catch (InvalidAlgorithmParameterException e) {
				log.info(">> Decodeing Error: {}", e.getMessage());
			} catch (IllegalBlockSizeException e) {
				log.info(">> Decodeing Error: {}", e.getMessage());
			} catch (BadPaddingException e) {
				log.info(">> Decodeing Error: {}", e.getMessage());
			} catch (IllegalArgumentException e) {
				log.info(">> Decodeing Error: {}", e.getMessage());
			}
			return decrypt;
			
		}else {
			return "";
		}
	}

}
