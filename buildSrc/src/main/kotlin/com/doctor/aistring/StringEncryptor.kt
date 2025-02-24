package com.doctor.aistring

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import java.security.MessageDigest
import java.util.Base64
import java.io.Serializable
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

class StringEncryptor : Serializable {
    @Transient
    private val algorithm = "AES/ECB/PKCS5Padding"
    @Transient
    private val logger: Logger = Logging.getLogger(StringEncryptor::class.java)
    
    fun encrypt(input: String, key: String): ByteArray {
        try {
            // 将十六进制密钥转换为字节数组
            val keyBytes = key.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
            val keySpec = SecretKeySpec(keyBytes, "AES")
            
            val cipher = Cipher.getInstance(algorithm)
            cipher.init(Cipher.ENCRYPT_MODE, keySpec)
            
            logger.lifecycle("使用密钥: $key")
            logger.lifecycle("密钥字节数组: ${keyBytes.joinToString(",")}")
            
            // 使用 UTF-8 编码
            val result = cipher.doFinal(input.toByteArray(Charsets.UTF_8))
            logger.lifecycle("加密结果: ${result.joinToString(",")}")
            return result
        } catch (e: Exception) {
            logger.error("AES加密失败，使用XOR加密", e)
            // 如果AES加密失败，回退到简单的XOR加密
            return input.toByteArray(Charsets.UTF_8).map { 
                it.toInt() xor key.hashCode() 
            }.map { it.toByte() }.toByteArray()
        }
    }
    
    private fun generateKey(key: String): SecretKeySpec {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = key.toByteArray(Charsets.UTF_8)
        val hash = digest.digest(bytes)
        return SecretKeySpec(hash.copyOf(16), "AES")
    }
}