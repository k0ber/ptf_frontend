val isCi: Boolean by extra

plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.patifiner.benchmark"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = 28 // Macrobenchmark and Baseline Profiles support started from 28
        //noinspection OldTargetApi
        targetSdk = libs.versions.android.targetSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        create("benchmark") {
            initWith(getByName("debug"))
            matchingFallbacks += listOf("release")
            proguardFiles("benchmark-rules.pro")
        }
    }

    targetProjectPath = ":androidApp"

    // run benchmark separately from the app process
    experimentalProperties["android.experimental.self-instrumenting"] = true
}

dependencies {
    implementation(projects.composeApp)

    implementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.settings.core)

    // Compose
    implementation(libs.compose.ui)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.runtime)

    // Decompose
    implementation(libs.decompose.core)
    implementation(libs.decompose.extensions.compose)
    implementation(libs.essenty.lifecycle)
    implementation(libs.essenty.statekeeper)
    implementation(libs.essenty.lifecycle)

    // Mvi
    implementation(libs.mvikotlin.core)
    implementation(libs.mvikotlin.main)
    implementation(libs.mvikotlin.coroutines)

    // Bench
    implementation(libs.androidx.uiautomator)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.benchmark.macro.junit4)

    // Tests
    implementation(libs.androidx.test.ext.junit)
    implementation(libs.koin.test)
    implementation(libs.koin.test.junit4)
    implementation(libs.koin.android)
    implementation(libs.mockk.android)
    implementation(libs.androidx.test.ext.junit)
    implementation(libs.settings.test)
    implementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

baselineProfile {
    @Suppress("UnstableApiUsage")
    enableEmulatorDisplay = true

    useConnectedDevices = true

    if (isCi) {
        managedDevices = mutableListOf("pixel6Api31")
    }
}
