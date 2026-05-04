/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.shareContent

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import platform.Foundation.NSURL
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@Composable
internal actual fun ShareFileTrigger(
    filePath: String,
    onClosed: () -> Unit,
) {
    LaunchedEffect(filePath) {
        val url = NSURL.URLWithString(filePath) ?: return@LaunchedEffect
        dispatch_async(dispatch_get_main_queue()) {
            val activityController = UIActivityViewController(
                activityItems = listOf(url),
                applicationActivities = null,
            )
            activityController.completionWithItemsHandler = { _, _, _, _ ->
                onClosed()
            }
            UIApplication.sharedApplication.keyWindow
                ?.rootViewController
                ?.presentViewController(
                    activityController,
                    animated = true,
                    completion = null,
                )
        }
    }
}
