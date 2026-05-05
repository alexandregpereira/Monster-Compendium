package br.alexandregpereira.file

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import kotlinx.coroutines.launch

@Composable
fun rememberImagePickerLauncher(
    onResult: (FileEntry?) -> Unit,
): PickerResultLauncher {
    return rememberFilePickerLauncher(
        fileType = FileType.IMAGE,
        onResult = onResult,
    )
}

@Composable
fun rememberCompendiumFilePickerLauncher(
    onResult: (FileEntry?) -> Unit,
): PickerResultLauncher {
    return rememberFilePickerLauncher(
        fileType = FileType.COMPENDIUM,
        onResult = onResult,
    )
}

@Composable
fun rememberFilePickerLauncher(
    fileType: FileType,
    onResult: (FileEntry?) -> Unit,
): PickerResultLauncher {
    val coroutineScope = rememberCoroutineScope()
    val type = remember(fileType) {
        fileType.toFileKitType()
    }
    val launcher = rememberFilePickerLauncher(
        type = type,
        mode = FileKitMode.Single,
    ) { file ->
        coroutineScope.launch {
            onResult(file?.toFileEntry())
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
