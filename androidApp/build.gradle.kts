import java.util.Properties

private fun getVersionCode(): Int {
    val versionMajor = libs.versions.app.version.major.get().toInt()
    val versionMinor = libs.versions.app.version.minor.get().toInt()
    val versionPatch = libs.versions.app.version.patch.get().toInt()
    return (versionMajor * 10000) + (versionMinor * 100) + versionPatch
}

private fun getVersionName(): String {
    val versionMajor = libs.versions.app.version.major.get().toInt()
    val versionMinor = libs.versions.app.version.minor.get().toInt()
    val versionPatch = libs.versions.app.version.patch.get().toInt()
    return "$versionMajor.$versionMinor.$versionPatch"
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.services)
}

val localProps = Properties().apply {
    rootProject.file("local.properties")
        .takeIf { it.exists() }
        ?.inputStream()
        ?.use { load(it) }
}

val authBaseUrl: String = localProps.getProperty(
    "AUTH_BASE_URL",
    "http://10.0.2.2:8080/api/v1"  // Default value: Android emulator
)

android {
    namespace = "com.sun.kmpstartertemplaterefined.androidapp"
    compileSdk {
        version = release(libs.versions.android.compileSdk.get().toInt())
    }

    defaultConfig {
        buildFeatures {
            buildConfig = true
        }
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        applicationId = "com.sun.kmpstartertemplaterefined"

        versionCode = getVersionCode()
        versionName = getVersionName()

        val buildMessage = "versionCode: $versionCode, versionName: $versionName"
        println(buildMessage)

        buildConfigField(
            "String",
            "AUTH_BASE_URL",
            "\"$authBaseUrl\""
        )
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(projects.composeApp)
    implementation(projects.features.live.presentation)
    implementation(libs.accompanist.system.ui.controller)

    testImplementation(libs.junit)
}