package br.alexandregpereira.hunter.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MonsterTypeIcon(
    type: MonsterItemType,
    iconSize: Dp,
    modifier: Modifier = Modifier
) = Box(
    contentAlignment = Alignment.TopEnd,
    modifier = modifier
        .fillMaxWidth()
        .padding(8.dp),
) {
    Icon(
        painter = painterResource(type.iconRes),
        contentDescription = type.name,
        modifier = Modifier
            .size(iconSize)
            .alpha(0.7f)
    )
}