plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Lifecycle components needed for ViewModel and viewModelScope
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0") // Check for the latest stable version
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

    // Kotlin Coroutines components (might already be there, but ensuring compatibility)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3") // Check for the latest stable version
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
}