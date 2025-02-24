package com.doctor.aistring

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class StringClassVisitor(
    classVisitor: ClassVisitor,
    private val encryptor: StringEncryptor,
    private val key: String
) : ClassVisitor(Opcodes.ASM9, classVisitor) {

    private var className: String = ""

    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        this.className = name
        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitField(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        value: Any?
    ): org.objectweb.asm.FieldVisitor {
        // 加密所有字符串常量，除了AiString类中的
        val newValue = if (value is String && 
            className != "com/doctor/aistring/AiString" && 
            !value.isEmpty() // 跳过空字符串
        ) {
            encryptor.encrypt(value, key)
        } else {
            value
        }
        
        return super.visitField(access, name, descriptor, signature, newValue)
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        if (mv == null || className == "com/doctor/aistring/AiString") {
            return mv
        }
        return StringMethodVisitor(mv, encryptor, key)
    }
}