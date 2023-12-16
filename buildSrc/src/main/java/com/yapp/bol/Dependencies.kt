package com.yapp.bol

import com.yapp.bol.Versions.BOTTOM_NAVIGATION
import com.yapp.bol.Versions.DATA_STORE_VERSION
import com.yapp.bol.Versions.HILT_VERSION
import com.yapp.bol.Versions.KAKAO_VERSION
import com.yapp.bol.Versions.KOTLIN_VERSION
import com.yapp.bol.Versions.KTLINT_VERSION
import com.yapp.bol.Versions.PAGING_VERSION

// ktlint-disable filename

object Versions {
    const val KOTLIN_VERSION = "1.6.10"
    const val KTLINT_VERSION = "10.2.1"
    const val HILT_VERSION = "2.44"
    const val KAKAO_VERSION = "2.11.0"
    const val PAGING_VERSION = "3.1.1"
    const val DATA_STORE_VERSION = "1.0.0"
    const val FIRBASE_ANALYSIS = "18.0.2"
    const val FIREBASE_CLASHLYTICS = "17.4.1"
    const val BOTTOM_NAVIGATION = "2.5.3"
}

object Android {
    const val BUILD_GRADLE = "com.android.tools.build:gradle:7.3.1"
}

object Kotlin {
    const val SDK = "org.jetbrains.kotlin:kotlin-gradle-plugin:$KOTLIN_VERSION"
}

object KTX {
    const val CORE = "androidx.core:core-ktx:1.8.0"
}

object AndroidX {
    const val MATERIAL = "com.google.android.material:material:1.9.0"
    const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:2.1.4"
    const val APP_COMPAT = "androidx.appcompat:appcompat:1.5.1"
    const val LIFECYCLE_VIEW_MODEL = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1"
    const val LIFECYCLE_LIVEDATA = "androidx.lifecycle:lifecycle-livedata-ktx:2.5.1"
    const val ACTIVITY = "androidx.activity:activity-ktx:1.3.1"
    const val FRAGMENT = "androidx.fragment:fragment-ktx:1.3.6"
    const val COMPOSE = "androidx.activity:activity-compose:1.5.1"
    const val PAGING = "androidx.paging:paging-runtime:$PAGING_VERSION"
    const val PAGING_WITHOUT_ANDROID = "androidx.paging:paging-common:$PAGING_VERSION"
    const val VIEW_PAGER = "androidx.viewpager2:viewpager2:1.0.0"
}

object Coroutines {
    const val COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2"
}

object DaggerHilt {
    const val DAGGER_HILT = "com.google.dagger:hilt-android:$HILT_VERSION"
    const val DAGGER_HILT_COMPILER = "com.google.dagger:hilt-android-compiler:$HILT_VERSION"
    const val DAGGER_HILT_VIEW_MODEL = "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03"
    const val DAGGER_HILT_ANDROIDX_COMPILER = "androidx.hilt:hilt-compiler:1.0.0"
    const val DAGGER_HILT_PLUGIN = "com.google.dagger:hilt-android-gradle-plugin:2.44.1"
}

object Retrofit {
    const val RETROFIT = "com.squareup.retrofit2:retrofit:2.9.0"
    const val CONVERTER_GSON = "com.squareup.retrofit2:converter-gson:2.9.0"
    const val GSON = "com.google.code.gson:gson:2.9.0"
    const val CONVERTER_JAXB = "com.squareup.retrofit2:converter-jaxb:2.9.0"
    const val LOGGING_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:4.10.0"
}

object OkHttp {
    const val OKHTTP = "com.squareup.okhttp3:okhttp:4.9.1"
    const val LOGGING_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:4.9.1"
}

object KtLint {
    const val KTLINT = "org.jlleitschuh.gradle:ktlint-gradle:$KTLINT_VERSION"
}

object OAuth {
    const val KAKAO = "com.kakao.sdk:v2-user:$KAKAO_VERSION"
    const val NAVER = "com.navercorp.nid:oauth:5.4.0"
}

object DataStore {
    const val DATA_STORE = "androidx.datastore:datastore-preferences:$DATA_STORE_VERSION"
    const val DATA_STORE_CORE = "androidx.datastore:datastore-preferences-core:$DATA_STORE_VERSION"
}

object Test {
    const val JUNIT = "junit:junit:4.13.2"
    const val TEST_RUNNER = "com.android.support.test:runner:1.0.2"
    const val ESPRESSO_CORE = "com.android.support.test.espresso:espresso-core:3.0.2"
    const val EXT_JUNIT = "androidx.test.ext:junit:1.1.5"
}

object Firebase {
    const val FIREBASE_BOM = "com.google.firebase:firebase-bom:32.0.0"
    const val FIREBASE_AUTH = "com.google.firebase:firebase-auth-ktx"
    const val FIREBASE_ANALYTICS = "com.google.firebase:firebase-analytics-ktx"
    const val GMS_AUTH = "com.google.android.gms:play-services-auth:20.5.0"
    const val GMS_CLASSPATH = "com.google.gms:google-services:4.3.15"
    const val ANALYSICES = "com.google.firebase:firebase-analytics-ktx:19.0.2"
    const val CRASHLYTICES = "com.google.firebase:firebase-crashlytics-ktx:18.3.7"
    const val CRASHLYTICES_GRADEL = "com.google.firebase:firebase-crashlytics-gradle:2.7.1"
}

object Glide {
    const val GLIDE = "com.github.bumptech.glide:glide:4.13.0"
    const val COMPILER = "com.github.bumptech.glide:compiler:4.13.0"
}

object Navigation {
    const val NAVIGATION = "androidx.navigation:navigation-dynamic-features-fragment:2.5.3"
    const val BOTTOM_FRAGMENT = "androidx.navigation:navigation-fragment-ktx:$BOTTOM_NAVIGATION"
    const val BOTTOM_UI = "androidx.navigation:navigation-ui-ktx:$BOTTOM_NAVIGATION"
}

object Shimmer {
    const val SHIMMER = "com.facebook.shimmer:shimmer:0.5.0"
}

object Licenses {
    const val OSS_LICENSES_CLASSPATH = "com.google.android.gms:oss-licenses-plugin:0.10.6"
    const val OSS_LICENSES= "com.google.android.gms:play-services-oss-licenses:17.0.1"
    const val OSS_LICENSES_ID= "com.google.android.gms.oss-licenses-plugin"
}

object LOTTIE {
    const val LOTTIE = "com.airbnb.android:lottie:5.2.0"
}
