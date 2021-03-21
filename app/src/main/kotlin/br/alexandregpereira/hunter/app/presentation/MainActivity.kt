/*
 * Copyright (c) 2021 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.app.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import br.alexandregpereira.hunter.app.ui.theme.Theme
import br.alexandregpereira.hunter.monster.compendium.MonsterCompendiumViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalAnimationApi
class MainActivity : AppCompatActivity() {

    private val viewModel: MonsterCompendiumViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Window(viewModel)
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun Window(viewModel: MonsterCompendiumViewModel) = Theme {
    val state = viewModel.stateLiveData.observeAsState().value ?: return@Theme
    Surface {
        Text(text = state.monsters.map { it.name }.reduce { acc, monster -> "$acc\n${monster}" })
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun DefaultPreview() {
}