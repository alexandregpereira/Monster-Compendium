package br.alexandregpereira.hunter.detail.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.detail.R
import br.alexandregpereira.hunter.domain.model.Damage
import br.alexandregpereira.hunter.domain.model.DamageType

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
