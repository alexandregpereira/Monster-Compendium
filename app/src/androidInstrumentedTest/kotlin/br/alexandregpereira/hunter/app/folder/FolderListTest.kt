package br.alexandregpereira.hunter.app.folder

import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import br.alexandregpereira.hunter.app.HunterApp
import org.junit.Rule
import org.junit.Test

class FolderListTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun folderDeletion() {
        composeTestRule.setContent {
            HunterApp()
        }

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onNodeWithText("Aboleth").isDisplayed()
        }
        composeTestRule.onNodeWithText("Aboleth").performClick()
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onNodeWithContentDescription("MonsterOptions").isDisplayed()
        }
        composeTestRule.onNodeWithContentDescription("MonsterOptions").performClick()
        composeTestRule.onNodeWithText("Add to Folder").performClick()
        composeTestRule.onNodeWithText("Folder name").performTextInput("Folder Test")
        composeTestRule.onNodeWithText("Save").performClick()
        composeTestRule.onNodeWithContentDescription("Close").performClick()
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onNodeWithText("Folders").isDisplayed()
        }
        composeTestRule.onNodeWithText("Folders").performClick()
        composeTestRule.onNodeWithText("Folder Test").performTouchInput { longClick() }
        composeTestRule.onNodeWithText("Delete").performClick()
        composeTestRule.onNodeWithText("Folder Test").assertIsNotDisplayed()
    }
}
