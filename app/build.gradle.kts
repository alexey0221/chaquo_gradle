plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
//    id("com.chaquo.python")
}

android {
    namespace = "com.example.chaquo_gradle"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.chaquo_gradle"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

//        ndk {
//            // On Apple silicon, you can omit x86_64.
//            abiFilters += listOf("arm64-v8a", "x86_64","x86", "armeabi-v7a")
//        }

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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
//chaquopy {
//    defaultConfig {
//        buildPython("C:\\Users\\user\\AppData\\Local\\Programs\\Python\\Python39\\python.exe")
////        buildPython("C:/path/to/py.exe", "-3.8")
////        pip {
////            install("pillow")
////            install("torch")
////            install("numpy")
////        }
//    }
//    sourceSets {
//        getByName("main") {
//            srcDir("python")
//        }
//    }
//}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    val torchLite_versio = "1.10.0"
    val torch_versio = "1.13.0"
    implementation ("org.pytorch:pytorch_android:$torch_versio")
    implementation ("org.pytorch:pytorch_android_torchvision:$torchLite_versio")

    implementation("org.tensorflow:tensorflow-lite:2.15.0")

//    compile ("com.rmtheis:tess-two:5.4.1")
    implementation ("com.rmtheis:tess-two:8.0.0")
}