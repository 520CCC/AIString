package com.doctor.aistring

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.File
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

class AiStringPlugin : Plugin<Project> {
    private val logger: Logger = Logging.getLogger(AiStringPlugin::class.java)

    override fun apply(project: Project) {
        try {
            // 自动添加runtime依赖
            project.afterEvaluate {
                project.dependencies.add(
                    "implementation",
                    "com.github.yourusername:aistring-runtime:${project.version}"
                )
            }
            
            // 生成新密钥
            val key = KeyGenerator.generateKey()
            
            // 处理应用模块的类文件
            project.afterEvaluate {
                // 首先替换 runtime 模块中的密钥
                val runtimeProject = project.rootProject.findProject(":runtime")
                if (runtimeProject != null) {
                    runtimeProject.tasks.matching { task ->
                        task.name.startsWith("compile") && task.name.endsWith("Kotlin")
                    }.configureEach { task ->
                        task.doFirst {
                            val runtimeDir = runtimeProject.projectDir
                            val aiStringFile = runtimeDir.resolve("src/main/java/com/doctor/aistring/AiString.kt")
                            if (aiStringFile.exists()) {
                                var content = aiStringFile.readText()
                                content = content.replace(
                                    "DEFAULT_KEY_WILL_BE_REPLACED",
                                    key
                                )
                                aiStringFile.writeText(content)
                            }
                        }
                    }
                }

                // 然后处理应用模块的类文件
                project.tasks.matching { task ->
                    task.name.startsWith("compile") && task.name.endsWith("Kotlin")
                }.configureEach { task ->
                    task.doLast {
                        try {
                            // 找到所有的 class 文件
                            val variantName = when {
                                task.name.contains("Debug") -> "debug"
                                task.name.contains("Release") -> "release"
                                else -> return@doLast
                            }
                            val classesDir = project.buildDir.resolve("tmp/kotlin-classes/$variantName")
                            
                            if (!classesDir.exists()) {
                                return@doLast
                            }
                            
                            classesDir.walk()
                                .filter { it.name.endsWith(".class") }
                                .forEach { classFile ->
                                    try {
                                        // 修改类文件
                                        val classReader = ClassReader(classFile.readBytes())
                                        val classWriter = ClassWriter(classReader, 0)
                                        val classVisitor = StringClassVisitor(classWriter, StringEncryptor(), key)
                                        classReader.accept(classVisitor, 0)
                                        
                                        // 写回修改后的类文件
                                        classFile.writeBytes(classWriter.toByteArray())
                                    } catch (e: Exception) {
                                        // 忽略处理失败的文件，继续处理下一个
                                    }
                                }
                        } catch (e: Exception) {
                            // 忽略任务执行错误，不影响编译
                        }
                    }
                }
            }
        } catch (e: Exception) {
            // 插件执行失败时不要中断编译
            logger.error("AIString插件执行失败，字符串加密可能未生效")
        }
    }
}


