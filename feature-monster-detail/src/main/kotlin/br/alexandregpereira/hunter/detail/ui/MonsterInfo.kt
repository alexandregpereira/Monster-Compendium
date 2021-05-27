package br.alexandregpereira.hunter.detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.detail.R
import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.MeasurementUnit
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.model.Speed
import br.alexandregpereira.hunter.domain.model.SpeedType
import br.alexandregpereira.hunter.domain.model.SpeedValue
import br.alexandregpereira.hunter.domain.model.Stats
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
fun MonsterInfo(
    monster: Monster,
    modifier: Modifier = Modifier,
    alpha: Float = 1f,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) = Column(
    modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
        .background(
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            color = MaterialTheme.colors.surface
        )
        .alpha(alpha)
) {
    MonsterTitle(title = monster.name, subTitle = monster.subtitle)

    BlockSection { StatsBlock(stats = monster.stats) }
    BlockSection { SpeedBlock(speed = monster.speed) }
    BlockSection { AbilityScoreBlock(abilityScores = monster.abilityScores) }
    OptionalBlockSection(monster.savingThrows) {
        ProficiencyBlock(
            proficiencies = it,
            title = stringResource(R.string.monster_detail_saving_throws)
        )
    }
    OptionalBlockSection(monster.skills) {
        ProficiencyBlock(
            proficiencies = it,
            title = stringResource(R.string.monster_detail_skills)
        )
    }
    OptionalBlockSection(monster.damageVulnerabilities) {
        DamageVulnerabilitiesBlock(damages = it)
    }
    OptionalBlockSection(monster.damageResistances) {
        DamageResistancesBlock(damages = it)
    }
    OptionalBlockSection(monster.damageImmunities) {
        DamageImmunitiesBlock(damages = it)
    }
    OptionalBlockSection(monster.conditionImmunities) {
        ConditionBlock(conditions = it)
    }
    BlockSection { SensesBlock(senses = monster.senses) }
    BlockSection {
        TextBlock(
            title = stringResource(R.string.monster_detail_languages),
            text = monster.languages
        )
    }
    BlockSection { SpecialAbilityBlock(specialAbilities = monster.specialAbilities) }
    BlockSection { ActionBlock(actions = monster.actions) }

    Spacer(
        modifier = Modifier
            .height(contentPadding.calculateBottomPadding())
            .fillMaxWidth()
    )
}

@Composable
private fun <T> ColumnScope.OptionalBlockSection(
    value: List<T>,
    content: @Composable ColumnScope.(List<T>) -> Unit
) {
    if (value.isEmpty()) return
    BlockSection {
        content(value)
    }
}

@Composable
private fun ColumnScope.BlockSection(
    content: @Composable ColumnScope.() -> Unit
) {
    Spacer(
        modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
    )

    content()
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
                speed = Speed(
                    hover = true, values = (0..6).map {
                        SpeedValue(
                            type = SpeedType.WALK,
                            measurementUnit = MeasurementUnit.METER,
                            value = 0,
                            valueFormatted = "10m"
                        )
                    }
                ),
                abilityScores = listOf(),
                savingThrows = listOf(),
                skills = listOf(),
                damageVulnerabilities = listOf(),
                damageResistances = listOf(),
                damageImmunities = listOf(),
                conditionImmunities = listOf(),
                senses = listOf(),
                languages = "",
                specialAbilities = listOf(),
                actions = listOf()
            )
        )
    }
}