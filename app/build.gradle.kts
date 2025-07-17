plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.lastminutedevice.sixweeks"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.lastminutedevice.sixweeks"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["clearPackageData"] = "true" // Clear between tests.
    }

    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
        }
    }

    flavorDimensions.add("skill")
    productFlavors {
        create("pushups") {
            dimension = "skill"
            applicationIdSuffix = ".pushups"
            versionNameSuffix = "-pushup"
            buildConfigField("String", "skill", "\"pushups\"") // Provide access to the skill name in code.
        }
        create("situps") {
            dimension = "skill"
            applicationIdSuffix = ".situps"
            versionNameSuffix = "-situp"
            buildConfigField("String", "skill", "\"situps\"")
        }
        create("planks") {
            dimension = "skill"
            applicationIdSuffix = ".planks"
            versionNameSuffix = "-plank"
            buildConfigField("String", "skill", "\"planks\"")
        }
        create("squats") {
            dimension = "skill"
            applicationIdSuffix = ".squats"
            versionNameSuffix = "-squat"
            buildConfigField("String", "skill", "\"squats\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    val coroutinesVersion = "1.10.2"
    val roomVersion = "2.7.2"

    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-runtime:${roomVersion}")
    implementation("androidx.room:room-ktx:$roomVersion")

    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.9.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.9.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.9.0")
    implementation("com.squareup.moshi:moshi:1.15.2")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

    ksp("com.google.dagger:hilt-compiler:2.56.2")
    implementation("com.google.dagger:hilt-android:2.56.2")

    // Unit tests.

    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("androidx.test:core-ktx:1.6.1")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${coroutinesVersion}")
    testImplementation("androidx.room:room-testing:${roomVersion}")

    testImplementation("org.mockito:mockito-core:5.18.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")

    implementation("androidx.test.ext:junit-ktx:1.2.1")
    implementation("org.jetbrains.kotlin:kotlin-test:2.1.21")

    // Instrumented tests.

    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")
    androidTestImplementation("androidx.test:core-ktx:1.6.1")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    androidTestImplementation("androidx.test:runner:1.6.2")

    androidTestUtil("androidx.test:orchestrator:1.5.1")
}
