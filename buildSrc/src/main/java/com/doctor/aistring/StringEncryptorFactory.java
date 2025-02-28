package com.doctor.aistring;

import com.android.build.api.instrumentation.AsmClassVisitorFactory;
import com.android.build.api.instrumentation.ClassContext;
import com.android.build.api.instrumentation.ClassData;

import org.objectweb.asm.ClassVisitor;

public abstract class StringEncryptorFactory implements AsmClassVisitorFactory<AiStringPlugin.StringEncryptParams> {

    @Override
    public ClassVisitor createClassVisitor(ClassContext classContext, ClassVisitor nextClassVisitor) {
        // 使用 getOrElse(null) 来避免空值问题
        AiStringPlugin.StringEncryptParams params = getParameters().getOrElse(null);
        if (params != null) {
            String key = params.getKey().getOrElse(null);  // 获取 Key，避免空值
            StringEncryptor encryptor = new StringEncryptor();
            return new StringClassVisitor(nextClassVisitor, encryptor, key);
        } else {
            throw new IllegalStateException("Instrumentation parameters not found!");
        }
    }

    @Override
    public boolean isInstrumentable(ClassData classData) {
        return true;
    }
}
