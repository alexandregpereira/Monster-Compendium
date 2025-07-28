# Keep Data models
-keep public class br.alexandregpereira.hunter.** {
    public protected *;
}

-dontwarn java.lang.invoke.MethodHandles
-dontwarn org.jetbrains.skia.**
-dontwarn org.jetbrains.skiko.**

# Packaging options to handle duplicate files
-dontwarn okio.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# Handle duplicate META-INF files by picking the first one
-keep class org.slf4j.** { *; }
-dontwarn org.slf4j.**

# Packaging options to handle duplicate files
-dontnote **
-dontwarn com.squareup.okhttp.** # If you're using an older version
-dontwarn okhttp3.**
-dontwarn retrofit2.**
-dontwarn javax.annotation.** # If you have JSR 250 annotations
-dontwarn org.codehaus.mojo.** # If it's a Maven plugin causing this, though less likely here

# Packaging options to handle duplicate files
# For MANIFEST.MF and other common META-INF files
-keepdirectories
-keepclassmembers class ** {
    @kotlin.jvm.JvmStatic public static final ** INSTANCE;
}

# Keep Kotlin metadata
#-keep class kotlin.Metadata { *; }
#-keepclassmembers class ** {
#    @kotlin.Metadata ** ;
#}
-keepattributes Annotation,Signature,EnclosingMethod,InnerClasses,SourceFile,LineNumberTable,Exceptions,Deprecated,RuntimeVisibleAnnotations,RuntimeInvisibleAnnotations,RuntimeVisibleParameterAnnotations,RuntimeInvisibleParameterAnnotations,RuntimeVisibleTypeAnnotations,RuntimeInvisibleTypeAnnotations,MethodParameters,Module,ModulePackages,ModuleMainClass,NestHost,NestMembers,Record,PermittedSubclasses

# For kotlinx.serialization
-keepclassmembers class kotlinx.serialization.internal.* {
    <fields>;
    <methods>;
}
-keepnames class kotlinx.serialization.SerializersKt
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
    public final java.lang.String getSerialName();
}
-keepclassmembers class * implements kotlinx.serialization.KSerializer {
    <methods>;
    <fields>;
}

# For Ktor (common rules, might need adjustments based on your usage)
-keep class io.ktor.** { *; }
-keepclassmembers class io.ktor.** { *; }
-keepattributes Signature
-keepattributes *Annotation*

# For OkHttp (common rules)
-keep class okhttp3.** { *; }
-keepclassmembers class okhttp3.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn okhttp3.internal.platform.ConscryptPlatform*
-dontwarn okhttp3.internal.platform.BouncyCastlePlatform*
-dontwarn okhttp3.internal.platform.OpenJSSEPlatform*
-dontwarn okhttp3.internal.platform.android.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# Keep Okio library intact
-keep class okio.** { *; }
-keepclassmembers class okio.** { *; }

# Keep coil3 library
-keep class coil3.** { *; }
-keepclassmembers class coil3.** { *; }

# For SLF4J (if used)
-keep class org.slf4j.** { *; }
-dontwarn org.slf4j.impl.StaticLoggerBinder

# Your application's main class and other necessary keeps
-keep public class MainKt {
    public static void main(java.lang.String[]);
}

# Keep all classes in your main package if necessary, or be more specific
# -keep class com.yourpackage.** { *; }

# Keep Compose specific things
-keep class androidx.compose.runtime.internal.ComposableLambda { *; }
-keep class androidx.compose.runtime.internal.ComposableLambdaN { *; }
-keep class androidx.compose.runtime.Composer { *; }
-keep class androidx.compose.runtime.Recomposer { *; }
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}
-keepclassmembers class * implements androidx.compose.runtime.Composer {
    <methods>;
}
-keepclassmembers class * implements androidx.compose.runtime.Recomposer {
    <methods>;
}
-keepclassmembernames class androidx.compose.runtime.R { *; }

# Add specific -keep rules for classes that are only accessed via reflection
# Example:
# -keep class com.example.MyReflectedClass { *; }

# Suppress warnings for missing Android classes if your desktop app shares code with Android
# and those classes are not actually used at runtime on desktop.
-dontwarn android.os.Build**
-dontwarn android.security.**
-dontwarn android.util.Log
-dontwarn android.net.http.X509TrustManagerExtensions
-dontwarn android.net.ssl.SSLSockets
-dontwarn dalvik.system.CloseGuard

-dontwarn io.ktor.network.sockets.SocketBase$attachFor$1

# Keep everything in the org.sqlite package and its subpackages
-keep class org.sqlite.** { *; }
-keepclassmembers class org.sqlite.** { *; }
-keepattributes *Annotation* # Keep annotations within this package if any are used
-dontwarn org.sqlite.**

