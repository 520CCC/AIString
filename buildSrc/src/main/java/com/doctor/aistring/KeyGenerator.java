package com.doctor.aistring;

public class KeyGenerator {
    public static String generateKey() {
        byte[] key = new byte[32]; // 256位密钥
        new java.security.SecureRandom().nextBytes(key);
        StringBuilder hexString = new StringBuilder();
        for (byte b : key) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}