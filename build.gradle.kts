plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.stability.analyzer) apply false

}

// region .ENV
val isReleaseBuild: Boolean = providers.gradleProperty("release")
    .map { it.toBoolean() }.orElse(providers.provider {
        val taskNames = gradle.startParameter.taskNames.joinToString(",").lowercase()
        taskNames.contains("release") || taskNames.contains("bundle")
    })
    .getOrElse(false)

val isCiBuild = providers.gradleProperty("isCi")
    .getOrElse("false").toBoolean()

val versionName: String = providers.gradleProperty("versionName")
    .getOrElse("1.0.0")

val versionCode = providers.gradleProperty("versionCode")
    .getOrElse("1").toInt()

subprojects {
    extra.set("isReleaseBuild", isReleaseBuild)
    extra.set("isCiBuild", isCiBuild)
    extra.set("ptfVersionCode", versionCode)
    extra.set("ptfVersionName", versionName)
}
//endregion
