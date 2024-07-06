import java.time.format.DateTimeFormatter
import java.time.ZoneOffset
import java.time.Instant

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

@Suppress("UnstableApiUsage")
android {
    namespace = "br.alexandregpereira.hunter.app"
    compileSdk = findProperty("compileSdk")?.toString()?.toInt()

    packaging {
        jniLibs {
            excludes += "META-INF/licenses/**"
        }
        resources {
            excludes += "**/attach_hotspot_windows.dll"
            excludes += "META-INF/licenses/**"
            excludes += "META-INF/AL2.0"
            excludes += "META-INF/LGPL2.1"
            excludes += "META-INF/LICENSE**"
        }
    }

    val isDebug = project.gradle.startParameter.taskNames.any { it.contains("Debug") }
    defaultConfig {
        applicationId = "br.alexandregpereira.hunter.app"
        minSdk = findProperty("minSdk")?.toString()?.toInt()
        targetSdk = findProperty("targetSdk")?.toString()?.toInt()
        applicationIdSuffix = when {
            hasProperty("dev") -> {
                ".dev"
            }
            else -> ""
        }

        val localDateTime = Instant.now().atOffset(ZoneOffset.UTC).toLocalDateTime()
        val baseTimestampInSeconds = 1467504000L // Base value for the timestamp of 2013-07-03 00:00:00
        val currentTimestampInSeconds = localDateTime.toEpochSecond(ZoneOffset.UTC)
        val timestampDiff = currentTimestampInSeconds - baseTimestampInSeconds
        if (timestampDiff <= 0) {
            throw RuntimeException("Adjust your machine datetime! timestampDiff")
        }
        val versionCodePerSeconds = 60 * 20 // Seconds to increment the version code
        versionCode = (timestampDiff / versionCodePerSeconds).toInt().also { versionCode ->
            val dateFormat = localDateTime.format(DateTimeFormatter.ofPattern("yy.MM.dd."))
            versionName = dateFormat + (versionCode - 186000)
        }

        versionNameSuffix = when {
            hasProperty("dev") -> {
                "-dev"
            }
            isDebug -> {
                "-debug"
            }
            else -> ""
        }
        if (hasProperty("dev")) {
            setProperty("archivesBaseName", "app-dev")
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("monster-keystore.jks")
            storePassword = System.getenv("MONSTER_COMPENDIUM_KEYSTORE_PASSWORD")
            keyAlias = "monster"
            keyPassword = System.getenv("MONSTER_COMPENDIUM_KEYSTORE_PASSWORD")
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

composeCompiler {
    enableStrongSkippingMode = true
}

dependencies {
    implementation(project(":core:analytics"))
    implementation(project(":core:event"))
    implementation(project(":core:localization"))
    implementation(project(":domain:app:data"))
    implementation(project(":domain:app:core"))
    implementation(project(":feature:folder-detail:event"))
    implementation(project(":feature:folder-list:event"))
    implementation(project(":feature:folder-preview:event"))
    implementation(project(":feature:monster-content-manager:event"))
    implementation(project(":feature:monster-detail:event"))
    implementation(project(":feature:folder-detail:android"))
    implementation(project(":feature:folder-insert:android"))
    implementation(project(":feature:folder-list:android"))
    implementation(project(":feature:folder-preview:android"))
    implementation(project(":feature:monster-compendium:android"))
    implementation(project(":feature:monster-content-manager:android"))
    implementation(project(":feature:monster-detail:android"))
    implementation(project(":feature:monster-lore-detail:android"))
    implementation(project(":feature:monster-registration:android"))
    implementation(project(":feature:sync:android"))
    implementation(project(":feature:search:android"))
    implementation(project(":feature:settings:android"))
    implementation(project(":feature:spell-compendium:android"))
    implementation(project(":feature:spell-detail:android"))
    implementation(project(":ui:core"))

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    implementation(libs.bundles.viewmodel.bundle)
    implementation(libs.bundles.compose)

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)

    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    testImplementation(libs.bundles.unittest)
    testImplementation(libs.koin.test)
    androidTestImplementation(libs.bundles.instrumentedtest)
    androidTestImplementation(libs.compose.ui.test)
    debugImplementation(libs.compose.ui.test.manifest)
}
