package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.monster.registration.MonsterInfoState
import br.alexandregpereira.hunter.monster.registration.ui.strings
import br.alexandregpereira.hunter.ui.compose.AppSwitch
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.Form
import br.alexandregpereira.hunter.ui.compose.MonsterCoilImage
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
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = TopCenter
                ) {
                    val imageWidth by animateDpAsState(targetValue = if (infoState.isImageHorizontal) 360.dp else 270.dp)
                    val imageHeight by animateDpAsState(targetValue = if (infoState.isImageHorizontal) 270.dp else 360.dp)
                    MonsterCoilImage(
                        imageUrl = infoState.imageUrl,
                        contentDescription = infoState.name,
                        backgroundColor = color,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.height(imageHeight).width(imageWidth),
                    )
                }
                AppSwitch(
                    checked = isDarkThemeMutable,
                    description = strings.darkThemeSwitchLabel,
                    onCheckedChange = {
                        isDarkThemeMutable = it
                    },
                    modifier = Modifier,
                )
                AppSwitch(
                    checked = infoState.isImageHorizontal,
                    description = strings.imageHorizontalSwitchLabel,
                    onCheckedChange = {
                        onMonsterChanged(infoState.copy(isImageHorizontal = it))
                    },
                    modifier = Modifier,
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
            AppTextField(
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
            AppTextField(
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
