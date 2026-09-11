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

package br.alexandregpereira.hunter.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.compose.AppCircleButton
import br.alexandregpereira.hunter.ui.compose.PreviewWindow

@Composable
internal fun HomeHeader(
    title: String,
    notificationsContentDescription: String,
    menuContentDescription: String,
    hasUnreadNotifications: Boolean,
    modifier: Modifier = Modifier,
    onNotificationClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
) = Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier.fillMaxWidth(),
) {
    Text(
        text = title,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.weight(1f),
    )

    HomeHeaderIconButton(
        imageVector = Icons.Outlined.Notifications,
        contentDescription = notificationsContentDescription,
        showBadge = hasUnreadNotifications,
        onClick = onNotificationClick,
    )

    Spacer(Modifier.size(12.dp))

    HomeHeaderIconButton(
        imageVector = Icons.Filled.Menu,
        contentDescription = menuContentDescription,
        onClick = onMenuClick,
    )
}

@Composable
private fun HomeHeaderIconButton(
    imageVector: ImageVector,
    contentDescription: String,
    showBadge: Boolean = false,
    onClick: () -> Unit,
) = AppCircleButton(
    isPrimary = false,
    onClick = onClick,
) {
    Box {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = LocalContentColor.current,
            modifier = Modifier.size(22.dp),
        )
        if (showBadge) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 1.dp, end = 1.dp)
                    .size(7.dp)
                    .background(color = MaterialTheme.colors.error, shape = CircleShape)
            )
        }
    }
}

@Preview
@Composable
private fun HomeHeaderPreview() = PreviewWindow(darkTheme = true) {
    HomeHeader(
        title = "Compendium",
        notificationsContentDescription = "Notifications",
        menuContentDescription = "Menu",
        hasUnreadNotifications = true,
        modifier = Modifier.padding(16.dp),
    )
}
