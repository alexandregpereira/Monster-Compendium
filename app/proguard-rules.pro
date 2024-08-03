# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep Data models
-keep public class br.alexandregpereira.hunter.** {
    public protected *;
}

-keepattributes LineNumberTable,SourceFile

-dontwarn org.slf4j.impl.StaticLoggerBinder
-dontwarn org.apiguardian.api.API$Status
-dontwarn org.apiguardian.api.API
