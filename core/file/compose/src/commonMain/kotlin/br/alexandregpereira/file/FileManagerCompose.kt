package br.alexandregpereira.file

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.launch

@Composable
fun rememberImagePickerLauncher(
    onResult: (FileEntry) -> Unit,
): PickerResultLauncher {
    return rememberFilePickerLauncher(
        fileType = FileType.IMAGE,
        onResult = onResult,
    )
}

@Composable
fun rememberCompendiumFilePickerLauncher(
    onResult: (FileEntry) -> Unit,
): PickerResultLauncher {
    return rememberFilePickerLauncher(
        fileType = FileType.ZIP,
        onResult = onResult,
    )
}

@Composable
fun rememberFilePickerLauncher(
    fileType: FileType,
    onResult: (FileEntry) -> Unit,
): PickerResultLauncher {
    val coroutineScope = rememberCoroutineScope()
    val type = remember {
        when (fileType) {
            FileType.IMAGE -> FileKitType.Image
            FileType.ZIP -> FileKitType.File(
                extension = "compendium"
            )
        }
    }
    val launcher = rememberFilePickerLauncher(
        type = type,
        mode = FileKitMode.Single,
    ) { file ->
        file?.let {
            coroutineScope.launch {
                onResult(
                    FileEntry(
                        name = it.name,
                        content = it.readBytes(),
                    )
                )
            }
        }
    }
    return remember(launcher) {
        PickerResultLauncher(onLaunch = launcher::launch)
    }
}

class PickerResultLauncher(
    private val onLaunch: () -> Unit,
) {
    fun launch() {
        onLaunch()
    }
}
