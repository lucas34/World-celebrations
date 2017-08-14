# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/lucas/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Android basic proguard
-optimizationpasses 25
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-optimizations class/*,field/*,method/*,code/*
-dontskipnonpubliclibraryclassmembers
-allowaccessmodification
-flattenpackagehierarchy
-forceprocessing

# Okio
-dontwarn okio.**

# Picasso
-dontwarn com.squareup.okhttp.**
-dontwarn com.squareup.picasso.**

# Joda Time
-dontwarn org.joda.**
-keep class org.joda** { *; }

-keep class com.journeemondialelib.Celebration
-keepclassmembers class com.journeemondialelib.Celebration { *; }

-dontwarn java.lang.invoke.*