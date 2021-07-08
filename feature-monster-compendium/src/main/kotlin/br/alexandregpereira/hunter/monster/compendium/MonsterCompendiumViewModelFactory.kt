package br.alexandregpereira.hunter.monster.compendium

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.AssistedFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@AssistedFactory
internal interface MonsterCompendiumViewModelFactory {
    fun create(dispatcher: CoroutineDispatcher, loadOnInit: Boolean): MonsterCompendiumViewModel
    
    companion object {
        fun provideFactory(
            assistedFactory: MonsterCompendiumViewModelFactory
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(Dispatchers.IO, loadOnInit = true) as T
            }
        }
    }
}
