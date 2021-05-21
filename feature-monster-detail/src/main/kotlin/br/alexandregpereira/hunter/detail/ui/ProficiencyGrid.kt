package br.alexandregpereira.hunter.detail.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import br.alexandregpereira.hunter.detail.R
import br.alexandregpereira.hunter.domain.model.MeasurementUnit
import br.alexandregpereira.hunter.domain.model.Proficiency
import br.alexandregpereira.hunter.domain.model.Speed
import br.alexandregpereira.hunter.domain.model.SpeedType
import br.alexandregpereira.hunter.domain.model.SpeedValue
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
fun ProficiencyGrid(
    proficiencies: List<Proficiency>,
) = Grid {

    proficiencies.forEach { proficiency ->
        Bonus(value = proficiency.modifier, name = proficiency.name)
    }
}

