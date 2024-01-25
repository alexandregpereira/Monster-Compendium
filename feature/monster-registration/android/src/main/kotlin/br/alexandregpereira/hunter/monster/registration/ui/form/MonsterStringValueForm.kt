package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import br.alexandregpereira.hunter.ui.compose.AppTextField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterStringValueForm(
    key: String,
    title: @Composable () -> String,
    value: String,
    onChanged: (String) -> Unit = {}
) = FormLazy(
    key = key,
    title = title,
) {
    formItem(key = "$key-string-value") {
        AppTextField(
            text = value,
            label = title(),
            onValueChange = onChanged
        )
    }
}
