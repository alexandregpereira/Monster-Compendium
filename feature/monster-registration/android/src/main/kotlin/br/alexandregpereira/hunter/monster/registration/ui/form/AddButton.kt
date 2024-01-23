package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.monster.registration.R

@Composable
internal fun AddButton(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit = {},
) = Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
        .height(40.dp)
        .fillMaxWidth()
        .clickable { onClick() },
) {
    val string = text.ifEmpty {
        stringResource(R.string.monster_registration_add_new)
    }
    Icon(
        painter = painterResource(R.drawable.ic_add),
        contentDescription = string,
        modifier = Modifier.padding(end = 8.dp),
    )
    Text(
        text = string,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
    )
}
