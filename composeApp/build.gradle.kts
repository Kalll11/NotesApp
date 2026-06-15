import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    id("app.cash.sqldelight") version "2.0.2"
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    jvmToolchain(21)

    androidTarget()

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation("io.ktor:ktor-client-android:2.3.8")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.8")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.8")
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            implementation("io.insert-koin:koin-android:3.5.3")
            implementation("io.insert-koin:koin-androidx-compose:3.5.3")
            implementation("androidx.navigation:navigation-compose:2.7.7")
            implementation("app.cash.sqldelight:android-driver:2.0.2")
            implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
            implementation("com.russhwolf:multiplatform-settings:1.1.1")

            // Ktor untuk HTTP Request ke Gemini API
            implementation("io.ktor:ktor-client-okhttp:2.3.8")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.8")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.8")
        }
        commonMain.dependencies {
            implementation("org.jetbrains.compose.material:material-icons-extended:1.7.3")
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // Koin
            implementation("io.insert-koin:koin-core:3.5.3")
            implementation("io.insert-koin:koin-compose:1.1.2")

            // Ekstensi SQLDelight
            implementation("app.cash.sqldelight:coroutines-extensions:2.0.2")
            implementation("app.cash.sqldelight:primitive-adapters:2.0.2")

            implementation("io.ktor:ktor-client-core:2.3.8")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.namakamu.notesapp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        // Konfigurasi pembacaan API Key dari local.properties
        val localProps = project.rootProject.file("local.properties")
        val properties = Properties()
        if (localProps.exists()) {
            properties.load(localProps.inputStream())
        }
        buildConfigField("String", "GEMINI_API_KEY", "\"${properties.getProperty("GEMINI_API_KEY", "")}\"")

        applicationId = "com.namakamu.notesapp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
}

sqldelight {
    databases {
        create("NotesDatabase") {
            packageName.set("com.namakamu.notesapp.db")
            dialect("app.cash.sqldelight:sqlite-3-24-dialect:2.0.2")
        }
    }
}