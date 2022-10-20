/*
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
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
    subTitle: String?,
    modifier: Modifier = Modifier,
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
