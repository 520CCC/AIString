package com.doctor.aistring

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import java.security.MessageDigest
import java.util.Base64

object StringDecryptor {
    private const val algorithm = "AES"
    
    @JvmStatic
    fun decrypt(encryptedBase64: String, key: String): String {
        try {
            val encrypted = Base64.getDecoder().decode(encryptedBase64)
            val keySpec = generateKey(key)
            val cipher = Cipher.getInstance(algorithm)
            cipher.init(Cipher.DECRYPT_MODE, keySpec)
            return String(cipher.doFinal(encrypted))
        } catch (e: Exception) {
            // 如果AES解密失败，回退到简单的XOR解密
            val encrypted = Base64.getDecoder().decode(encryptedBase64)
            return String(encrypted.map { it.toInt() xor key.hashCode() }
                .map { it.toByte() }.toByteArray())
        }
    }
    
    private fun generateKey(key: String): SecretKeySpec {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = key.toByteArray()
        val hash = digest.digest(bytes)
        return SecretKeySpec(hash.copyOf(16), algorithm)
    }
}