package com.doctor.aistring;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class StringClassVisitor extends ClassVisitor {
    private final StringEncryptor encryptor;
    private final String key;

    public StringClassVisitor(ClassVisitor classVisitor, StringEncryptor encryptor, String key) {
        super(Opcodes.ASM9, classVisitor);
        this.encryptor = encryptor;
        this.key = key;
    }

    private String className;

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.className = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (methodVisitor != null) {
            return new StringMethodVisitor(methodVisitor, encryptor, key, access, name, descriptor, className);
        }
        return null;
    }
}