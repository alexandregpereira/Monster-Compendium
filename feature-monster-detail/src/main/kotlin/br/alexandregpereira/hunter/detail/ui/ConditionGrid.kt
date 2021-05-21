package br.alexandregpereira.hunter.detail.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import br.alexandregpereira.hunter.detail.R
import br.alexandregpereira.hunter.domain.model.Condition
import br.alexandregpereira.hunter.domain.model.ConditionType

@Composable
fun ConditionGrid(
    conditions: List<Condition>
) = Grid {

    conditions.forEach { condition ->
        val iconRes = condition.type.getIconRes()
        IconInfo(
            title = condition.name,
            painter = painterResource(iconRes)
        )
    }
}

private fun ConditionType.getIconRes(): Int {
    return when (this) {
        ConditionType.BLINDED -> R.drawable.ic_blinded
        ConditionType.CHARMED -> R.drawable.ic_charmed
        ConditionType.DEAFENED -> R.drawable.ic_deafened
        ConditionType.EXHAUSTION -> R.drawable.ic_exhausted
        ConditionType.FRIGHTENED -> R.drawable.ic_frightened
        ConditionType.GRAPPLED -> R.drawable.ic_grappled
        ConditionType.PARALYZED -> R.drawable.ic_paralyzed
        ConditionType.PETRIFIED -> R.drawable.ic_petrified
        ConditionType.POISONED -> R.drawable.ic_poison
        ConditionType.PRONE -> R.drawable.ic_prone
        ConditionType.RESTRAINED -> R.drawable.ic_restrained
        ConditionType.STUNNED -> R.drawable.ic_stuned
        ConditionType.UNCONSCIOUS -> R.drawable.ic_unconscious
    }
}
