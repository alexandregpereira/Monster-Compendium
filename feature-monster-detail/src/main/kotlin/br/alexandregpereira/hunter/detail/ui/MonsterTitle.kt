/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.detail.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.detail.R
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
fun MonsterTitle(
    title: String,
    subTitle: String,
    onOptionsClicked: () -> Unit = {}
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        MonsterTitle(
            title,
            subTitle,
            Modifier.weight(1f)
        )

        OptionIcon(Modifier.align(Alignment.CenterVertically), onOptionsClicked)
    }
}

@Composable
fun MonsterTitle(
    title: String,
    subTitle: String,
    modifier: Modifier = Modifier
) = Column(modifier) {
    Text(
        text = title,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )
    Text(
        text = subTitle,
        fontSize = 12.sp,
        fontWeight = FontWeight.Light,
        fontStyle = FontStyle.Italic
    )
}

@Composable
fun OptionIcon(
    modifier: Modifier,
    onOptionsClicked: (() -> Unit)? = null
) {
    Icon(
        Icons.Filled.MoreVert,
        contentDescription = stringResource(R.string.monster_detail_options),
        tint = LocalContentColor.current.copy(alpha = 0.7f),
        modifier = modifier
            .let {
                if (onOptionsClicked != null) {
                    it.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(
                            bounded = false,
                            radius = 32.dp
                        ),
                        onClick = onOptionsClicked
                    )
                }
                else it
            }
    )
}

@Preview
@Composable
fun MonsterTitlePreview() {
    HunterTheme {
        MonsterTitle(
            title = "Teste dos testes",
            subTitle = "Teste dos teste testado dos testes"
        ) {}
    }
}