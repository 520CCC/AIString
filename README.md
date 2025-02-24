# AIString - Android字符串加密工具

一行代码，自动加密你的Android应用中所有字符串。

## 使用方法

1. 在项目根目录的 `settings.gradle` 中添加：
```groovy
dependencyResolutionManagement {
    repositories {
        // ... 其他仓库
        maven { url 'https://jitpack.io' }
    }
}
```

2. 在应用模块的 `build.gradle` 中添加：
```groovy
plugins {
    id 'com.github.doctorgero.aistring' version '1.0.0'
}
```

完成！你的应用中所有字符串都会在编译时自动加密。

## 示例

加密前：
```kotlin
class MainActivity : AppCompatActivity() {
    private val apiKey = "1234567890abcdef"
    private val secretKey = "this_is_a_secret"
    
    fun test() {
        Log.d("TAG", "测试字符串")
    }
}
```

加密后（反编译看到的）：
```kotlin
class MainActivity : AppCompatActivity() {
    private val apiKey = AiString.decrypt("Ax7Yd+8kP2jL9nM=")
    private val secretKey = AiString.decrypt("Kj4Tp0Qw5vN8xR2=")
    
    fun test() {
        Log.d("TAG", AiString.decrypt("Hc1Bf6UeVm3gY9="))
    }
}
```

## 注意事项

- 支持 Android API 21+
- 自动加密所有非空字符串
- 无需任何配置，一行代码搞定

## License

```
Copyright 2024 Your Name

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
``` 