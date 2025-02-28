package com.doctor.aistring;

import com.android.build.api.instrumentation.InstrumentationParameters;
import com.android.build.api.instrumentation.InstrumentationScope;
import com.android.build.api.variant.AndroidComponentsExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;

public class AiStringPlugin implements Plugin<Project> {
    private final Logger logger = Logging.getLogger(AiStringPlugin.class);

    public interface StringEncryptParams extends InstrumentationParameters {
        @Input
        Property<String> getKey();
    }

    @Override
    public void apply(Project project) {
        project.getPlugins().withId("com.android.application", androidPlugin -> {
            boolean isReleaseBuild = project.getGradle().getStartParameter().getTaskNames().stream()
                    .anyMatch(task -> task.toLowerCase().contains("release"));

            if (!isReleaseBuild) {
                logger.lifecycle("\n  \n当前不是 Release 模式      \n字符串加密插件先不运行   \n不然卡的难受                       \nBemol  \n\n");
                return;
            }

            try {
                // 生成密钥
                String key = KeyGenerator.generateKey();

                // 创建 RuntimeKeyUpdater 实例，更新密钥
                Project runtimeProject = project.getRootProject().findProject(":runtime");
                if (runtimeProject != null) {
                    logger.lifecycle("找到runtime模块，开始更新密钥");
                    RuntimeKeyUpdater runtimeKeyUpdater = new RuntimeKeyUpdater();
                    runtimeKeyUpdater.updateKey(runtimeProject, key);  // 调用更新密钥的方法
                } else {
                    logger.error("未找到runtime模块，请检查项目配置");
                    return;
                }

                // 使用 ASM 注册字符串加密操作
                AndroidComponentsExtension<?, ?, ?> androidComponents = project.getExtensions().getByType(AndroidComponentsExtension.class);
                logger.lifecycle("开始注册字符串加密转换");

                androidComponents.onVariants(androidComponents.selector().all(), (variant) -> {
                    logger.lifecycle("处理变体: " + variant.getName());
                    if (variant.getInstrumentation() != null) {
                        logger.lifecycle("开始设置字符串加密转换器");
                        variant.getInstrumentation().transformClassesWith(
                                StringEncryptorFactory.class,
                                InstrumentationScope.ALL,
                                params -> {
                                    params.getKey().set(key);
                                    logger.lifecycle("字符串加密转换器设置完成");
                                    return null;
                                }
                        );
                    } else {
                        logger.lifecycle("Instrumentation 不存在，跳过变体: " + variant.getName());
                    }
                });
            } catch (Exception e) {
                logger.error("插件执行过程中发生错误: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
        });
    }
}
