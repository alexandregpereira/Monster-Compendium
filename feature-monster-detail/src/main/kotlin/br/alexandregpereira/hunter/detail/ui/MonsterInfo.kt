package br.alexandregpereira.hunter.detail.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.model.Speed
import br.alexandregpereira.hunter.domain.model.Stats
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
fun MonsterInfo(monster: Monster) = Column(
    Modifier
        .fillMaxWidth()
        .clip(
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        )
) {
    MonsterTitle(title = monster.name, subTitle = monster.subtitle)
    Spacer(modifier = Modifier.padding(top = 1.dp))
    StatsBlock(stats = monster.stats)
}

@Preview
@Composable
fun MonsterInfoPreview() {
    HunterTheme {
        MonsterInfo(
            Monster(
                index = "sda",
                type = MonsterType.ABERRATION,
                subtype = null,
                group = null,
                challengeRating = 0.0f,
                name = "Teste dos tes",
                subtitle = "asdasd asdasdas asdasdasd",
                imageData = MonsterImageData(
                    url = "",
                    backgroundColor = Color(light = "", dark = ""),
                    isHorizontal = false
                ),
                size = "",
                alignment = "",
                stats = Stats(armorClass = 0, hitPoints = 0, hitDice = ""),
                speed = Speed(hover = false, values = listOf()),
                abilityScores = listOf(),
                savingThrows = listOf(),
                skills = listOf(),
                damageVulnerabilities = listOf(),
                damageResistances = listOf(),
                damageImmunities = listOf()
            )
        )
    }
}