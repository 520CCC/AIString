package com.doctor.aistring

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.CacheableTask

@CacheableTask
abstract class ArtifactTransform : DefaultTask() {
    @get:Input
    var key: String = ""

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val inputClasses: ListProperty<RegularFile>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    init {
        // 确保任务总是运行
        outputs.upToDateWhen { false }
    }

    @TaskAction
    fun transform() {
        val outputDirectory = outputDir.get().asFile
        outputDirectory.mkdirs()
        
        inputClasses.get().forEach { inputFile ->
            val file = inputFile.asFile
            if (!file.name.endsWith(".class")) return@forEach
            
            // 获取相对路径，保持目录结构
            val relativeFile = file.relativeTo(file.parentFile)
            val outputFile = outputDirectory.resolve(relativeFile.path)
            outputFile.parentFile.mkdirs()
            
            // 处理每个class文件
            val classReader = org.objectweb.asm.ClassReader(file.readBytes())
            val classWriter = org.objectweb.asm.ClassWriter(classReader, 0)
            val classVisitor = StringClassVisitor(classWriter, StringEncryptor(), key)
            classReader.accept(classVisitor, 0)
            
            // 写入转换后的文件
            outputFile.writeBytes(classWriter.toByteArray())
        }
    }
}