/*
 * Copyright (C) 2026 Alexandre Gomes Pereira
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

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.provider.Property
import org.gradle.api.tasks.IgnoreEmptyDirectories
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

/**
 * Copies Compose Multiplatform resources into the androidMain variant's ASSETS artifact,
 * working around a Compose MP 1.10.x incompatibility with AGP 9's
 * com.android.kotlin.multiplatform.library plugin where variant.sources.assets is null
 * for the androidMain compilation variant, leaving outputDirectory unconfigured on
 * copyAndroidMainComposeResourcesToAndroidAssets.
 */
abstract class ComposeResourcesToAndroidMainAssetsTask : DefaultTask() {

    @get:Inject
    abstract val fileSystem: FileSystemOperations

    @get:InputFiles
    @get:IgnoreEmptyDirectories
    @get:Optional
    abstract val composeResources: ConfigurableFileCollection

    @get:Input
    abstract val packageName: Property<String>

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun action() {
        fileSystem.copy {
            it.includeEmptyDirs = false
            it.from(composeResources)
            it.into(outputDirectory.dir("composeResources/${packageName.get()}"))
        }
    }
}
