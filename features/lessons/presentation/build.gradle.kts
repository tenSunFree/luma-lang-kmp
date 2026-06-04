plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.kotlin.serialization)
    id(libs.plugins.build.koin.compose.get().pluginId)
    id(libs.plugins.build.common.get().pluginId)
    id(libs.plugins.build.compose.multiplatform.get().pluginId)
}

compose.resources {
    generateResClass = never
}

val packageName = "com.sun.kmpstartertemplaterefined.feature_lessons_presentation"

kotlin {
    androidLibrary {
        namespace = packageName
        compileSdk {
            version = release(version = libs.versions.android.compileSdk.get().toInt())
        }
        minSdk {
            version = release(libs.versions.android.minSdk.get().toInt())
        }
    }

    val xcfName = "starter:featureLessonsPresentationKit"

    iosArm64 {
        binaries.framework { baseName = xcfName }
    }
    iosSimulatorArm64 {
        binaries.framework { baseName = xcfName }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(projects.features.lessons.domain)
                // Navigation
                implementation(projects.features.navigation)
            }
        }
        androidMain.dependencies {
            implementation(libs.android.youtube.player)
        }
        androidMain.dependencies { }
    }
}