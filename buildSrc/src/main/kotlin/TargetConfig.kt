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

import com.android.build.api.dsl.LibraryExtension
import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

private val Project.kotlin: KotlinMultiplatformExtension
    get() = extensions.getByType(KotlinMultiplatformExtension::class.java)

private val Project.java: JavaPluginExtension
    get() = extensions.getByType(JavaPluginExtension::class.java)

private val Project.androidLibrary: LibraryExtension
    get() = extensions.getByType(LibraryExtension::class.java)

fun Project.androidLibrary(
    withCompose: Boolean = true,
    block: LibraryExtension.() -> Unit
) {
    androidLibrary.apply {
        compileSdk = findProperty("compileSdk")?.toString()?.toInt()

        defaultConfig {
            minSdk = findProperty("minSdk")?.toString()?.toInt()
        }

        if (withCompose) {
            buildFeatures {
                compose = true
            }
        }

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

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
            freeCompilerArgs.add("-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi")
            freeCompilerArgs.add("-opt-in=androidx.compose.animation.ExperimentalAnimationApi")
            freeCompilerArgs.add("-opt-in=androidx.compose.foundation.ExperimentalFoundationApi")
        }
    }

    java.apply {
        toolchain.apply {
            languageVersion.set(JavaLanguageVersion.of(17))
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
    androidTarget()

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
    sourceSets.apply {
        jvmTest.dependencies(block)
    }
}

fun KotlinMultiplatformExtension.iosMain(
    block: KotlinDependencyHandler.() -> Unit = {}
) {
    if (Os.isArch("aarch64")) {
        listOf(
            iosArm64(),
            iosSimulatorArm64()
        )
    } else {
        listOf(
            iosX64()
        )
    }.onEach {
        it.binaries.framework {
            baseName = "KotlinApp"
            isStatic = true
        }
    }

    sourceSets.apply {
        iosMain.dependencies(block)
    }
}
