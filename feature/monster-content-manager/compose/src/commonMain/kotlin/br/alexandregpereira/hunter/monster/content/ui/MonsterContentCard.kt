package br.alexandregpereira.hunter.monster.content.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.monster.content.MonsterContentManagerEmptyStrings
import br.alexandregpereira.hunter.monster.content.MonsterContentManagerStrings
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.AppButtonSize
import br.alexandregpereira.hunter.ui.compose.AppCard
import br.alexandregpereira.hunter.ui.compose.AppSurface
import br.alexandregpereira.hunter.ui.compose.CoilImage
import br.alexandregpereira.hunter.ui.compose.SectionTitle
import br.alexandregpereira.hunter.ui.compose.cardShape
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import br.alexandregpereira.hunter.ui.theme.Shapes
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun MonsterContentCard(
    name: String,
    originalName: String?,
    totalMonsters: Int,
    summary: String,
    coverImageUrl: String,
    isEnabled: Boolean,
    strings: MonsterContentManagerStrings,
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit = {},
    onRemoveClick: () -> Unit = {},
    onPreviewClick: () -> Unit = {},
) = AppCard(modifier = modifier, shape = cardShape) {
    Column(Modifier.padding(16.dp)) {
        Title(
            name = name,
            originalName = originalName,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Cover(
            coverImageUrl = coverImageUrl,
            name = name,
            totalMonsters = strings.totalMonsters(totalMonsters),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Summary(summary = summary)

        Spacer(Modifier.height(8.dp))

        Buttons(
            isEnabled = isEnabled,
            removeText = strings.remove,
            addText = strings.add,
            previewLabel = strings.preview,
            onAddClick = onAddClick,
            onRemoveClick = onRemoveClick,
            onPreviewClick = onPreviewClick,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}

@Composable
private fun Cover(
    coverImageUrl: String,
    name: String,
    totalMonsters: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CoilImage(
            imageUrl = coverImageUrl,
            contentDescription = name,
            shape = Shapes.large,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .background(color = MaterialTheme.colors.surface, shape = Shapes.large)
                .height(IMAGE_HEIGHT.dp)
                .width(172.dp)
                .align(alignment = Alignment.CenterHorizontally)
        )

        Text(
            text = totalMonsters,
            fontWeight = FontWeight.Light,
            fontSize = 14.sp,
            modifier = Modifier
                .width(156.dp)
                .align(alignment = Alignment.CenterHorizontally)
                .padding(top = 4.dp)
        )
    }
}

@Composable
private fun Title(
    name: String,
    originalName: String?,
    modifier: Modifier = Modifier
) = Column(
    modifier = modifier
) {
    SectionTitle(
        title = name,
        isHeader = false,
        modifier = Modifier
    )
    originalName?.let {
        Text(
            text = it,
            fontWeight = FontWeight.Light,
            fontSize = 14.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier
        )
    }
}

@Composable
private fun Summary(
    summary: String,
    modifier: Modifier = Modifier
) = Text(
    text = summary,
    fontWeight = FontWeight.Light,
    fontSize = 16.sp,
    modifier = modifier,
)

@Composable
private fun Buttons(
    isEnabled: Boolean,
    removeText: String,
    addText: String,
    previewLabel: String,
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit = {},
    onRemoveClick: () -> Unit = {},
    onPreviewClick: () -> Unit = {},
) {
    val text = if (isEnabled) {
        removeText
    } else addText

    val click = if (isEnabled) onRemoveClick else onAddClick

    Row(modifier) {
        AppButton(
            text = text,
            onClick = click,
            size = AppButtonSize.SMALL,
            modifier = Modifier
                .padding(end = 8.dp)
                .weight(1f),
        )

        AppButton(
            text = previewLabel,
            onClick = onPreviewClick,
            size = AppButtonSize.SMALL,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f),
        )
    }
}

@Preview
@Composable
private fun MonsterContentCardPreview() = HunterTheme {
    AppSurface {
        MonsterContentCard(
            name = "Name",
            originalName = "Other name",
            totalMonsters = 10,
            summary = "A menagerie of deadly monsters for the world's greatest roleplaying game. The Monster Manual presents a horde of classic Dungeons & Dragons creatures, including dragons, giants, mind flayers, and beholders—a monstrous feast for Dungeon Masters ready to challenge their players and populate their adventures.",
            coverImageUrl = "",
            isEnabled = true,
            strings = MonsterContentManagerEmptyStrings(),
        )
    }
}

private const val IMAGE_HEIGHT = 220
