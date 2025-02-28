# AIString - Android字符串加密工具

一个用于 Android 项目的字符串加密解密工具，用于保护应用中的敏感信息。

## 使用方法

### 1. 复制必要文件
1. 将 `buildSrc` 目录复制到项目根目录
2. 将 `runtime` 目录复制到项目根目录

### 2. 修改项目配置

在项目根目录的 `settings.gradle` 中添加：
include ':runtime'

在项目根目录的 `build.gradle` 中添加插件：
plugins {
id 'com.doctor.aistring'
}

在应用模块的 `build.gradle` 中添加依赖：
dependencies {
implementation project(':runtime')
}

##  完成！

1，你的应用中所有字符串都会在编译时自动加密。
2.支持混淆

   
## 示例
加密前：
private val testString = "东方红太阳升"

加密后（反编译看到的）：

private val testString = "werjweorakwfeakwr"



