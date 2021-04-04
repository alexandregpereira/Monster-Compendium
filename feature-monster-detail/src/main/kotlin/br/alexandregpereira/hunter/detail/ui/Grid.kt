package br.alexandregpereira.hunter.detail.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode

@Composable
fun Grid(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) = FlowRow(
    modifier.fillMaxWidth(),
    mainAxisSize = SizeMode.Wrap,
    mainAxisAlignment = FlowMainAxisAlignment.SpaceEvenly,
    mainAxisSpacing = 16.dp,
    crossAxisSpacing = 16.dp,
    content = content
)
