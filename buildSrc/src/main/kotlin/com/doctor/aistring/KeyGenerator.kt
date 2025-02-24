package com.doctor.aistring

import java.io.File
import java.security.SecureRandom
import java.util.Base64

object KeyGenerator {
    fun generateKey(): String {
        val key = ByteArray(32) // 256位密钥
        java.security.SecureRandom().nextBytes(key)
        return key.joinToString("") { "%02x".format(it) } // 转换为十六进制字符串
    }
}

