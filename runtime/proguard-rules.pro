# 防止 AiString 类和它的方法被混淆
-keep class com.doctor.aistring.AiString { *; }
-keep class com.doctor.aistring.AiString$* { *; }


# 保留 StringConcatFactory 类
#-keep class java.lang.invoke.StringConcatFactory { *; }
#
## 保留与 AES 加密相关的类
#-keep class javax.crypto.** { *; }
#-keep class javax.crypto.spec.** { *; }

