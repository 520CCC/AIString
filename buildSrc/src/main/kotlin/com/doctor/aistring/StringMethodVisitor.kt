package com.doctor.aistring

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter
import java.util.Base64
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

class StringMethodVisitor(
    methodVisitor: MethodVisitor,
    private val encryptor: StringEncryptor,
    private val key: String,
    access: Int,
    name: String,
    descriptor: String
) : AdviceAdapter(Opcodes.ASM9, methodVisitor, access, name, descriptor) {

    private val logger: Logger = Logging.getLogger(StringMethodVisitor::class.java)

    override fun visitLdcInsn(value: Any?) {
        if (value is String && value.isNotEmpty()) {
            try {
                logger.lifecycle("原始字符串: $value")
                logger.lifecycle("使用密钥: $key")
                
                val encrypted = encryptor.encrypt(value, key)
                logger.lifecycle("加密后字节数组: ${encrypted.joinToString(",")}")
                
                val base64Str = Base64.getEncoder().encodeToString(encrypted)
                logger.lifecycle("Base64编码后: $base64Str")
                
                // 加载解密方法所需的参数
                super.visitLdcInsn(base64Str)
                super.visitLdcInsn(key)
                // 调用解密方法
                super.visitMethodInsn(
                    Opcodes.INVOKESTATIC,
                    "com/doctor/aistring/AiString",
                    "decrypt",
                    "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;",
                    false
                )
                logger.lifecycle("完成字符串处理")
            } catch (e: Exception) {
                logger.error("处理字符串时出错", e)
                super.visitLdcInsn(value)
            }
        } else {
            super.visitLdcInsn(value)
        }
    }
}