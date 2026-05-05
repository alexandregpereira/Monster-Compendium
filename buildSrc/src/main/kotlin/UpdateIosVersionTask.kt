import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

abstract class UpdateIosVersionTask : DefaultTask() {

    @get:Input
    abstract val versionName: Property<String>

    @get:Input
    abstract val versionCode: Property<Int>

    @get:OutputDirectory
    abstract val iosAppDir: DirectoryProperty

    init {
        description = "Updates MARKETING_VERSION and CURRENT_PROJECT_VERSION in the CocoaPods xcconfig files."
        group = "build"
    }

    @TaskAction
    fun update() {
        val podsXcconfigDir = iosAppDir.get().asFile
            .resolve("Pods/Target Support Files/Pods-MonsterCompendium")
        if (!podsXcconfigDir.exists()) return

        podsXcconfigDir.listFiles { f -> f.extension == "xcconfig" }?.forEach { xcconfig ->
            val lines = xcconfig.readLines()
                .filter { !it.startsWith("MARKETING_VERSION") && !it.startsWith("CURRENT_PROJECT_VERSION") }
            val updated = (lines + listOf(
                "MARKETING_VERSION = ${versionName.get()}",
                "CURRENT_PROJECT_VERSION = ${versionCode.get()}",
            )).joinToString("\n")
            xcconfig.writeText(updated)
        }
    }
}
