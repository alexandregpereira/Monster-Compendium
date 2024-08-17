package br.alexandregpereira.hunter.monster.lore.registration

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import br.alexandregpereira.hunter.event.monster.lore.registration.MonsterLoreRegistrationEvent
import br.alexandregpereira.hunter.event.monster.lore.registration.MonsterLoreRegistrationEventDispatcher
import br.alexandregpereira.hunter.monster.lore.registration.ui.MonsterRegistrationScreen
import br.alexandregpereira.hunter.ui.compose.AppScreen
import br.alexandregpereira.hunter.ui.compose.LocalScreenSize
import br.alexandregpereira.hunter.ui.compose.isLandscape
import br.alexandregpereira.hunter.ui.compose.maxBottomSheetWidth
import org.koin.compose.koinInject

@Composable
fun MonsterLoreRegistrationFeature() = Box(
    Modifier.fillMaxSize(),
    contentAlignment = Alignment.BottomEnd
) {
    val eventDispatcher = koinInject<MonsterLoreRegistrationEventDispatcher>()
    var monsterLoreIndex: String by rememberSaveable {
        mutableStateOf("")
    }
    LaunchedEffect(eventDispatcher.events) {
        eventDispatcher.events.collect { event ->
            when (event) {
                is MonsterLoreRegistrationEvent.Show -> {
                    monsterLoreIndex = event.index
                }
            }
        }
    }

    val screenSize = LocalScreenSize.current
    AppScreen(
        isOpen = monsterLoreIndex.isNotBlank(),
        modifier = Modifier.widthIn(
            max = maxBottomSheetWidth.takeIf { screenSize.isLandscape } ?: Dp.Unspecified
        ),
        onClose = { monsterLoreIndex = "" },
    ) {
        val stateHolder = koinInject<MonsterLoreRegistrationStateHolder>()
        LaunchedEffect(stateHolder) {
            stateHolder.fetchMonsterLore(monsterLoreIndex)
        }
        val state by stateHolder.state.collectAsState()
        MonsterRegistrationScreen(
            state = state,
            onChanged = stateHolder::onChanged
        )
    }
}
