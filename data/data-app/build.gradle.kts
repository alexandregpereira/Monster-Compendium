plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("kapt")
    kotlin("plugin.serialization")
}

kotlin {
    android()
    jvm()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "data"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":data:data-alternative-source"))
                implementation(project(":data:data-monster"))
                implementation(project(":data:data-monster-folder"))
                implementation(project(":data:data-monster-lore"))
                implementation(project(":data:data-settings"))
                implementation(project(":data:data-spell"))
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.kotlin.serialization)
                implementation(libs.koin.core)
                implementation(libs.ktor.core)
                implementation(libs.ktor.logging)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.bundles.retrofit)
                implementation(libs.ktor.okhttp)
                implementation(libs.bundles.room)
                configurations["kapt"]
                    .dependencies
                    .add(project.dependencies.create(libs.room.compiler.get().toString()))
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(libs.bundles.unittest)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.ktor.okhttp)
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(libs.ktor.darwin)
            }
        }
    }
}

android {
    namespace = "br.alexandregpereira.hunter.data"
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
//                compilerArgumentProviders(
//                    RoomSchemaArgProvider(File(projectDir, "schemas"))
//                )
            }
        }
    }
}

//class RoomSchemaArgProvider(
//    @get:InputDirectory
//    @get:PathSensitive(PathSensitivity.RELATIVE)
//    val schemaDir: File
//) : CommandLineArgumentProvider {
//
//    override fun asArguments(): Iterable<String> {
//        return listOf("-Aroom.schemaLocation=${schemaDir.path}")
//    }
//}
