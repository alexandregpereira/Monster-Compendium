package br.alexandregpereira.hunter.detail.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.util.lerp
import br.alexandregpereira.hunter.ui.compose.MonsterCoilImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import kotlin.math.absoluteValue

data class Image(val url: String, val contentDescription: String)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MonsterImages(
    images: List<Image>,
    pagerState: PagerState,
    height: Dp,
    shape: Shape,
) = HorizontalPager(
    state = pagerState,
    verticalAlignment = Alignment.Top
) { pagePosition ->
    val image = images[pagePosition]
    MonsterCoilImage(
        imageUrl = image.url,
        contentDescription = image.contentDescription,
        height = height,
        shape = shape,
        graphicsLayerBlock = {
            val fraction =
                1f - calculateCurrentOffsetForPage(pagePosition).absoluteValue.coerceIn(0f, 1f)

            lerp(
                start = 0.4f,
                stop = 1f,
                fraction = fraction
            ).also { scale ->
                scaleX = scale
                scaleY = scale
            }

            alpha = lerp(
                start = 0.2f,
                stop = 1f,
                fraction = fraction
            )
        }
    )
}