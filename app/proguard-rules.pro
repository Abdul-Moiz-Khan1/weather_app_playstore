#############################################
# Jetpack Compose
#############################################
-dontwarn androidx.compose.**
-keep class androidx.compose.** { *; }

#############################################
# Lifecycle, ViewModel, LiveData
#############################################
-dontwarn androidx.lifecycle.**
-keep class androidx.lifecycle.** { *; }
-keepclassmembers class * {
    @androidx.lifecycle.* <methods>;
}

#############################################
# Navigation Compose
#############################################
-dontwarn androidx.navigation.**
-keep class androidx.navigation.** { *; }

#############################################
# Hilt / Dagger
#############################################
-keep class dagger.** { *; }
-dontwarn dagger.**
-keep class javax.inject.** { *; }
-dontwarn javax.inject.**

# Required for Hilt
-keep class * extends dagger.hilt.internal.GeneratedComponent { *; }
-keep class moiz.dev.android.weatherApp.MyApp { *; }

#############################################
# Retrofit + Gson
#############################################
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**
-keepclassmembers class * {
    @retrofit2.http.* <methods>;
}
-keepattributes Signature
-keepattributes RuntimeVisibleAnnotations

# Keep Gson-annotated fields (if any)
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**

#############################################
# Coil (Image Loading)
#############################################
-keep class coil.** { *; }
-dontwarn coil.**

#############################################
# Glide (if you really use it)
#############################################
-keep class com.bumptech.glide.** { *; }
-dontwarn com.bumptech.glide.**

#############################################
# Room (Database)
#############################################
-dontwarn androidx.room.**
-keep class androidx.room.** { *; }
-keepclassmembers class * {
    @androidx.room.* <methods>;
}

#############################################
# Lottie
#############################################
-keep class com.airbnb.lottie.** { *; }
-dontwarn com.airbnb.lottie.**

#############################################
# Google Play Services - Location
#############################################
-dontwarn com.google.android.gms.**
-keep class com.google.android.gms.location.** { *; }

#############################################
# Accompanist (Permissions)
#############################################
-keep class com.google.accompanist.** { *; }
-dontwarn com.google.accompanist.**

#############################################
# Your own app classes (keep models, viewmodels, etc.)
#############################################
-keep class moiz.dev.android.weatherApp.** { *; }

#############################################
# Optional: Keep line numbers for debugging crashes
#############################################
-keepattributes SourceFile,LineNumberTable
