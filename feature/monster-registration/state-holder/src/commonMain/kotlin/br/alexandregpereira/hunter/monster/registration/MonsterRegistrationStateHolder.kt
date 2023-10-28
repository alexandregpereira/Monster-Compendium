package br.alexandregpereira.hunter.monster.registration

import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.usecase.GetMonsterUseCase
import br.alexandregpereira.hunter.event.EventManager
import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationEvent
import br.alexandregpereira.hunter.state.MutableActionHandler
import br.alexandregpereira.hunter.state.MutableStateHolder
import br.alexandregpereira.hunter.state.ScopeManager
import br.alexandregpereira.hunter.state.StateHolder
import br.alexandregpereira.hunter.state.StateHolderParams
import br.alexandregpereira.hunter.state.UiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MonsterRegistrationStateHolder internal constructor(
    private val dispatcher: CoroutineDispatcher,
    private val params: StateHolderParams<MonsterRegistrationParams>,
    private val eventManager: EventManager<MonsterRegistrationEvent>,
    private val getMonster: GetMonsterUseCase,
) : UiModel<MonsterRegistrationState>(MonsterRegistrationState()),
    MutableActionHandler<MonsterRegistrationAction> by MutableActionHandler(),
    MonsterRegistrationIntent {

    init {
        observeEvents()
        if (state.value.isOpen) {
            loadMonster()
        }
    }

    override fun onClose() {
        eventManager.dispatchEvent(MonsterRegistrationEvent.Hide)
    }

    override fun onMonsterChanged(monster: Monster) {
        setState { copy(monster = monster) }
    }

    private fun loadMonster() {
        val monsterIndex = params.value.monsterIndex?.takeUnless { it.isBlank() } ?: return

        setState { copy(isLoading = true) }
        getMonster(monsterIndex)
            .flowOn(dispatcher)
            .onEach { monster ->
                setState { copy(monster = monster, isLoading = false) }
            }
            .launchIn(scope)
    }

    private fun observeEvents() {
        eventManager.events
            .flowOn(dispatcher)
            .onEach { event ->
                when (event) {
                    MonsterRegistrationEvent.Hide -> setState { copy(isOpen = false) }
                    is MonsterRegistrationEvent.ShowEdit -> {
                        params.value = MonsterRegistrationParams(monsterIndex = event.monsterIndex)
                        setState { copy(isOpen = true) }
                    }
                }
            }
            .launchIn(scope)
    }
}

interface MonsterRegistrationIntent {

    fun onClose()

    fun onMonsterChanged(monster: Monster)
}
