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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmptyScreenMessage(
    title: String,
    modifier: Modifier = Modifier,
    description: String = "",
    buttonText: String = "",
    onButtonClick: () -> Unit = {}
) = Window {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier
                .align(Alignment.Center)
                .padding(16.dp)
        ) {
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
        }
    }
}

@Preview
@Composable
private fun EmptyScreenPreview() {
    EmptyScreenMessage(
        title = "No Internet Connection",
        description = "Try something before trying again",
        buttonText = "Try Again"
    )
}

@Preview
@Composable
private fun EmptyScreenPreview2() {
    EmptyScreenMessage(
        title = "No Internet Connection",
        description = "Try something before trying again",
    )
}

@Preview
@Composable
private fun EmptyScreenPreview3() {
    EmptyScreenMessage(
        title = "No Internet Connection",
        buttonText = "Try Again"
    )
}

@Preview
@Composable
private fun EmptyScreenPreview4() {
    EmptyScreenMessage(
        title = "No Internet Connection",
    )
}
