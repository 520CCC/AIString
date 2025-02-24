-keep class com.doctor.aistring.AiString {
    private static final java.lang.String KEY;
    private static final java.lang.String algorithm;
    private static final java.util.concurrent.ConcurrentHashMap decryptCache;
    *;
}

-keepattributes *Annotation*
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keepattributes InnerClasses,EnclosingMethod
-keepattributes RuntimeVisible*Annotations*
-keepattributes Exceptions

-keep class androidx.annotation.Keep
-keep @androidx.annotation.Keep class * {*;}
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <methods>;
}
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <fields>;
}

-keepclassmembers class com.doctor.aistring.AiString { *; }
-keepnames class com.doctor.aistring.AiString
-dontwarn com.doctor.aistring.**
-keep class javax.crypto.** { *; }
-keep class java.security.** { *; }
-keep class java.util.concurrent.** { *; }
-keep class android.security.** { *; }
-keep class androidx.security.** { *; }

-allowaccessmodification
-repackageclasses ''

-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-dontoptimize
-dontpreverify
-dontobfuscate