import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.yapp.bol.presentation"
    compileSdk = com.yapp.bol.Applications.compileSdk

    defaultConfig {
        minSdk = com.yapp.bol.Applications.minSdk
        targetSdk = com.yapp.bol.Applications.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        buildConfigField("String", "KAKAO_API_KEY", getProperty("KAKAO_API_KEY"))
        buildConfigField("String", "KAKAO_API_KEY_SANDBOX", getProperty("KAKAO_API_KEY_SANDBOX"))
        buildConfigField("String", "GOOGLE_LOGIN_API_KEY", getProperty("GOOGLE_LOGIN_API_KEY"))
        buildConfigField("String", "GOOGLE_LOGIN_API_KEY_SANDBOX", getProperty("GOOGLE_LOGIN_API_KEY_SANDBOX"))
        manifestPlaceholders["kakaoKey"] = getProperty("KAKAO_API_KEY_MANI")
        manifestPlaceholders["kakaoKeySandbox"] = getProperty("KAKAO_API_KEY_MANI_SANDBOX")
        buildConfigField("String", "NAVER_CLIENT_ID", getProperty("NAVER_CLIENT_ID"))
        buildConfigField("String", "NAVER_CLIENT_ID_SANDBOX", getProperty("NAVER_CLIENT_ID_SANDBOX"))
        buildConfigField("String", "NAVER_CLIENT_NAME", getProperty("NAVER_CLIENT_NAME"))
        buildConfigField("String", "NAVER_CLIENT_NAME_SANDBOX", getProperty("NAVER_CLIENT_NAME_SANDBOX"))
        buildConfigField("String", "NAVER_CLIENT_SECRET", getProperty("NAVER_CLIENT_SECRET"))
        buildConfigField("String", "NAVER_CLIENT_SECRET_SANDBOX", getProperty("NAVER_CLIENT_SECRET_SANDBOX"))
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        debug {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = com.yapp.bol.Applications.sourceCompatibilityVersion
        targetCompatibility = com.yapp.bol.Applications.targetCompatibilityVersion
    }

    kotlinOptions {
        jvmTarget = com.yapp.bol.Applications.jvmTarget
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {

    implementation(project(mapOf("path" to ":domain")))
    implementation(project(mapOf("path" to ":designsystem")))

    implementation(com.yapp.bol.KTX.CORE)
    implementation(com.yapp.bol.AndroidX.APP_COMPAT)
    implementation(com.yapp.bol.AndroidX.MATERIAL)
    implementation(com.yapp.bol.AndroidX.CONSTRAINT_LAYOUT)
    implementation(com.yapp.bol.Test.JUNIT)
    implementation(com.yapp.bol.Test.TEST_RUNNER)
    implementation(com.yapp.bol.Test.ESPRESSO_CORE)

    // Hilt
    implementation(com.yapp.bol.DaggerHilt.DAGGER_HILT)
    kapt(com.yapp.bol.DaggerHilt.DAGGER_HILT_COMPILER)
    kapt(com.yapp.bol.DaggerHilt.DAGGER_HILT_ANDROIDX_COMPILER)

    // retrofit
    implementation(com.yapp.bol.Retrofit.RETROFIT)
    implementation(com.yapp.bol.Retrofit.GSON)
    implementation(com.yapp.bol.Retrofit.CONVERTER_GSON)
    implementation(com.yapp.bol.Retrofit.CONVERTER_JAXB)

    // AndroidX
    implementation(com.yapp.bol.AndroidX.LIFECYCLE_VIEW_MODEL)
    implementation(com.yapp.bol.AndroidX.LIFECYCLE_LIVEDATA)
    implementation(com.yapp.bol.AndroidX.ACTIVITY)
    implementation(com.yapp.bol.AndroidX.FRAGMENT)
    implementation(com.yapp.bol.AndroidX.COMPOSE)
    implementation(com.yapp.bol.AndroidX.VIEW_PAGER)

    implementation(com.yapp.bol.Firebase.GMS_CLASSPATH)
    implementation(com.yapp.bol.Firebase.ANALYSICES)
    implementation(com.yapp.bol.Firebase.CRASHLYTICES)
    implementation(com.yapp.bol.Firebase.FIREBASE_BOM)

    // Coroutines
    implementation(com.yapp.bol.Coroutines.COROUTINES)

    // OAuth
    implementation(com.yapp.bol.OAuth.NAVER)
    implementation(com.yapp.bol.OAuth.KAKAO)
    implementation(com.yapp.bol.Firebase.GMS_AUTH)

    // Glide
    implementation(com.yapp.bol.Glide.GLIDE)
    implementation(com.yapp.bol.Glide.COMPILER)

    // Navigation
    implementation(com.yapp.bol.Navigation.NAVIGATION)
    implementation(com.yapp.bol.Navigation.BOTTOM_UI)
    implementation(com.yapp.bol.Navigation.BOTTOM_FRAGMENT)

    // Paging3
    implementation(com.yapp.bol.AndroidX.PAGING)

    // Shimmer
    implementation(com.yapp.bol.Shimmer.SHIMMER)

    // Licenses
    implementation(com.yapp.bol.Licenses.OSS_LICENSES)

    // Lottie
    implementation(com.yapp.bol.LOTTIE.LOTTIE)
}

fun getProperty(propertyKey: String): String {
    return gradleLocalProperties(rootDir).getProperty(propertyKey)
}
