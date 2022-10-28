/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScreenHeader(
    title: String,
    modifier: Modifier = Modifier,
    subTitle: String? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    titleFontSize: HeaderFontSize = HeaderFontSize.LARGE,
) = Column(modifier) {
    val titleFontSizeValue = when (titleFontSize) {
        HeaderFontSize.LARGE -> 24.sp
        HeaderFontSize.SMALL -> 16.sp
    }
    val titleFontWeight = when (titleFontSize) {
        HeaderFontSize.LARGE -> FontWeight.Bold
        HeaderFontSize.SMALL -> FontWeight.SemiBold
    }
    val titleBottomPadding = if (subTitle != null) 0.dp else {
        contentPadding.calculateBottomPadding()
    }
    Text(
        text = title,
        fontSize = titleFontSizeValue,
        fontWeight = titleFontWeight,
        modifier = Modifier.padding(
            top = contentPadding.calculateTopPadding(),
            bottom = titleBottomPadding,
            start = contentPadding.calculateStartPadding(LayoutDirection.Rtl)
        )
    )
    subTitle?.let {
        Text(
            text = it,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(
                bottom = contentPadding.calculateBottomPadding(),
                start = contentPadding.calculateStartPadding(LayoutDirection.Rtl)
            )
        )
    }
}

enum class HeaderFontSize {
    LARGE, SMALL
}
