plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "by.bashlikovvv.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        javaCompileOptions {
            annotationProcessorOptions {
                argument("room.schemaLocation", "$projectDir/schemas")
            }
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(Dependencies.AndroidX.Core.coreKTX)
    testImplementation(Dependencies.JUnit.jUnit)
    androidTestImplementation(Dependencies.AndroidX.Test.Ext.jUnit)

    //Gson
    implementation(Dependencies.Com.Google.Code.Gson.gson)
    // Retrofit
    implementation (Dependencies.Com.SquareUp.Retrofit2.retrofit)
    //Room
    implementation(Dependencies.AndroidX.Room.roomRuntime)
    implementation(Dependencies.AndroidX.Room.roomKTX)
    ksp(Dependencies.AndroidX.Room.roomCompiler)
}