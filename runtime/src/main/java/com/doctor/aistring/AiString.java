package com.doctor.aistring;

import android.os.Build;
import androidx.annotation.Keep;
import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

@RequiresApi(Build.VERSION_CODES.O)
@Keep
public class AiString {
    @Keep
    private static final String algorithm = "AES/ECB/PKCS5Padding";

    @Keep
    private static final String KEY = "cc4ee3123e7f31422cfcee4af92aaff5d69be7cb7a0cd3002b5794e1d8357e10";

    @Keep
    private static final ConcurrentHashMap<String, String> decryptCache = new ConcurrentHashMap<>();

    @Keep
    private static final SecretKeySpec keySpec;

    static {
        byte[] keyBytes = new byte[32];
        for (int i = 0; i < KEY.length(); i += 2) {
            keyBytes[i / 2] = (byte) Integer.parseInt(KEY.substring(i, i + 2), 16);
        }
        keySpec = new SecretKeySpec(keyBytes, algorithm);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Keep
    public static String decrypt(String base64Str, String key) {

        String cacheKey = base64Str + ":" + key;

        return decryptCache.computeIfAbsent(cacheKey, k -> {
            try {
                byte[] encryptedBytes = Base64.getDecoder().decode(base64Str);

                Cipher cipher = Cipher.getInstance(algorithm);
                SecretKeySpec currentKeySpec;
                if (key.equals(KEY)) {
                    currentKeySpec = keySpec;
                } else {
                    byte[] customKeyBytes = new byte[32];
                    for (int i = 0; i < key.length(); i += 2) {
                        customKeyBytes[i / 2] = (byte) Integer.parseInt(key.substring(i, i + 2), 16);
                    }
                    currentKeySpec = new SecretKeySpec(customKeyBytes, algorithm);
                }
                cipher.init(Cipher.DECRYPT_MODE, currentKeySpec);
                return new String(cipher.doFinal(encryptedBytes), StandardCharsets.UTF_8);
            } catch (Exception e) {
                // 如果AES解密失败，使用XOR解密
                try {
                    byte[] bytes = Base64.getDecoder().decode(base64Str);
                    byte[] result = new byte[bytes.length];
                    int keyHash = key.hashCode();
                    for (int i = 0; i < bytes.length; i++) {
                        result[i] = (byte) (bytes[i] ^ keyHash);
                    }
                    return new String(result, StandardCharsets.UTF_8);
                } catch (Exception e2) {
                    return base64Str;
                }
            }
        });
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Keep
    public static String decrypt(String base64Str) {
        return decrypt(base64Str, KEY);
    }


}