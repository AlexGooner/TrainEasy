plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.citrus.cookiteasy"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.citrus.cookiteasy"
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
    buildFeatures {
        viewBinding = true
        dataBinding = true
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
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    ksp(libs.androidx.room.compiler)
    ksp(libs.androidx.room.runtime)
    ksp(libs.androidx.room.rxjava2)
    annotationProcessor(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)


    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation (libs.squareup.logging.interceptor)
    ksp(libs.moshi.kotlin.codegen)

    implementation(libs.androidx.paging.runtime.ktx)
    implementation (libs.androidx.swiperefreshlayout)

    implementation (libs.glide)
    annotationProcessor (libs.compiler)

    implementation (libs.androidx.recyclerview)

    implementation(libs.hilt.android.v2511)
    kapt(libs.hilt.android.compiler)

    implementation ("androidx.datastore:datastore-preferences:1.1.4")

    implementation ("oauth.signpost:signpost-core:1.2.1.2")
    implementation ("oauth.signpost:signpost-commonshttp4:1.2.1.2")
    implementation ("com.squareup.okhttp3:okhttp:4.10.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")

    implementation (libs.lottie)

}