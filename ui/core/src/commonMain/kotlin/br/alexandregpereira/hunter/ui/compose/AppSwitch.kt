package br.alexandregpereira.hunter.ui.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppSwitch(
    checked: Boolean,
    description: String,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit = {}
) = Row(
    verticalAlignment = CenterVertically,
) {
    Switch(
        checked = checked,
        modifier = modifier,
        onCheckedChange = onCheckedChange
    )
    Spacer(Modifier.padding(start = 8.dp))
    Text(
        text = description,
    )
}
