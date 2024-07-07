/*
 * Copyright 2023 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.sync.di

import androidx.lifecycle.SavedStateHandle
import br.alexandregpereira.hunter.sync.SyncState
import br.alexandregpereira.hunter.sync.SyncStateRecovery
import br.alexandregpereira.hunter.sync.SyncViewModel
import br.alexandregpereira.hunter.sync.getState
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val syncModule = module {
    factory {
        val savedStateHandle = get<SavedStateHandle>()
        SyncStateRecovery {
            val state = savedStateHandle.getState()
            SyncState(
                isOpen = state.isOpen,
            )
        }
    }

    viewModel {
        SyncViewModel(get(), get())
    }
}
