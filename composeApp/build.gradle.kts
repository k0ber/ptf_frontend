@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

val ptfVersionName: String by extra
val ptfVersionCode: Int by extra

//region Allure
val allureAgent: Configuration by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
    isTransitive = false
}

tasks.withType<Test>().configureEach {
    val allureResultsDir = rootProject.layout.buildDirectory.dir("allure-results")
    systemProperty("allure.results.directory", allureResultsDir.get().asFile.absolutePath)
    doFirst {
        val agentFile = allureAgent.singleFile
        jvmArgs("-javaagent:${agentFile.absolutePath}")
    }
    testLogging { events("passed", "skipped", "failed") }
}
//endregion

kotlin {
    //region targets
    androidLibrary {
        namespace = "org.patifiner.client.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        androidResources.enable = true

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    jvm()

    wasmJs {
        outputModuleName.set("composeApp")
        browser()
        binaries.executable()
    }

    listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    //endregion

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.koin.compose)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.ui.tooling.preview)

            implementation(libs.compose.lifecycle.viewmodel)
            implementation(libs.compose.lifecycle.runtime)

            implementation(libs.decompose.core)
            implementation(libs.decompose.extensions.compose)
            implementation(libs.decompose.extensions.compose.experimental)
            implementation(libs.essenty.lifecycle)
            implementation(libs.essenty.statekeeper)

            implementation(libs.mvikotlin.core)
            implementation(libs.mvikotlin.main)
            implementation(libs.mvikotlin.coroutines)
            implementation(libs.mvikotlin.logging)

            implementation(libs.settings.core)
            implementation(libs.settings.coroutines)
            implementation(libs.settings.serialization)
            implementation(libs.settings.no.arg)

            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.napier)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.logback.classic)
        }
        jvmTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.koin.test)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.settings.core)
            implementation(libs.allure.junit4)
        }
        wasmJsMain.dependencies {
            implementation(libs.ktor.client.js)
            implementation(libs.napier.wasm)
        }
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.decompose.extensions.android)
            implementation(libs.koin.android)
            implementation(libs.compose.ui.tooling) // is it needed for proper work of layout inspector?
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.settings.core)
        }
    }
}

//region Compose
composeCompiler {
    includeSourceInformation = true
    includeTraceMarkers = true

    stabilityConfigurationFiles.addAll(
        project.layout.projectDirectory.file("stability_config.conf")
    )

    metricsDestination = layout.buildDirectory.dir("compose_compiler")
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
}

compose {
    resources {
        publicResClass = true
        generateResClass = always
        packageOfResClass = "patifinerclient.composeapp.generated.resources"
    }
    desktop {
        application {
            mainClass = "org.patifiner.client.MainKt"

            nativeDistributions {
                targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
                packageName = "org.patifiner.client"
                packageVersion = ptfVersionName
            }
        }
    }
}
//endregion

dependencies {
    androidRuntimeClasspath(libs.compose.ui.tooling)
    allureAgent(libs.aspectj.weaver)
}
