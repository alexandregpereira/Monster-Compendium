/*
 * Copyright (C) 2026 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.settings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.AppButtonSize
import br.alexandregpereira.hunter.ui.compose.AppCard
import br.alexandregpereira.hunter.ui.compose.animatePressed
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
internal fun PremiumCard(
    title: String,
    description: String,
    buttonText: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    AppCard(
        modifier = modifier.animatePressed(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        elevation = 8.dp,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Star,
                tint = MaterialTheme.colors.primary,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
                Text(
                    text = description,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                )
            }
            AppButton(
                text = buttonText,
                size = AppButtonSize.VERY_SMALL,
                isPrimary = true,
                modifier = Modifier.weight(.7f),
                onClick = onClick,
            )
        }
    }
}

@Preview
@Composable
private fun PremiumCardPreview() = HunterTheme {
    PremiumCard(
        title = "Compendium Premium dadaasd",
        description = "Remove ads",
        buttonText = "Saiba mais",
    )
}
