/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.app

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import br.alexandregpereira.hunter.domain.Navigator
import javax.inject.Inject

class NavigatorImpl @Inject constructor(
    private val navController: NavController
): Navigator {

    override fun navigateToDetail(index: String) {
        navController.navigate(
            R.id.action_monsterCompendiumFragment_to_monsterDetailFragment,
            bundleOf("index" to index)
        )
    }
}
