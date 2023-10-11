plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "by.bashlikovvv.hotelreservation"
    compileSdk = 34

    defaultConfig {
        applicationId = "by.bashlikovvv.hotelreservation"
        minSdk = 24
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
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(Dependencies.AndroidX.Core.coreKTX)
    implementation(Dependencies.AndroidX.AppCompat.appCompat)
    implementation(Dependencies.Com.Google.Android.Material.material)
    implementation(Dependencies.AndroidX.ConstraintLayout.constraintlayout)
    testImplementation(Dependencies.JUnit.jUnit)
    androidTestImplementation(Dependencies.AndroidX.Test.Ext.jUnit)
    androidTestImplementation(Dependencies.AndroidX.Test.Espresso.espressoCore)

    //RecyclerView
    implementation(Dependencies.AndroidX.RecyclerView.recyclerView)
    //ViewModel architecture component
    implementation(Dependencies.AndroidX.LifeCycle.lifeCycleViewModel)
    //Fragment
    implementation(Dependencies.AndroidX.Fragment.fragmentKTX)
    //Navigation
    implementation(Dependencies.AndroidX.Navigation.navigationFragmentKTX)
    implementation(Dependencies.AndroidX.Navigation.navigationUiKTX)
    //ViewBinding
    implementation(Dependencies.Com.Github.Kirich1409.viewBindingDelegateFull)
    //Glide
    implementation (Dependencies.Com.Github.BumpTech.Glide.glide)
    // Retrofit
    implementation (Dependencies.Com.SquareUp.Retrofit2.retrofit)
    implementation (Dependencies.Com.SquareUp.Retrofit2.converterGson)
    //Gson
    implementation(Dependencies.Com.Google.Code.Gson.gson)
    //Adapter delegates
    implementation(Dependencies.Com.Hannesdorfmann.adapterDelegates)
    //Room
    implementation(Dependencies.AndroidX.Room.roomKTX)
    //Koin
    implementation(Dependencies.Io.InsertKoin.koinAndroid)
}