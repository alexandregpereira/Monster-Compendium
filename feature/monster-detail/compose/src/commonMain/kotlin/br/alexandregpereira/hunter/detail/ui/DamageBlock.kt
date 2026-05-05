/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.detail.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.domain.model.DamageType
import br.alexandregpereira.hunter.monster.detail.DamageState
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import br.alexandregpereira.hunter.ui.util.toColor
import org.jetbrains.compose.resources.painterResource

@Composable
private fun DamageBlock(
    damages: List<DamageState>,
    title: String,
    modifier: Modifier = Modifier
) = Block(title = title, modifier = modifier) {

    DamageGrid(damages)
    damages.filter { it.type == DamageType.OTHER }.forEach { damage ->
        Text(
            text = damage.name,
            fontWeight = FontWeight.Light,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 24.dp).padding(horizontal = 16.dp),
        )
    }
}

@Composable
internal fun DamageVulnerabilitiesBlock(
    damages: List<DamageState>,
    modifier: Modifier = Modifier
) = DamageBlock(damages, title = strings.vulnerabilities, modifier)

@Composable
internal fun DamageResistancesBlock(
    damages: List<DamageState>,
    modifier: Modifier = Modifier
) = DamageBlock(damages, title = strings.resistances, modifier)

@Composable
internal fun DamageImmunitiesBlock(
    damages: List<DamageState>,
    modifier: Modifier = Modifier
) = DamageBlock(damages, title = strings.immunities, modifier)

@Composable
internal fun DamageGrid(
    damages: List<DamageState>
) = Grid {

    damages.forEach { damage ->
        val iconRes = damage.type.toIcon()
        if (iconRes != null) {
            IconInfo(
                title = damage.name,
                painter = painterResource(iconRes),
                iconColor = damage.type.getIconColor(),
                iconAlpha = 1f,
                iconSize = 48.dp,
                modifier = Modifier.width(GridItemWidth).padding(horizontal = 8.dp),
            )
        }
    }
}

@Composable
internal fun DamageType.getIconColor(): Color {
    val colors = DamageIconColors(isSystemInDarkTheme())
    return when (this) {
        DamageType.OTHER -> LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
        DamageType.SLASHING -> colors.bludgeoning
        DamageType.PIERCING -> colors.piercing
        DamageType.BLUDGEONING -> colors.slashing
        DamageType.ACID -> colors.acid
        DamageType.COLD -> colors.cold
        DamageType.FIRE -> colors.fire
        DamageType.LIGHTNING -> colors.lightning
        DamageType.NECROTIC -> colors.necrotic
        DamageType.POISON -> colors.poison
        DamageType.PSYCHIC -> colors.psychic
        DamageType.RADIANT -> colors.radiant
        DamageType.THUNDER -> colors.thunder
        DamageType.FORCE -> colors.force
    }
}

private class DamageIconColors(isDark: Boolean) {
    val acid = (if (isDark) "#BCFC7D" else "#5E8636").toColor()
    val cold = (if (isDark) "#95F2F8" else "#00B7C2").toColor()
    val fire = (if (isDark) "#FF6060" else "#E90000").toColor()
    val lightning = (if (isDark) "#FFEB81" else "#FFD600").toColor()
    val necrotic = (if (isDark) "#D8D8D8" else "#000000").toColor()
    val poison = (if (isDark) "#DC8BF9" else "#6D3780").toColor()
    val psychic = (if (isDark) "#84DEC9" else "#00664E").toColor()
    val radiant = (if (isDark) "#F2A762" else "#FF7A00").toColor()
    val thunder = (if (isDark) "#A1BBE1" else "#6D6D6D").toColor()
    val bludgeoning = (if (isDark) "#E1A8A8" else "#613A3A").toColor()
    val piercing = (if (isDark) "#BAAFE2" else "#464058").toColor()
    val slashing = (if (isDark) "#9ECAEA" else "#364450").toColor()
    val force = (if (isDark) "#5E6AD4" else "#001AFF").toColor()
}

@Preview(showBackground = true)
@Composable
private fun DamageBlockPreview() = HunterTheme {
    val damages = listOf(
        DamageState(
            index = "acid",
            type = DamageType.ACID,
            name = "Acid"
        ),
        DamageState(
            index = "acid",
            type = DamageType.ACID,
            name = "Acid"
        ),
        DamageState(
            index = "acid",
            type = DamageType.BLUDGEONING,
            name = "Bludgeoning*"
        ),
    )
    DamageBlock(
        damages = damages,
        title = "Vulnerabilities"
    )
}
