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

    packagingOptions {
        jniLibs {
            excludes += "META-INF/licenses/**"
        }
        resources {
            excludes += "**/attach_hotspot_windows.dll"
            excludes += "META-INF/licenses/**"
            excludes += "META-INF/AL2.0"
            excludes += "META-INF/LGPL2.1"
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
        val baseTimestampInSeconds = 1680040800L // Base value for the timestamp of 2023-03-28 22:00:00
        val currentTimestampInSeconds = localDateTime.toEpochSecond(ZoneOffset.UTC)
        val timestampDiff = currentTimestampInSeconds - baseTimestampInSeconds
        if (timestampDiff <= 0) {
            throw RuntimeException("Adjust your machine datetime! timestampDiff")
        }
        val versionCodePerSeconds = 60 // Seconds to increment the version code
        versionCode = (timestampDiff / versionCodePerSeconds).toInt()

        versionName = localDateTime.format(DateTimeFormatter.ofPattern("yy.MM.dd.HHmm"))
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    implementation(project(":core:analytics"))
    implementation(project(":data:data-app"))
    implementation(project(":domain:app"))
    implementation(project(":event:folder-detail-event"))
    implementation(project(":event:folder-list-event"))
    implementation(project(":event:folder-preview-event"))
    implementation(project(":event:monster-content-manager-event"))
    implementation(project(":event:monster-detail-event"))
    implementation(project(":android:folder-detail"))
    implementation(project(":android:folder-insert"))
    implementation(project(":android:folder-list"))
    implementation(project(":android:folder-preview"))
    implementation(project(":android:monster-compendium"))
    implementation(project(":android:monster-content-manager"))
    implementation(project(":android:monster-detail"))
    implementation(project(":android:monster-lore-detail"))
    implementation(project(":android:sync"))
    implementation(project(":feature:search"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:spell-detail"))
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
}
