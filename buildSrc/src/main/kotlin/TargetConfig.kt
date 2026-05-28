/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import com.android.build.api.variant.KotlinMultiplatformAndroidComponentsExtension
import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

private val Project.kotlin: KotlinMultiplatformExtension
    get() = extensions.getByType(KotlinMultiplatformExtension::class.java)

private val Project.java: JavaPluginExtension
    get() = extensions.getByType(JavaPluginExtension::class.java)

fun Project.androidLibrary(
    block: KotlinMultiplatformAndroidLibraryExtension.() -> Unit = {}
) {
    kotlin.extensions.getByType(KotlinMultiplatformAndroidLibraryExtension::class.java).apply {
        compileSdk = (findProperty("android.compileSdk") ?: findProperty("compileSdk"))?.toString()?.toInt()
        minSdk = (findProperty("android.minSdk") ?: findProperty("minSdk"))?.toString()?.toInt()
        block()
    }
}

fun Project.multiplatform(block: KotlinMultiplatformExtension.() -> Unit) {
    kotlin.apply {
        block()

        sourceSets.apply {
            all {
                it.languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
                it.languageSettings.optIn("kotlin.experimental.ExperimentalObjCRefinement")
            }
        }

        compilerOptions {
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
        }
    }

    java.apply {
        toolchain.apply {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }
}

fun KotlinMultiplatformExtension.commonMain(block: KotlinDependencyHandler.() -> Unit = {}) {
    sourceSets.apply {
        commonMain.dependencies(block)
    }
}

fun KotlinMultiplatformExtension.commonTest(block: KotlinDependencyHandler.() -> Unit = {}) {
    sourceSets.apply {
        commonTest.dependencies(block)
    }
}

fun KotlinMultiplatformExtension.androidMain(block: KotlinDependencyHandler.() -> Unit = {}) {
    if (!project.plugins.hasPlugin("com.android.kotlin.multiplatform.library")) {
        androidTarget()
    }

    sourceSets.apply {
        androidMain.dependencies(block)
    }
}

fun KotlinMultiplatformExtension.jvmMain(block: KotlinDependencyHandler.() -> Unit = {}) {
    jvm()

    sourceSets.apply {
        jvmMain.dependencies(block)
    }
}

fun KotlinMultiplatformExtension.jvmTest(block: KotlinDependencyHandler.() -> Unit = {}) {
    jvm()
    sourceSets.apply {
        jvmTest.dependencies(block)
    }
}

/**
 * Wires Compose Multiplatform resources into the androidMain variant's ASSETS artifact.
 * Workaround for Compose MP 1.10.x bug with AGP 9's com.android.kotlin.multiplatform.library:
 * variant.sources.assets is null for androidMain, so copyAndroidMainComposeResourcesToAndroidAssets
 * never gets its outputDirectory configured and the resources are missing from the AAR.
 */
@Suppress("UnstableApiUsage")
fun Project.configureComposeAssetsForAndroidMain(packageOfResClass: String) {
    extensions.findByType(KotlinMultiplatformAndroidComponentsExtension::class.java)
        ?.onVariants { variant ->
            if (variant.name != "androidMain") return@onVariants

            val preparedResourcesDir = layout.buildDirectory.dir(
                "generated/compose/resourceGenerator/preparedResources/commonMain/composeResources"
            )

            val copyTask = tasks.register(
                "copyAndroidMainComposeResourcesToAndroidAssetsFixed",
                ComposeResourcesToAndroidMainAssetsTask::class.java
            ) { task ->
                task.dependsOn(tasks.named("prepareComposeResourcesTaskForCommonMain"))
                task.composeResources.from(preparedResourcesDir)
                task.packageName.set(packageOfResClass)
            }

            variant.artifacts.use(copyTask)
                .wiredWith(ComposeResourcesToAndroidMainAssetsTask::outputDirectory)
                .toCreate(SingleArtifact.ASSETS)
        }
}

fun KotlinMultiplatformExtension.iosMain(
    block: KotlinDependencyHandler.() -> Unit = {}
) {
    listOf(
        iosArm64(),
        if (Os.isArch("aarch64")) iosSimulatorArm64() else iosX64()
    ).onEach {
        it.binaries.framework {
            baseName = "KotlinApp"
            isStatic = true
        }
    }

    sourceSets.apply {
        iosMain.dependencies(block)
    }
}
