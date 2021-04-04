package br.alexandregpereira.hunter.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import br.alexandregpereira.hunter.ui.util.toColor
import com.google.accompanist.coil.CoilImage

@Composable
fun MonsterCoilImage(
    imageUrl: String,
    contentDescription: String,
    height: Dp,
    shape: Shape,
    backgroundColor: String? = null,
    graphicsLayerBlock: GraphicsLayerScope.() -> Unit = {},
) {
    CoilImage(
        data = imageUrl,
        contentDescription = contentDescription,
        fadeIn = true,
        modifier = Modifier
            .height(height)
            .fillMaxWidth()
            .run {
                backgroundColor?.let {
                    background(
                        color = it.toColor(),
                        shape = shape
                    )
                } ?: this
            }
            .graphicsLayer(graphicsLayerBlock)
    )
}