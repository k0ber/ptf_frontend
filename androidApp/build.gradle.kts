plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "org.patifiner.client"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.patifiner.client"
        minSdk = libs.versions.android.minSdk.get().toInt()
        //noinspection OldTargetApi - 35 is ok
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = System.getenv("VERSION_CODE")?.toInt() ?: 1
        versionName = System.getenv("VERSION_NAME") ?: "1.0.0"
    }

    signingConfigs {
        create("release") {
            storeFile = file("../release.keystore").takeIf { it.exists() } ?: file("patifiner-release.jks")
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = System.getenv("KEY_ALIAS")
            keyPassword = System.getenv("KEY_PASSWORD")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    dependencies {
        implementation(projects.composeApp)

        implementation(libs.koin.android)

        implementation(libs.compose.ui.tooling.preview)
        debugImplementation(libs.compose.ui.tooling)

        implementation(libs.androidx.activity.compose)
        implementation(libs.ktor.client.okhttp)

        implementation(libs.decompose.core)
        implementation(libs.decompose.extensions.android)
    }
}
