-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-dontskipnonpubliclibraryclassmembers
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keepattributes Signature

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.app.Fragment

-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#bean不混淆
-dontwarn com.deya.acaide.vo.**
-keep class com.deya.acaide.vo.**{*; }

#IM不混淆
-dontwarn com.im.sdk.dy.**
-keep class com.im.sdk.dy.**{*; }
-keep class com.deya.hospital.base.img.*{ *; }
-dontwarn com.deya.hospital.util.**
-keep class com.deya.hospital.util.**{ *; }
-dontwarn com.deya.hospital.view.**
-keep class com.deya.hospital.view.**{ *; }
#mrwujay不混淆
-dontwarn com.mrwujay.cascade.**
-keep class com.mrwujay.cascade.**{*; }

#kankan不混淆
-dontwarn kankan.wheel.widget.**
-keep class kankan.wheel.widget.**{*; }


##蒲公英
#-dontwarn com.pgyersdk.**
#-keep class com.pgyersdk.** { *; }

#腾讯bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

#第三方包
-dontwarn org.apache.http.entity.**
-keep class org.apache.http.entity.**{*;}

-dontwarn corg.joda.time
-keep class org.joda.time.**{ *; }

-dontwarn com.example.calendar
-keep class com.example.calendar.**{ *; }

-dontwarn com.hp.hpl.sparta.**
-keep class com.hp.hpl.sparta.**{ *; }
-dontwarn demo.**
-keep class demo.**{ *; }
-dontwarn net.sourceforge.pinyin4j.**
-keep class net.sourceforge.pinyin4j.**{ *; }
-dontwarn net.sourceforge.pinyin4j.**
-keep class net.sourceforge.pinyin4j.**{ *; }

-dontwarn com.tencent.**
-keep class com.tencent.**{ *; }

-dontwarn com.umeng.**
-keep class com.umeng.**{ *; }

-dontwarn com.sina.**
-keep class com.sina.**{ *; }

-dontwarn com.nostra13.**
-keep class com.nostra13.**{ *; }

-dontwarn android.**
-keep class android.**{ *; }

-dontwarn com.sina.**
-keep class com.sina.** { *; }

-dontwarn com.lidroid.xutils.**
-keep class com.lidroid.xutils.** { *; }

-dontwarn com.yuntongxun.**
-keep class com.yuntongxun.** { *; }

-dontwarn org.webrtc.**
-keep class org.webrtc.** { *; }

-dontwarn com.google.**
-keep class com.google.** { *; }
-dontwarn android.support.**
-keep class android.support.** { *; }
-dontwarn com.android.**
-keep class com.android.test.** { *; }

-dontwarn org.joda.convert.**
-keep class org.joda.convert.** { *; }

#友盟
-keep public class com.deya.acaide.R$*{
    public static final int *;
}


-dontwarn com.artifex.mupdfdemo
-keep class com.artifex.mupdfdemo.**{ *; }

-dontwarn com.baoyz
-keep class com.baoyz.**{ *; }

-dontwarn com.mrwujay.cascade
-keep class com.mrwujay.cascade.**{ *; }

