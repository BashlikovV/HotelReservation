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

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    //ViewModel architecture component
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    //Fragment
    implementation("androidx.fragment:fragment-ktx:1.6.1")
    //Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.4")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.4")
    //ViewBinding
    implementation("com.github.kirich1409:viewbindingpropertydelegate-full:1.5.9")
    //Glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    //Gson
    implementation("com.google.code.gson:gson:2.10.1")
    //Adapter delegates
    implementation("com.hannesdorfmann:adapterdelegates4-kotlin-dsl-viewbinding:4.3.2")
    //Room
    implementation("androidx.room:room-ktx:2.5.2")
    //Koin
    implementation("io.insert-koin:koin-android:3.5.0")
}