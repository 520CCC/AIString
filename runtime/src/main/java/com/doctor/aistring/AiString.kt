package com.doctor.aistring

import android.os.Build
import androidx.annotation.Keep
import androidx.annotation.RequiresApi
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import java.security.MessageDigest
import java.util.Base64
import java.util.concurrent.ConcurrentHashMap


@RequiresApi(Build.VERSION_CODES.O)
@Keep
object AiString {
    @Keep
    private const val algorithm = "AES/ECB/PKCS5Padding"
    
    @Keep
    private const val KEY: String = "6a7934426925a01eb384dce1f6fef4989f12b241559a1bd40e8a70fe7461ea3a" // 这里会被替换为十六进制字符串

    private val decryptCache = ConcurrentHashMap<String, String>()
    
    private val keySpec by lazy {
        android.util.Log.d("AiString", "初始化keySpec，KEY=$KEY")
        val keyBytes = KEY.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
        android.util.Log.d("AiString", "密钥字节数组: ${keyBytes.joinToString(",")}")
        SecretKeySpec(keyBytes, algorithm)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Keep
    @JvmStatic
    fun decrypt(base64Str: String, key: String): String {
        android.util.Log.d("AiString", "开始解密，输入=$base64Str，密钥=$key")
        val cacheKey = "$base64Str:$key"
        return decryptCache.getOrPut(cacheKey) {
            try {
                val encryptedBytes = Base64.getDecoder().decode(base64Str)
                android.util.Log.d("AiString", "Base64解码后: ${encryptedBytes.joinToString(",")}")
                
                val cipher = Cipher.getInstance(algorithm)
                val currentKeySpec = if (key == KEY) {
                    android.util.Log.d("AiString", "使用默认密钥: $KEY")
                    android.util.Log.d("AiString", "默认密钥字节数组: ${KEY.chunked(2).map { it.toInt(16).toByte() }.joinToString(",")}")
                    keySpec
                } else {
                    android.util.Log.d("AiString", "使用自定义密钥: $key")
                    val keyBytes = key.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
                    android.util.Log.d("AiString", "自定义密钥字节数组: ${keyBytes.joinToString(",")}")
                    SecretKeySpec(keyBytes, algorithm)
                }
                cipher.init(Cipher.DECRYPT_MODE, currentKeySpec)
                val result = String(cipher.doFinal(encryptedBytes), Charsets.UTF_8)
                android.util.Log.d("AiString", "解密结果: $result")
                result
            } catch (e: Exception) {
                android.util.Log.e("AiString", "AES解密失败，使用XOR解密", e)
                // 如果AES解密失败，使用XOR解密
                try {
                    val bytes = Base64.getDecoder().decode(base64Str)
                    String(bytes.map { 
                        it.toInt() xor key.hashCode() 
                    }.map { it.toByte() }.toByteArray(), Charsets.UTF_8)
                } catch (e2: Exception) {
                    android.util.Log.e("AiString", "XOR解密也失败", e2)
                    base64Str
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Keep
    @JvmStatic
    fun decrypt(base64Str: String): String {
        return decrypt(base64Str, KEY)
    }
}