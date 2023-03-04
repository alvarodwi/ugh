plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  kotlin("kapt")
  kotlin("plugin.serialization") version "1.8.10"
  id("kotlin-parcelize")
  id("androidx.navigation.safeargs.kotlin")
  id("dagger.hilt.android.plugin")
}

android {
  namespace = "me.varoa.ugh"
  compileSdk = 33

  defaultConfig {
    applicationId = "me.varoa.ugh"
    minSdk = 24
    targetSdk = 33
    versionCode = 1
    versionName = "1.0"
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = true
      proguardFiles(
        getDefaultProguardFile("proguard-android.txt"),
        "proguard-rules.pro"
      )
    }
    getByName("debug") {
      // isMinifyEnabled = true
      proguardFiles(
        getDefaultProguardFile("proguard-android.txt"),
        "proguard-rules.pro"
      )
    }
  }

  compileOptions {
    sourceCompatibility(JavaVersion.VERSION_11)
    targetCompatibility(JavaVersion.VERSION_11)
  }

  kotlinOptions {
    jvmTarget = "11"
    freeCompilerArgs = freeCompilerArgs + listOf(
      "-opt-in=kotlin.ExperimentalStdlibApi",
      "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
      "-opt-in=kotlin.RequiresOptIn"
    )
  }

  buildFeatures {
    viewBinding = true
  }
}

kotlin {
  jvmToolchain(11)
}

dependencies {
  // coroutine
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

  // core
  implementation("androidx.core:core-ktx:1.9.0")
  implementation("androidx.appcompat:appcompat:1.6.1")
  implementation("androidx.navigation:navigation-runtime-ktx:2.5.3")
  implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
  implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")

  // ui
  implementation("com.google.android.material:material:1.8.0")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")
  implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
  implementation("androidx.paging:paging-runtime-ktx:3.1.1")

  // di
  implementation("com.google.dagger:hilt-android:2.44")
  kapt("com.google.dagger:hilt-android-compiler:2.44")

  // networking
  implementation("com.squareup.retrofit2:retrofit:2.9.0")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
  implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
  implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

  // other
  implementation("io.coil-kt:coil:2.2.0")
  implementation("com.squareup.logcat:logcat:0.1")

  // testing
  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}