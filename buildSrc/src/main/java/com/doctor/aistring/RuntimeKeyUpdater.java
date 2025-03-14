package com.doctor.aistring;

import org.gradle.api.Project;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class RuntimeKeyUpdater {

    public void updateKey(Project runtimeProject, String key) {
        File runtimeDir = runtimeProject.getProjectDir();
        File aiStringFile = new File(runtimeDir, "src/main/java/com/doctor/aistring/AiString.java");

        if (aiStringFile.exists()) {
            try {
                String content = Files.readString(aiStringFile.toPath(), StandardCharsets.UTF_8);
                String pattern = "private\\s+static\\s+final\\s+String\\s+KEY\\s*=\\s*\"([^\"]*)\"";
                content = content.replaceAll(pattern, "private static final String KEY = \"" + key + "\"");

                // 写回文件
                Files.writeString(aiStringFile.toPath(), content, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
//                System.out.println("成功替换密钥: " + key);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
