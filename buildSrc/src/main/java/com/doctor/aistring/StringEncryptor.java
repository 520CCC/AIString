package com.doctor.aistring;

import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class StringEncryptor implements Serializable {
    private transient final String algorithm = "AES/ECB/PKCS5Padding";
    private static final Logger logger = Logging.getLogger(StringEncryptor.class);

    public byte[] encrypt(String input, String key) {
        try {
            // 将十六进制密钥转换为字节数组
            byte[] keyBytes = new byte[32];
            for (int i = 0; i < key.length(); i += 2) {
                keyBytes[i / 2] = (byte) Integer.parseInt(key.substring(i, i + 2), 16);
            }
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, algorithm);

            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            // 使用 UTF-8 编码
            return cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            // 如果AES加密失败，回退到简单的XOR加密
            byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
            byte[] result = new byte[inputBytes.length];
            int keyHash = key.hashCode();
            for (int i = 0; i < inputBytes.length; i++) {
                result[i] = (byte) (inputBytes[i] ^ keyHash);
            }
            return result;
        }
    }
}