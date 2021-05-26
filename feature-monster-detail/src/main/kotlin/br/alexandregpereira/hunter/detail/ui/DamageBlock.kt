package br.alexandregpereira.hunter.detail.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.detail.R
import br.alexandregpereira.hunter.domain.model.Damage
import br.alexandregpereira.hunter.domain.model.DamageType
import br.alexandregpereira.hunter.ui.util.toColor

@Composable
private fun DamageBlock(
    damages: List<Damage>,
    title: String,
    modifier: Modifier = Modifier
) = Block(title = title, modifier = modifier) {

    DamageGrid(damages)
    damages.filter { it.type == DamageType.OTHER }.forEach { damage ->
        Text(
            text = damage.name,
            fontWeight = FontWeight.Light,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 24.dp)
        )
    }
}

@Composable
fun DamageVulnerabilitiesBlock(
    damages: List<Damage>,
    modifier: Modifier = Modifier
) = DamageBlock(damages, title = stringResource(R.string.monster_detail_vulnerabilities), modifier)

@Composable
fun DamageResistancesBlock(
    damages: List<Damage>,
    modifier: Modifier = Modifier
) = DamageBlock(damages, title = stringResource(R.string.monster_detail_resistances), modifier)

@Composable
fun DamageImmunitiesBlock(
    damages: List<Damage>,
    modifier: Modifier = Modifier
) = DamageBlock(damages, title = stringResource(R.string.monster_detail_immunities), modifier)

@Composable
fun DamageGrid(
    damages: List<Damage>
) = Grid {

    damages.forEach { damage ->
        val iconRes = damage.type.getIconRes()
        if (iconRes != null) {
            IconInfo(
                title = damage.name,
                painter = painterResource(iconRes),
                iconColor = damage.type.getIconColor(),
                iconAlpha = 1f
            )
        }
    }
}

private fun DamageType.getIconRes(): Int? {
    return when (this) {
        DamageType.ACID -> R.drawable.ic_acid
        DamageType.BLUDGEONING -> R.drawable.ic_bludgeoning
        DamageType.COLD -> R.drawable.ic_cold
        DamageType.FIRE -> R.drawable.ic_elemental
        DamageType.LIGHTNING -> R.drawable.ic_lightning
        DamageType.NECROTIC -> R.drawable.ic_undead
        DamageType.PIERCING -> R.drawable.ic_piercing
        DamageType.POISON -> R.drawable.ic_poison
        DamageType.PSYCHIC -> R.drawable.ic_psychic
        DamageType.RADIANT -> R.drawable.ic_radiant
        DamageType.SLASHING -> R.drawable.ic_slashing
        DamageType.THUNDER -> R.drawable.ic_thunder
        DamageType.OTHER -> null
    }
}

@Composable
private fun DamageType.getIconColor(): Color {
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
}
