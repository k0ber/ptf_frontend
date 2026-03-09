val isCi: Boolean by extra
val ptfVersionName: String by extra
val ptfVersionCode: Int by extra

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "org.patifiner.client"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.patifiner.client"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()

        versionCode = ptfVersionCode
        versionName = ptfVersionName
    }

    signingConfigs {
        create("release") {
            if (isCi) {
                storeFile = file(System.getenv("KEYSTORE_FILE_PATH"))
                storePassword = System.getenv("KEYSTORE_PASSWORD")
                keyAlias = System.getenv("KEY_ALIAS")
                keyPassword = System.getenv("KEY_PASSWORD")
            } else {
                val debugConfig = getByName("debug")
                storeFile = debugConfig.storeFile
                storePassword = debugConfig.storePassword
                keyAlias = debugConfig.keyAlias
                keyPassword = debugConfig.keyPassword
            }
        }
    }

    buildTypes {
        getByName("debug") {
            buildConfigField("Boolean", "IS_DEV", "true")
        }

        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("Boolean", "IS_DEV", "false")
        }

        create("releaseDebug") {
            initWith(getByName("release"))
            isDebuggable = true
            applicationIdSuffix = ".dev"
            buildConfigField("Boolean", "IS_DEV", "true")
        }
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

composeCompiler {
    includeSourceInformation = true
    includeTraceMarkers = true
}

baselineProfile {
    from(project(":benchmark"))
    filter { include("org.patifiner.client.**") }
    saveInSrc = true
}

dependencies {
    implementation(projects.composeApp)

    implementation(libs.google.android.material)

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.activity.compose)

    implementation(libs.koin.android)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.decompose.core)
    implementation(libs.decompose.extensions.android)

    compileOnly(libs.compose.ui.tooling.preview) // double check is it really needed here or not?
    debugImplementation(libs.compose.ui.tooling) // this to

    baselineProfile(project(":benchmark"))
    implementation(libs.androidx.profileinstaller)
}
