# ClipSave — minify disabled by default; keep rules for safety if enabled.
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod
-keepclassmembers class kotlinx.serialization.json.** { *; }
-keep,includedescriptorclasses class com.abdellatif.clipsave.**$$serializer { *; }
-keepclassmembers class com.abdellatif.clipsave.** { *** Companion; }
-keepclasseswithmembers class com.abdellatif.clipsave.** { kotlinx.serialization.KSerializer serializer(...); }
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
