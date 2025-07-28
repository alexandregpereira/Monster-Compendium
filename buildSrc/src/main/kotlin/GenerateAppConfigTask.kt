import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

abstract class GenerateAppConfigTask : DefaultTask() {

    @get:Input
    abstract val taskVersionName: Property<String>

    @get:Input
    abstract val taskVersionCode: Property<Int>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    init {
        description = "Generates the AppConfig.kt file with version information."
        group = "build"

        val path = "generated/source/appConfig/kotlin"
        outputDir.convention(project.layout.buildDirectory.dir(path))
    }

    @TaskAction
    fun generate() {
        val versionName = taskVersionName.get()
        val versionCode = taskVersionCode.get()

        val targetDir = outputDir.get().asFile
        val packagePath = "br/alexandregpereira/hunter/app"
        val outputFile = File(File(targetDir, packagePath), "AppConfig.kt")

        outputFile.parentFile.mkdirs()

        outputFile.writeText("""
            package br.alexandregpereira.hunter.app

            object AppConfig {
                const val VERSION_NAME: String = "$versionName"
                const val VERSION_CODE: Int = $versionCode
            }
        """.trimIndent())
    }
}

@Suppress("NewApi")
fun getVersionCodeAndVersionName(): Pair<Int, String> {
    val localDateTime = Instant.now().atOffset(ZoneOffset.UTC).toLocalDateTime()
    val baseTimestampInSeconds = 1467504000L // Base value for the timestamp of 2013-07-03 00:00:00
    val currentTimestampInSeconds = localDateTime.toEpochSecond(ZoneOffset.UTC)
    val timestampDiff = currentTimestampInSeconds - baseTimestampInSeconds
    if (timestampDiff <= 0) {
        throw RuntimeException("Adjust your machine datetime! timestampDiff")
    }
    val versionCodePerSeconds = 60 * 20 // Seconds to increment the version code
    val versionCode = (timestampDiff / versionCodePerSeconds).toInt()
    val dateFormat = localDateTime.format(DateTimeFormatter.ofPattern("yy.MM.dd"))
    val versionName = (dateFormat + versionCode).take(11)
    return versionCode to versionName
}
