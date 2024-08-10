package br.alexandregpereira.hunter.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CornerCircle(
    color: Color,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    direction: Direction = Direction.LEFT,
    content: @Composable () -> Unit = {}
) {
    val shape = if (direction == Direction.LEFT) {
        RoundedCornerShape(bottomEnd = 48.dp)
    } else {
        RoundedCornerShape(bottomStart = 48.dp)
    }
    val brush = object : ShaderBrush() {
        override fun createShader(size: Size): Shader {
            val center = if (direction == Direction.LEFT) {
                Offset.Zero
            } else {
                Offset(size.width, 0f)
            }
            val biggerDimension = maxOf(size.height, size.width)
            return RadialGradientShader(
                colors = listOf(
                    color.copy(alpha = .5f),
                    Color.Transparent,
                ),
                center = center,
                radius = biggerDimension,
                colorStops = listOf(0.5f, 1f)
            )
        }
    }
    Box(
        modifier
            .size(size)
            .background(brush = brush, shape = shape)
            .clip(shape),
    ) {
        val contentAlignment = if (direction == Direction.LEFT) {
            TopStart
        } else {
            TopEnd
        }
        Box(Modifier.fillMaxSize().padding(12.dp), contentAlignment = contentAlignment) {
            content()
        }
    }
}

private fun DrawScope.drawLeftCornerCircle(color: Color, contentTopPadding: Dp) {
    val width = size.width - 8.dp.toPx()
    val height = size.height - 8.dp.toPx()
    val topPadding = contentTopPadding.toPx()
    drawPath(
        path = Path().apply {
            lineTo(0f, 0f)
            lineTo(width, 0f)
            lineTo(width, topPadding)
            cubicTo(
                x1 = width, y1 = height * 0.7f,
                x2 = width * 0.7f, y2 = height,
                x3 = 0f, y3 = height
            )
            close()
        },
        color = color
    )
}

private fun DrawScope.drawRightCornerCircle(color: Color, contentTopPadding: Dp) {
    val start = 8.dp.toPx()
    val width = size.width
    val height = size.height - 8.dp.toPx()
    val topPadding = contentTopPadding.toPx()
    drawPath(
        path = Path().apply {
            lineTo(width, 0f)
            lineTo(start, 0f)
            lineTo(start, topPadding)
//            lineTo(start, height)
//            lineTo(width, height)

            cubicTo(
                x1 = start, y2 = height * 0.7f,
                x2 = start + (width + .7f), y1 = height,
                x3 = width, y3 = height
            )
            lineTo(width, 0f)
            close()
        },
        color = color
    )
}

enum class Direction {
    LEFT, RIGHT
}
