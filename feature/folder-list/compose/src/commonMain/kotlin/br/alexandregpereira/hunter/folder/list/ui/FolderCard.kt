/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.folder.list.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.folder.list.FolderCardImageState
import br.alexandregpereira.hunter.ui.compose.ImageCard
import br.alexandregpereira.hunter.ui.compose.MonsterCoilImage
import br.alexandregpereira.hunter.ui.compose.Window
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun FolderCard(
    folderName: String,
    image1: FolderCardImageState,
    modifier: Modifier = Modifier,
    image2: FolderCardImageState? = null,
    image3: FolderCardImageState? = null,
    onCLick: () -> Unit = {},
    onLongCLick: () -> Unit = {},
) = ImageCard(
    name = folderName,
    isHorizontal = true,
    fontSize = 24.sp,
    modifier = modifier,
    onCLick = onCLick,
    onLongCLick = onLongCLick,
) {
    FolderCardImageLayout(
        isHorizontalImage = image1.isHorizontalImage
    ) {
        FolderCardImage(
            image1 = image1,
            image2 = image2,
            image3 = image3
        )
    }
}

@Composable
private fun FolderCardImage(
    image1: FolderCardImageState,
    image2: FolderCardImageState? = null,
    image3: FolderCardImageState? = null,
) {
    image1.run {
        val backgroundColor = if (isSystemInDarkTheme()) {
            backgroundColorDark
        } else backgroundColorLight

        MonsterCoilImage(
            imageUrl = url,
            contentDescription = contentDescription,
            backgroundColor = backgroundColor,
            contentScale = ContentScale.Crop,
            shape = RectangleShape,
        )
    }

    image2?.run {
        val backgroundColor = if (isSystemInDarkTheme()) {
            backgroundColorDark
        } else backgroundColorLight

        MonsterCoilImage(
            imageUrl = url,
            contentDescription = contentDescription,
            backgroundColor = backgroundColor,
            contentScale = ContentScale.Crop,
            shape = RectangleShape,
        )
    }

    image3?.run {
        val backgroundColor = if (isSystemInDarkTheme()) {
            backgroundColorDark
        } else backgroundColorLight

        MonsterCoilImage(
            imageUrl = url,
            contentDescription = contentDescription,
            backgroundColor = backgroundColor,
            contentScale = ContentScale.Crop,
            shape = RectangleShape,
        )
    }
}

@Composable
private fun FolderCardImageLayout(
    isHorizontalImage: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) = Layout(
    modifier = modifier.fillMaxSize(),
    content = content
) { measurables, constraints ->
    val firstImagePadding: Int
    val firstImageWidth: Int
    val firstImageHeight: Int
    if (measurables.size == 1) {
        firstImagePadding = 0
        firstImageWidth = constraints.maxWidth
        firstImageHeight = constraints.maxHeight
    } else {
        firstImagePadding = -24.dp.roundToPx()
        if (isHorizontalImage) {
            firstImageWidth = constraints.maxWidth - (firstImagePadding * 2)
            firstImageHeight = (constraints.maxHeight * 0.7).toInt() - firstImagePadding
        } else {
            firstImageWidth = (constraints.maxWidth * 0.7).toInt() - firstImagePadding
            firstImageHeight = constraints.maxHeight - (firstImagePadding * 2)
        }
    }

    val firstPlaceable = measurables.first().measure(
        Constraints.fixed(
            width = firstImageWidth,
            height = firstImageHeight
        )
    )
    val placeables = measurables.toMutableList()
        .also { it.removeAt(0) }
        .map { measurable ->
            if (measurables.size == 2) {
                measurable.measure(
                    Constraints.fixed(
                        width = if (isHorizontalImage) {
                            constraints.maxWidth
                        } else firstImageWidth,
                        height = if (isHorizontalImage) {
                            firstImageHeight
                        } else constraints.maxHeight
                    )
                )
            } else {
                measurable.measure(
                    Constraints.fixed(
                        width = (constraints.maxWidth * 0.7).toInt(),
                        height = (constraints.maxHeight * 0.7).toInt()
                    )
                )
            }
        }

    placeImages(
        constraints = constraints,
        firstPlaceable = firstPlaceable,
        secondPlaceable = placeables.getOrNull(0),
        thirdPlaceable = placeables.getOrNull(1),
        isHorizontalImage = isHorizontalImage,
        firstImagePadding = firstImagePadding
    )
}

private fun MeasureScope.placeImages(
    constraints: Constraints,
    firstPlaceable: Placeable,
    secondPlaceable: Placeable?,
    thirdPlaceable: Placeable?,
    isHorizontalImage: Boolean,
    firstImagePadding: Int
): MeasureResult = layout(constraints.maxWidth, constraints.maxHeight) {
    val x = if (isHorizontalImage) 0 else firstPlaceable.width + firstImagePadding
    val y = if (isHorizontalImage) firstPlaceable.height + firstImagePadding else 0

    secondPlaceable?.let { secondPlaceable ->
        thirdPlaceable?.let { thirdPlaceable ->
            val thirdPlaceableX = if (isHorizontalImage) {
                secondPlaceable.width
            } else x

            val thirdPlaceableY = if (isHorizontalImage.not()) {
                y + secondPlaceable.height
            } else y

            thirdPlaceable.placeRelative(
                x = thirdPlaceableX - (thirdPlaceable.width * 0.3).toInt() ,
                y = thirdPlaceableY
            )
        }
        secondPlaceable.placeRelative(x = x, y = y)
    }

    firstPlaceable.placeRelative(x = firstImagePadding, y = firstImagePadding)
}

@Preview
@Composable
private fun FolderCardPreview() = Window(Modifier.width(240.dp)) {
    Column {
        FolderCard(
            folderName = "Folder 1",
            image1 = FolderCardImageState(
                isHorizontalImage = true,
                backgroundColorLight = "#e2e2e2"
            ),
            image2 = FolderCardImageState(
                backgroundColorLight = "#a2a2a2"
            ),
            image3 = FolderCardImageState(
                backgroundColorLight = "#b2b2b2"
            ),
        )

        FolderCard(
            folderName = "Folder 2",
            image1 = FolderCardImageState(
                isHorizontalImage = false,
                backgroundColorLight = "#e2e2e2"
            ),
            image2 = FolderCardImageState(
                backgroundColorLight = "#a2a2a2"
            ),
            image3 = FolderCardImageState(
                backgroundColorLight = "#b2b2b2"
            ),
            modifier = Modifier.padding(top = 24.dp)
        )

        FolderCard(
            folderName = "Folder 3",
            image1 = FolderCardImageState(
                isHorizontalImage = true,
                backgroundColorLight = "#e2e2e2"
            ),
            image2 = FolderCardImageState(
                backgroundColorLight = "#a2a2a2"
            ),
            modifier = Modifier.padding(top = 24.dp)
        )

        FolderCard(
            folderName = "Folder 4",
            image1 = FolderCardImageState(
                isHorizontalImage = false,
                backgroundColorLight = "#e2e2e2"
            ),
            image2 = FolderCardImageState(
                backgroundColorLight = "#a2a2a2"
            ),
            modifier = Modifier.padding(top = 24.dp)
        )
    }
}
