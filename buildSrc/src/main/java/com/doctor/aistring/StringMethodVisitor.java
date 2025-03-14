package com.doctor.aistring;

import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

import java.util.Base64;
import java.util.List;

public class StringMethodVisitor extends AdviceAdapter {
    private final StringEncryptor encryptor;
    private final String key;
    private final String className;
    private final List<String> targetPackages;
    private static final Logger logger = Logging.getLogger(StringMethodVisitor.class);

    public StringMethodVisitor(
            MethodVisitor methodVisitor,
            StringEncryptor encryptor,
            String key,
            List<String> targetPackages,
            int access,
            String name,
            String descriptor,
            String className
    ) {
        super(Opcodes.ASM9, methodVisitor, access, name, descriptor);
        this.encryptor = encryptor;
        this.key = key;
        this.targetPackages = targetPackages;
        this.className = className;
    }

    private boolean shouldEncrypt(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        
        // 只加密指定包名下的代码
        if (this.className != null && targetPackages != null) {
            return targetPackages.stream().anyMatch(pkg -> this.className.contains(pkg));
        }
        
        return false;
    }

    private String encryptString(String value) {
        try {
            byte[] encrypted = encryptor.encrypt(value, key);
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            logger.warn("[StringMethodVisitor] 加密失败: {}", e.getMessage());
            return value;
        }
    }

    @Override
    public void visitLdcInsn(Object value) {
        if (value instanceof String && !((String) value).isEmpty() && shouldEncrypt((String) value)) {
            try {
                String processedValue = ((String) value).replace("\n", "\\n");
                String base64Str = encryptString(processedValue);
//                logger.warn("字符串加密: 原始值='{}', 加密后='{}'" , processedValue, base64Str);
                super.visitLdcInsn(base64Str);
                super.visitLdcInsn(key);
                super.visitMethodInsn(
                        Opcodes.INVOKESTATIC,
                        "com/doctor/aistring/AiString",
                        "decrypt",
                        "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;",
                        false
                );


            } catch (Exception e) {
                logger.warn("[StringMethodVisitor] 处理字符串时发生错误: {}", e.getMessage());
                super.visitLdcInsn(value);
            }
        } else {
            super.visitLdcInsn(value);
        }
    }
}