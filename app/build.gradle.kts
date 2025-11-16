plugins {
    // 1. FIXED: Removed duplicate plugin IDs (e.g., id("com.android.application"))
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // 2. Add Google Services Plugin (Needed for Firebase)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.weave_android_app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.weave_android_app"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        // Set target to Java 1.8 for Firebase compatibility if 11 causes issues
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11" // Match Java version
    }
}

dependencies {

    // ANDROIDX/KOTLIN CORE
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // NAVIGATION
    val nav_version = "2.7.5"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    // LIFECYCLE & COROUTINES (VIEWMODEL)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // FIREBASE PLATFORM & SDKs
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))

    // Auth and Firestore (No version needed due to BOM)
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx") // Contains the KTX extensions

    // Critical Coroutines Bridge (Helps link KTX features like .await() and .asFlow())
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

    // TESTING
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}