plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.saving_test"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.saving_test"
        minSdk = 24
        targetSdk = 35
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.core)

    // Room dependencies
    val room_version = "2.7.1"
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)


    // Testing dependencies
    testImplementation(libs.room.testing)
    testImplementation(libs.junit)
    testImplementation(libs.core.v150)

    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
