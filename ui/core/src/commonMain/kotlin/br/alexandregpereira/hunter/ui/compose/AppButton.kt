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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AppButton(
    text: String,
    modifier: Modifier = Modifier,
    size: AppButtonSize = AppButtonSize.MEDIUM,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    Button(
        enabled = enabled,
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.surface,
        ),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 8.dp,
            pressedElevation = 2.dp,
        ),
        modifier = modifier
            .height(size.height.dp)
            .fillMaxWidth()
            .animatePressed(),
        onClick = onClick
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        )
    }
}

enum class AppButtonSize(val height: Int) {
    SMALL(40),
    MEDIUM(48),
}

@Preview
@Composable
private fun AppButtonPreview() = Window {
    Column {
        AppButton(text = "Text")
        AppButton(
            text = "Text Disabled",
            enabled = false,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}
