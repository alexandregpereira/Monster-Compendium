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

package br.alexandregpereira.hunter.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
fun EmptyScreenMessage(
    title: String,
    modifier: Modifier = Modifier,
    description: String = "",
    buttonText: String = "",
    secondaryButtonText: String = "",
    icon: ImageVector? = null,
    onButtonClick: () -> Unit = {},
    onSecondaryButtonClick: () -> Unit = {},
) = Window {
    Box(modifier = Modifier.fillMaxSize()) {
        EmptyScreenMessageContent(
            icon = icon,
            title = title,
            description = description,
            buttonText = buttonText,
            secondaryButtonText = secondaryButtonText,
            onButtonClick = onButtonClick,
            onSecondaryButtonClick = onSecondaryButtonClick,
            modifier = modifier.align(Alignment.Center).padding(16.dp)
        )
    }
}

@Composable
fun EmptyScreenMessageContent(
    title: String,
    modifier: Modifier = Modifier,
    description: String = "",
    buttonText: String = "",
    secondaryButtonText: String = "",
    icon: ImageVector? = null,
    onButtonClick: () -> Unit = {},
    onSecondaryButtonClick: () -> Unit = {},
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        icon?.let {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(92.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        description.takeIf { it.isNotBlank() }?.let {
            Text(
                text = it,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
        }

        buttonText.takeIf { it.isNotBlank() }?.let {
            AppButton(
                text = it,
                onClick = onButtonClick,
                modifier = Modifier.padding(top = 24.dp)
            )
        }

        secondaryButtonText.takeIf { it.isNotBlank() }?.let {
            AppButton(
                text = it,
                isPrimary = false,
                onClick = onSecondaryButtonClick,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}

@Preview
@Composable
private fun EmptyScreenPreview() = HunterTheme {
    EmptyScreenMessage(
        title = "No Internet Connection",
        description = "Try something before trying again",
        buttonText = "Try Again"
    )
}

@Preview
@Composable
private fun EmptyScreenPreview6() = HunterTheme {
    EmptyScreenMessage(
        icon = Icons.Filled.CheckCircle,
        title = "No Internet Connection",
        description = "Try something before trying again",
        buttonText = "Try Again"
    )
}

@Preview
@Composable
private fun EmptyScreenPreview5() = HunterTheme {
    EmptyScreenMessage(
        title = "No Internet Connection",
        description = "Try something before trying again",
        buttonText = "Try Again",
        secondaryButtonText = "Contact Support"
    )
}

@Preview
@Composable
private fun EmptyScreenPreview2() = HunterTheme {
    EmptyScreenMessage(
        title = "No Internet Connection",
        description = "Try something before trying again",
    )
}

@Preview
@Composable
private fun EmptyScreenPreview3() = HunterTheme {
    EmptyScreenMessage(
        title = "No Internet Connection",
        buttonText = "Try Again"
    )
}

@Preview
@Composable
private fun EmptyScreenPreview4() = HunterTheme {
    EmptyScreenMessage(
        title = "No Internet Connection",
    )
}
