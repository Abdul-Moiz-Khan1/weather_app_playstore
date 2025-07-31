-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken

-keep class com.google.ads.** # Don't proguard AdMob classes
-dontwarn com.google.ads.** # Temporary workaround for v6.2.1. It gives a warning that you can ignore

-keep public class com.google.android.gms.ads.** {
    public *;
}

-keep public class com.google.ads.** {
    public *;
}

# Gson uses generic type information stored in a class file when working with
# fields. Proguard removes such information by default, keep it.
-keepattributes Signature
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken
# Optional. For using GSON @Expose annotation
-keepattributes AnnotationDefault,RuntimeVisibleAnnotations
-dontwarn okhttp3.internal.platform.*
-keep class com.vid.downloader.Retrofit.** { *; }

# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.OpenSSLProvider
# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Keep annotation default values (e.g., retrofit2.http.Field.encoded).
-keepattributes AnnotationDefault

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# Keep inherited services.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>

# With R8 full mode generic signatures are stripped for classes that are not
# kept. Suspend functions are wrapped in continuations where the type argument
# is used.
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# R8 full mode strips generic signatures from return types if not kept.
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>

# With R8 full mode generic signatures are stripped for classes that are not kept.
-keep,allowobfuscation,allowshrinking class retrofit2.Response
# Prevent proguard from stripping interface information from TypeAdapter, TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
# Prevent R8 from leaving Data object members always null
-keepclasseswithmembers class * {
    <init>(...);
    @com.google.gson.annotations.SerializedName <fields>;
}
# Retain generic signatures of TypeToken and its subclasses with R8 version 3.0 and higher.
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
# Retrofit and OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-keep class retrofit2.** { *; }

# Keep Gson model classes
-keep class com.vid.downloader.Retrofit.** { *; }
-keep class com.vid.downloader.Model.** { *; }
-keep class com.vid.downloader.VM.** { *; }

# Ensure that Gson annotations remain intact.
-keepattributes Signature
-keepattributes *Annotation*

# Retain the default Gson TypeAdapter
-keep class com.google.gson.TypeAdapter { *; }
-keep class com.google.gson.Gson { *; }
-keep class com.google.gson.reflect.TypeToken { *; }

# Prevent R8 from optimizing away Gson's reflective usage
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
    }
-keep public class org.jsoup.* { public *; }

#-keep class screen.translator.hitranslator.screen.R$* { *; }

-keepattributes Signature
-keepattributes *Annotation*

-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
    @com.google.gson.annotations.SerializedName <methods>;
}
-keep,allowobfuscation class * {
    <fields>;
    <methods>;
}
-keep class com.google.gson.examples.android.model.** { *; }

# Retain generic signatures of TypeToken and its subclasses with R8 version 3.0 and higher.
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken
#-keepattributes InnerClasses -keep class **.R -keep class **.R$* { <fields>; }



-dontwarn com.google.android.gms.ads.AbstractAdRequestBuilder
-dontwarn java.lang.reflect.AnnotatedType

-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.OpenSSLProvider

# FCM merged (Picasso for Reading URL image)
-dontwarn okhttp3.internal.platform.*
-dontwarn okhttp3.internal.platform.ConscryptPlatform.**

# Google Mobile Ads SDK
-keep class com.google.android.gms.ads.** { *; }
-keep class com.google.android.gms.common.** { *; }
-keep class com.google.android.gms.internal.** { *; }
-keep class com.google.ads.** { *; }
-keep class org.chromium.** { *; }
-dontwarn java.lang.instrument.IllegalClassFormatException

-keep class android.speech.** { *; }
-keep class android.speech.RecognitionService { *; }

-keep class org.json.JSONObject { *; }
-keep class org.json.** { *; }
-keepclassmembers class org.json.** {*;}
# Keep annotations used by Retrofit

# For Kotlin metadata (to preserve class structure)

-keep class com.ttl.weatherupdate.forecast.data.model.** {*;}
-keep class com.ttl.weatherupdate.forecast.data.model.**
-keep class com.ttl.weatherupdate.forecast.data.repository.** {*;}
-keep class com.ttl.weatherupdate.forecast.data.repository.**
-keep class com.ttl.weatherupdate.forecast.data.remote.** {*;}
-keep class com.ttl.weatherupdate.forecast.data.remote.**
-keep class com.ttl.weatherupdate.forecast.data.viewModel.** {*;}
-keep class com.ttl.weatherupdate.forecast.data.viewModel.**


