/*
 * Copyright (c) 2021 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.detail.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.detail.R
import br.alexandregpereira.hunter.ui.compose.AppBarIcon
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
fun MonsterTitleCompose(
    title: String,
    modifier: Modifier = Modifier,
    subTitle: String? = null,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    titleFontSize: MonsterTitleFontSize = MonsterTitleFontSize.LARGE,
    onOptionsClicked: () -> Unit = {}
) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(contentPadding)
    ) {
        MonsterTitle(
            title,
            subTitle,
            Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            titleFontSize
        )

        OptionIcon(Modifier.align(Alignment.CenterVertically), onOptionsClicked)
    }
}

@Composable
private fun MonsterTitle(
    title: String,
    subTitle: String?,
    modifier: Modifier = Modifier,
    titleFontSize: MonsterTitleFontSize = MonsterTitleFontSize.LARGE,
) = Column(modifier) {
    val titleFontSizeValue = when (titleFontSize) {
        MonsterTitleFontSize.LARGE -> 24.sp
        MonsterTitleFontSize.SMALL -> 16.sp
    }
    val titleFontWeight = when (titleFontSize) {
        MonsterTitleFontSize.LARGE -> FontWeight.Bold
        MonsterTitleFontSize.SMALL -> FontWeight.SemiBold
    }
    Text(
        text = title,
        fontSize = titleFontSizeValue,
        fontWeight = titleFontWeight
    )
    subTitle?.let {
        Text(
            text = it,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            fontStyle = FontStyle.Italic
        )
    }
}

@Composable
private fun OptionIcon(
    modifier: Modifier,
    onOptionsClicked: (() -> Unit)? = null
) {
    AppBarIcon(
        Icons.Filled.MoreVert,
        contentDescription = stringResource(R.string.monster_detail_options),
        modifier = modifier,
        onClicked = onOptionsClicked
    )
}

enum class MonsterTitleFontSize {
    LARGE, SMALL
}

@Preview
@Composable
private fun MonsterTitlePreview() {
    HunterTheme {
        MonsterTitleCompose(
            title = "Teste dos testes",
            subTitle = "Teste dos teste testado dos testes"
        ) {}
    }
}