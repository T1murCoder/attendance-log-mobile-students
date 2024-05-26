plugins {
    id("com.android.application")
}

android {
    namespace = "ru.technosopher.attendancelogappstudents"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.technosopher.attendancelogappstudents"
        minSdk = 28
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    val camerax_version = "1.3.0"
    val zxing_version = "3.4.1"

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("com.google.code.gson:gson:2.10")

    implementation("androidx.activity:activity:1.9.0")
    implementation("com.squareup.picasso:picasso:2.8")

    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    implementation("com.github.ismaeldivita:chip-navigation-bar:1.4.0")

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.10")

    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")

    //CameraX
    implementation("androidx.camera:camera-core:$camerax_version")

    implementation("androidx.camera:camera-extensions:$camerax_version")
    implementation("androidx.camera:camera-view:$camerax_version")

    implementation("androidx.camera:camera-camera2:$camerax_version")
    implementation("androidx.camera:camera-lifecycle:$camerax_version")
    implementation("androidx.camera:camera-video:$camerax_version")

    //Zxing
    implementation("com.google.zxing:core:$zxing_version")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

}