package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import br.alexandregpereira.hunter.ui.compose.AppTextField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterStringValueForm(
    keys: Iterator<String>,
    title: @Composable () -> String,
    value: String,
    onChanged: (String) -> Unit = {}
) = FormLazy(
    titleKey = keys.next(),
    title = title,
) {
    formItem(key = keys.next()) {
        AppTextField(
            text = value,
            label = title(),
            onValueChange = onChanged
        )
    }
}
