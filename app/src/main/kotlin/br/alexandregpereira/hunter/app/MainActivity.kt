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

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import br.alexandregpereira.hunter.app.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val onDestinationChanged = NavController.OnDestinationChangedListener { _, destination, _ ->
        val value = if (destination.id != R.id.monsterDetailFragment) {
            binding.bottomNavigation.isVisible = true
            0
        } else {
            // Workaround when the app is killed in the background
            if (binding.bottomNavigation.height == 0) binding.bottomNavigation.isVisible = false

            binding.bottomNavigation.height
        }
        binding.bottomNavigation.animate()
            .setDuration(150L)
            .setInterpolator(FastOutLinearInInterpolator())
            .translationY(value.toFloat())
    }

    private val navController by lazy {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.navHostFragment.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }

        binding.bottomNavigation.setupWithNavController(navController)
    }

    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(onDestinationChanged)
    }

    override fun onPause() {
        navController.removeOnDestinationChangedListener(onDestinationChanged)
        super.onPause()
    }
}
