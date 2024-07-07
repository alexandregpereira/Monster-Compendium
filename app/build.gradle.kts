import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/*
 * Copyright 2023 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    id("com.android.application")
    kotlin("multiplatform")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

multiplatform {
    androidMain {
        implementation(project(":core:event"))
        implementation(project(":feature:folder-detail:event"))
        implementation(project(":feature:folder-list:event"))
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

        implementation(libs.core.ktx)
        implementation(libs.appcompat)
        implementation(libs.material)

        implementation(libs.koin.android)
        implementation(libs.koin.compose)

        implementation(libs.bundles.viewmodel.bundle)

        implementation(compose.preview)
        implementation(libs.compose.activity)

        implementation(project.dependencies.platform(libs.firebase.bom))
        implementation(libs.firebase.analytics)
        implementation(libs.firebase.crashlytics)
    }

    commonMain {
        implementation(project(":core:analytics"))
        implementation(project(":core:localization"))
        implementation(project(":domain:app:data"))
        implementation(project(":domain:app:core"))
        implementation(project(":domain:sync:core"))
        implementation(project(":feature:folder-insert:event")) // TODO Remove later
        implementation(project(":feature:folder-preview:event")) // TODO Remove later
        implementation(project(":feature:monster-lore-detail:event")) // TODO Remove later
        implementation(project(":feature:monster-registration:event")) // TODO Remove later
        implementation(project(":feature:spell-detail:event")) // TODO Remove later
        implementation(project(":feature:monster-compendium:state-holder"))
        implementation(project(":feature:monster-detail:state-holder"))
        implementation(project(":feature:sync:state-holder"))
        implementation(libs.kotlin.coroutines.core)
        implementation(libs.koin.core)
        implementation(compose.runtime)
        implementation(compose.foundation)
        implementation(compose.material)
        implementation(compose.ui)
        implementation(compose.components.resources)
        implementation(compose.components.uiToolingPreview)
    }

    jvmMain()
    iosMain(iosFramework = { linkerOpts("-l", "sqlite3") })

    jvmTest {
        implementation(libs.bundles.unittest)
        implementation(libs.koin.test)
    }
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant {
            sourceSetTree.set(KotlinSourceSetTree.test)

            dependencies {
                implementation(libs.bundles.instrumentedtest)
                implementation(libs.compose.ui.test)
                debugImplementation(libs.compose.ui.test.manifest)
            }
        }
    }
}

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
        buildConfig = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}

composeCompiler {
    enableStrongSkippingMode = true
}
