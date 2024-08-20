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

package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.domain.model.MonsterImageContentScale
import br.alexandregpereira.hunter.monster.registration.MonsterInfoState
import br.alexandregpereira.hunter.monster.registration.ui.strings
import br.alexandregpereira.hunter.strings.format
import br.alexandregpereira.hunter.ui.compose.AppImageContentScale
import br.alexandregpereira.hunter.ui.compose.AppSwitch
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.ColorTextField
import br.alexandregpereira.hunter.ui.compose.Form
import br.alexandregpereira.hunter.ui.compose.MonsterCoilImage
import br.alexandregpereira.hunter.ui.compose.getMonsterImageAspectRatio
import br.alexandregpereira.hunter.ui.compose.monsterAspectRatio
import br.alexandregpereira.hunter.ui.util.toColor

@Suppress("FunctionName")
internal fun LazyListScope.MonsterImageForm(
    keys: Iterator<String>,
    infoState: MonsterInfoState,
    onMonsterChanged: (MonsterInfoState) -> Unit = {}
) {
    FormLazy(
        titleKey = keys.next(),
        title = { strings.imageFormTitle }
    ) {
        formItem(key = keys.next()) {
            Form {
                val isDarkTheme = isSystemInDarkTheme()
                var isDarkThemeMutable by remember { mutableStateOf(isDarkTheme) }
                val darkColor =
                    remember(infoState.backgroundColorDark) { infoState.backgroundColorDark.toColor() }
                val lightColor =
                    remember(infoState.backgroundColorLight) { infoState.backgroundColorLight.toColor() }
                val color by animateColorAsState(
                    targetValue = if (isDarkThemeMutable) darkColor else {
                        lightColor
                    }
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = CenterHorizontally,
                ) {
                    val widthFraction by animateFloatAsState(
                        targetValue = if (infoState.isImageHorizontal) .8f else .4f
                    )
                    MonsterCoilImage(
                        imageUrl = infoState.imageUrl,
                        contentDescription = infoState.name,
                        backgroundColor = color,
                        contentScale = when (infoState.imageContentScale) {
                            MonsterImageContentScale.Fit -> AppImageContentScale.Fit
                            MonsterImageContentScale.Crop -> AppImageContentScale.Crop
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.widthIn(max = 500.dp).monsterAspectRatio(
                            isHorizontal = infoState.isImageHorizontal,
                            widthFraction = widthFraction,
                        ),
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    val aspectRatio = getMonsterImageAspectRatio(infoState.isImageHorizontal)
                    Text(
                        text = strings.imageProportion.format(aspectRatio),
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp,
                    )
                }
                AppSwitch(
                    checked = isDarkThemeMutable,
                    label = strings.darkThemeSwitchLabel,
                    onCheckedChange = {
                        isDarkThemeMutable = it
                    },
                )
                AppSwitch(
                    checked = infoState.isImageHorizontal,
                    label = strings.imageHorizontalSwitchLabel,
                    onCheckedChange = {
                        onMonsterChanged(infoState.copy(isImageHorizontal = it))
                    },
                )
            }
        }
        formItem(key = keys.next()) {
            AppTextField(
                text = infoState.imageUrl,
                label = strings.imageUrl,
                onValueChange = {
                    onMonsterChanged(infoState.copy(imageUrl = it))
                }
            )
        }
        formItem(key = keys.next()) {
            ColorTextField(
                text = infoState.backgroundColorLight,
                label = strings.imageBackgroundColorLight,
                onValueChange = {
                    onMonsterChanged(
                        infoState.copy(
                            backgroundColorLight = it,
                        )
                    )
                }
            )
        }
        formItem(key = keys.next()) {
            ColorTextField(
                text = infoState.backgroundColorDark,
                label = strings.imageBackgroundColorDark,
                onValueChange = {
                    onMonsterChanged(
                        infoState.copy(
                            backgroundColorDark = it
                        )
                    )
                }
            )
        }
    }
}
