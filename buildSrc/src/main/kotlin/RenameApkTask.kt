import com.android.build.api.artifact.ArtifactTransformationRequest
import com.android.build.api.variant.BuiltArtifact
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class RenameApkTask : DefaultTask() {

    @get:InputDirectory
    abstract val input: DirectoryProperty

    @get:OutputDirectory
    abstract val output: DirectoryProperty

    @get:Input
    abstract val apkPrefix: Property<String>

    @get:Input
    abstract val apkBuildType: Property<String>

    @get:Input
    abstract val apkVersionName: Property<String>

    @Internal
    var transformationRequest: ArtifactTransformationRequest<RenameApkTask>? = null

    @TaskAction
    fun run() {
        transformationRequest!!.submit(this) { builtArtifact: BuiltArtifact ->
            val inputFile = File(builtArtifact.outputFile)
            val newName = "${apkPrefix.get()}-${apkBuildType.get()}-${apkVersionName.get()}.apk"
            val outputFile = output.get().file(newName).asFile
            inputFile.copyTo(outputFile, overwrite = true)
            outputFile
        }
    }
}
