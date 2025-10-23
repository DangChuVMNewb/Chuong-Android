# =======================
# ⚙️ Cơ bản
# =======================
# Giữ thông tin dòng để debug (tuỳ chọn)
-keepattributes SourceFile,LineNumberTable

# Không đổi tên các resource, layout, manifest
-keepclassmembers class **.R$* {
    public static <fields>;
}

# =======================
# 🧭 AndroidX + ViewBinding
# =======================
-keep class androidx.** { *; }
-keep interface androidx.** { *; }

# Giữ ViewBinding (tránh crash khi binding view)
-keep class * extends androidx.viewbinding.ViewBinding { *; }
-keep class **Binding { *; }

# =======================
# 🌐 WebView + JavaScript Interface
# =======================
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
-keepclassmembers class * extends android.webkit.WebViewClient { *; }
-keepclassmembers class * extends android.webkit.WebChromeClient { *; }
-keepclassmembers class * extends android.webkit.WebView { *; }

# =======================
# 🔍 Reflection & Serializable
# =======================
-keepattributes *Annotation*
-keepattributes Signature
-keep class * implements java.io.Serializable { *; }

# =======================
# 🧩 Google AdMob
# =======================
-keep class com.google.android.gms.ads.** { *; }
-dontwarn com.google.android.gms.**

# =======================
# ☁️ Firebase
# =======================
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# =======================
# 📡 Network (Retrofit, OkHttp, Gson)
# =======================
-keep class com.google.gson.** { *; }
-keepattributes EnclosingMethod
-dontwarn sun.misc.**

-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

-keep class retrofit2.** { *; }
-dontwarn retrofit2.**
-keep class okhttp3.** { *; }
-dontwarn okhttp3.**

# =======================
# 💬 Logging
# =======================
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# =======================
# 🧱 Custom App Code (WebView App)
# =======================
# Giữ Activity chính, WebView, Service, Receiver,...
-keep class com.chuong.**.MainActivity { *; }
-keep class com.chuong.**.WebAppInterface { *; }
-keep class com.chuong.**.Server { *; }

# =======================
# 🚫 Tắt cảnh báo không cần thiết
# =======================
-dontnote
-dontwarn org.apache.**
-dontwarn javax.annotation.**