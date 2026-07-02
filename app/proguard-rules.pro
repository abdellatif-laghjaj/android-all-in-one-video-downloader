# MediaGrab — minify disabled by default; keep rules for safety if enabled.
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod
-keepclassmembers class kotlinx.serialization.json.** { *; }
-keep,includedescriptorclasses class media.grab.os.**$$serializer { *; }
-keepclassmembers class media.grab.os.** { *** Companion; }
-keepclasseswithmembers class media.grab.os.** { kotlinx.serialization.KSerializer serializer(...); }
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
