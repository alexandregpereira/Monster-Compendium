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
            freeCompilerArgs.add("-Xopt-in=kotlin.RequiresOptIn")
            freeCompilerArgs.add("-Xopt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi")
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
