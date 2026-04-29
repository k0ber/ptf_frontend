@file:OptIn(ExperimentalWasmDsl::class)

import com.android.ide.common.symbols.parseMinifiedKeepRules
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.stability.analyzer)
}

val isReleaseBuild: Boolean by extra
val ptfVersionName: String by extra
val ptfVersionCode: Int by extra

//region Allure
val allureAgent: Configuration by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
    isTransitive = false
}

val agentFileProvider: Provider<File> = provider { allureAgent.singleFile }
val allureResultsDir: String = layout.projectDirectory
    .dir("../build/allure-results")
    .asFile.absolutePath

tasks.withType<Test>().configureEach {
    systemProperty("allure.results.directory", allureResultsDir)
    val agentPath = agentFileProvider.map { it.absolutePath }
    doFirst {
        jvmArgs("-javaagent:${agentPath.get()}")
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
        outputModuleName.set("PtfApp")
        browser()
        binaries.executable()
    }

    listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "PtfApp"
            isStatic = true
        }
    }
    //endregion

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.navigation3)

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

            implementation(libs.filekit.core)
            implementation(libs.filekit.compose)
            implementation(libs.filekit.coil)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)

            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.navigation3.ui)

            implementation(libs.orbit.core)
            implementation(libs.orbit.viewmodel)
            implementation(libs.orbit.compose)

            implementation(libs.settings.core)
            implementation(libs.settings.coroutines)
            implementation(libs.settings.serialization)
            implementation(libs.settings.no.arg)

            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.collections.immutable)

            implementation(libs.napier)
            implementation(libs.compose.stability.runtime)
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
            implementation(libs.koin.android)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.settings.core)
        }
    }
}

composeStabilityAnalyzer {
    stabilityValidation {
        enabled.set(!isReleaseBuild)
        outputDir.set(layout.projectDirectory.dir("stability"))
        ignoredClasses.set(listOf(".*Preview.*", ".*ComposableSingletons.*"))
        failOnStabilityChange.set(!isReleaseBuild)
    }
}

//region Compose
composeCompiler {
    includeSourceInformation = true
    includeTraceMarkers = true

    stabilityConfigurationFiles.addAll(
        project.layout.projectDirectory.file("stability_config.conf")
    )

    if (!project.targetIsWasm()) {
        metricsDestination = layout.buildDirectory.dir("compose_compiler")
        reportsDestination = layout.buildDirectory.dir("compose_compiler")
    }
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
                packageVersion = ptfVersionName.replaceFirst(Regex("^0"), "1") // MAJOR is required to be > 0
            }
            // todo test proguard on desktop
            parseMinifiedKeepRules(project.file("proguard-rules.pro"))
        }
    }
}
//endregion

dependencies {
    androidRuntimeClasspath(libs.compose.ui.tooling)
    allureAgent(libs.aspectj.weaver)
}

fun Project.targetIsWasm() = gradle.startParameter.taskNames.any { it.contains("wasm", ignoreCase = true) }
