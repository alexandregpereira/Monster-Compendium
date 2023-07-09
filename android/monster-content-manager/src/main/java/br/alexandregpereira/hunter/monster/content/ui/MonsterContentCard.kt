package br.alexandregpereira.hunter.monster.content.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.monster.content.R
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.AppButtonSize
import br.alexandregpereira.hunter.ui.compose.CoilImage
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
) {
    Row {
        Column(
            Modifier
                .weight(1f)
                .padding(end = 16.dp)
        ) {
            CoilImage(
                imageUrl = coverImageUrl,
                contentDescription = name,
                shape = Shapes.large,
                modifier = Modifier
                    .height(204.dp)
                    .width(156.dp)
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(top = 4.dp)
            )

            Text(
                text = stringResource(
                    id = R.string.monster_content_manager_manage_total_monsters,
                    formatArgs = arrayOf(totalMonsters)
                ),
                fontWeight = FontWeight.Light,
                fontSize = 14.sp,
                modifier = Modifier
                    .width(156.dp)
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(top = 4.dp)
            )

            val textId = if (isEnabled) {
                R.string.monster_content_manager_manage_remove
            } else R.string.monster_content_manager_manage_add

            val click = if (isEnabled) onRemoveClick else onAddClick

            AppButton(
                text = stringResource(textId),
                onClick = click,
                size = AppButtonSize.SMALL,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
        Column(
            Modifier
                .weight(1f)
                .padding(start = 4.dp)
        ) {
            Text(
                text = name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
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

            Text(
                text = summary,
                fontWeight = FontWeight.Light,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
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