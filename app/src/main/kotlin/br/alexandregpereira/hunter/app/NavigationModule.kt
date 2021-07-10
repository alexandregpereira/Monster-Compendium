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

import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import br.alexandregpereira.hunter.domain.Navigator
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object NavControllerModule {

    @Provides
    fun provideNavHostController(activity: FragmentActivity): NavController {
        val fragment = activity.supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        return fragment?.findNavController()
            ?: throw IllegalAccessError("NavHostController not founded")
    }
}

@Module
@InstallIn(ActivityComponent::class)
internal abstract class NavigationModule {

    @Binds
    abstract fun bindNavigator(navigatorImpl: NavigatorImpl): Navigator
}