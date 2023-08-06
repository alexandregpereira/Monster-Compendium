package br.alexandregpereira.hunter.monster.content.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.monster.content.R
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.AppButtonSize
import br.alexandregpereira.hunter.ui.compose.CoilImage
import br.alexandregpereira.hunter.ui.compose.SectionTitle
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import br.alexandregpereira.hunter.ui.theme.Shapes

@Composable
internal fun MonsterContentCard(
    name: String,
    originalName: String?,
    totalMonsters: Int,
    summary: String,
    coverImageUrl: String,
    isEnabled: Boolean,
    onAddClick: () -> Unit = {},
    onRemoveClick: () -> Unit = {},
    onPreviewClick: () -> Unit = {},
) = Column {
    Title(
        name = name,
        originalName = originalName,
        modifier = Modifier.padding(bottom = 16.dp)
    )
    Row {
        Cover(
            coverImageUrl = coverImageUrl,
            name = name,
            totalMonsters = totalMonsters,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )
        Summary(
            summary = summary,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )
    }

    Buttons(
        isEnabled = isEnabled,
        onAddClick = onAddClick,
        onRemoveClick = onRemoveClick,
        onPreviewClick = onPreviewClick,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
private fun Cover(
    coverImageUrl: String,
    name: String,
    totalMonsters: Int,
    modifier: Modifier
) {
    Column(
        modifier = modifier
    ) {
        CoilImage(
            imageUrl = coverImageUrl,
            contentDescription = name,
            shape = Shapes.large,
            modifier = Modifier
                .height(IMAGE_HEIGHT.dp)
                .width(172.dp)
                .align(alignment = Alignment.CenterHorizontally)
        )

        Text(
            text = stringResource(
                id = R.string.monster_content_manager_total_monsters,
                formatArgs = arrayOf(totalMonsters)
            ),
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
    fontSize = 14.sp,
    overflow = Ellipsis,
    modifier = modifier.sizeIn(maxHeight = IMAGE_HEIGHT.dp)
)

@Composable
private fun Buttons(
    isEnabled: Boolean,
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit = {},
    onRemoveClick: () -> Unit = {},
    onPreviewClick: () -> Unit = {},
) {
    val textId = if (isEnabled) {
        R.string.monster_content_manager_remove
    } else R.string.monster_content_manager_add

    val click = if (isEnabled) onRemoveClick else onAddClick

    Row(modifier) {
        AppButton(
            text = stringResource(textId),
            onClick = click,
            size = AppButtonSize.SMALL,
            modifier = Modifier
                .padding(end = 8.dp)
                .weight(1f),
        )

        AppButton(
            text = stringResource(R.string.monster_content_manager_preview),
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
    Surface {
        MonsterContentCard(
            name = "Name",
            originalName = "Other name",
            totalMonsters = 10,
            summary = "A menagerie of deadly monsters for the world's greatest roleplaying game. The Monster Manual presents a horde of classic Dungeons & Dragons creatures, including dragons, giants, mind flayers, and beholders—a monstrous feast for Dungeon Masters ready to challenge their players and populate their adventures.",
            coverImageUrl = "",
            isEnabled = true,
        )
    }
}

private const val IMAGE_HEIGHT = 220
