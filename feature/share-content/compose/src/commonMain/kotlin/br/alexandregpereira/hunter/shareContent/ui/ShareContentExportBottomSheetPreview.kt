package br.alexandregpereira.hunter.shareContent.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import br.alexandregpereira.hunter.shareContent.state.ShareContentExportState
import br.alexandregpereira.hunter.shareContent.state.ShareContentExtractedEntryState
import br.alexandregpereira.hunter.shareContent.state.ShareContentExtractedState
import br.alexandregpereira.hunter.ui.compose.PreviewWindow
import kotlinx.collections.immutable.persistentListOf

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun ShareContentExportBottomSheetPreview() = PreviewWindow {
    val state = ShareContentExportState(
        isOpen = true,
        exportExtractedState = ShareContentExtractedState(
            contentTitle = "My Content",
            contentDescription = "This content is a test to test the test of the content test",
            isContentEditable = false,
            fileName = "File Name",
            contentSize = "400 MB",
            contentEntries = persistentListOf(
                ShareContentExtractedEntryState(
                    quantity = "50 Monsters",
                    content = "Monster 1, Monster 2, Monster 3, Monster 4",
                    contentWarning = "This content will override the monster content",
                ),
                ShareContentExtractedEntryState(
                    quantity = "50 Lore entries",
                    content = "Monster 1, Monster 2, Monster 3",
                ),
                ShareContentExtractedEntryState(
                    quantity = "20 Spells",
                    enabled = false,
                    content = "Spell 1, Spell 2, Spell 3, Spell 5, Spell 65",
                    contentWarning = "This content will override the monster content",
                ),
                ShareContentExtractedEntryState(
                    quantity = "10 Local images",
                    content = "Image 1, Image 2, Image 3",
                ),
            ),
        )
    )
    ShareContentExportBottomSheet(
        state = state,
        onClose = {},
        onExportToFile = {},
        onEditContentTitle = {},
        onEditContentDescription = {},
    )
}
