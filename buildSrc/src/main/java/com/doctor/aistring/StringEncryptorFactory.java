package com.doctor.aistring;

import com.android.build.api.instrumentation.AsmClassVisitorFactory;
import com.android.build.api.instrumentation.ClassContext;
import com.android.build.api.instrumentation.ClassData;

import org.objectweb.asm.ClassVisitor;

import java.util.Collections;
import java.util.List;

public abstract class StringEncryptorFactory implements AsmClassVisitorFactory<AiStringPlugin.StringEncryptParams> {

    @Override
    public ClassVisitor createClassVisitor(ClassContext classContext, ClassVisitor nextClassVisitor) {
        AiStringPlugin.StringEncryptParams params = getParameters().getOrElse(null);
        if (params != null) {
            String key = params.getKey().getOrElse(null);
            List<String> targetPackages = params.getTargetPackages().getOrElse(Collections.emptyList());
            StringEncryptor encryptor = new StringEncryptor();
            return new StringClassVisitor(nextClassVisitor, encryptor, key, targetPackages);
        } else {
            throw new IllegalStateException("Instrumentation parameters not found!");
        }
    }

    @Override
    public boolean isInstrumentable(ClassData classData) {
        return true;
    }
}
