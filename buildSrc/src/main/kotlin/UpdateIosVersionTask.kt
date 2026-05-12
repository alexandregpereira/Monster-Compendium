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

    @get:Input
    abstract val admobAppId: Property<String>

    @get:OutputDirectory
    abstract val iosAppDir: DirectoryProperty

    init {
        description = "Updates MARKETING_VERSION, CURRENT_PROJECT_VERSION and GAD_APPLICATION_IDENTIFIER in the CocoaPods xcconfig files."
        group = "build"
    }

    @TaskAction
    fun update() {
        val podsXcconfigDir = iosAppDir.get().asFile
            .resolve("Pods/Target Support Files/Pods-MonsterCompendium")
        if (!podsXcconfigDir.exists()) return

        podsXcconfigDir.listFiles { f -> f.extension == "xcconfig" }?.forEach { xcconfig ->
            val lines = xcconfig.readLines()
                .filter {
                    !it.startsWith("MARKETING_VERSION")
                        && !it.startsWith("CURRENT_PROJECT_VERSION")
                        && !it.startsWith("GAD_APPLICATION_IDENTIFIER")
                }
            val updated = (lines + listOf(
                "MARKETING_VERSION = ${versionName.get()}",
                "CURRENT_PROJECT_VERSION = ${versionCode.get()}",
                "GAD_APPLICATION_IDENTIFIER = ${admobAppId.get()}",
            )).joinToString("\n")
            xcconfig.writeText(updated)
        }
    }
}
