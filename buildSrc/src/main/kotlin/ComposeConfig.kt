
import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.KotlinMultiplatformAndroidComponentsExtension
import org.gradle.api.Project
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.resources.ResourcesExtension

/**
 * Configures Compose Multiplatform resources and wires them into the androidMain variant's
 * ASSETS artifact. The assets wiring is a workaround for a Compose MP 1.10.x bug with AGP 9's
 * com.android.kotlin.multiplatform.library: variant.sources.assets is null for androidMain, so
 * copyAndroidMainComposeResourcesToAndroidAssets never gets its outputDirectory configured and
 * the resources are missing from the AAR.
 */
@Suppress("UnstableApiUsage")
fun Project.composeResources(
    packageOfResClass: String,
    publicResClass: Boolean = false,
    generateResClass: ResourcesExtension.ResourceClassGeneration = ResourcesExtension.ResourceClassGeneration.Always,
) {
    extensions.getByType(ComposeExtension::class.java)
        .extensions.getByType(ResourcesExtension::class.java).apply {
            this.packageOfResClass = packageOfResClass
            this.publicResClass = publicResClass
            this.generateResClass = generateResClass
        }

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
