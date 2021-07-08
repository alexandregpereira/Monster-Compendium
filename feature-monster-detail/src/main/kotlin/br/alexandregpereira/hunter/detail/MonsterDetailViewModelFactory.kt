package br.alexandregpereira.hunter.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.AssistedFactory

@AssistedFactory
internal interface MonsterDetailViewModelFactory {
    fun create(monsterIndex: String): MonsterDetailViewModel

    companion object {
        fun provideFactory(
            assistedFactory: MonsterDetailViewModelFactory,
            monsterIndex: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(monsterIndex) as T
            }
        }
    }
}
