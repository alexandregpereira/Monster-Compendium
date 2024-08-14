package br.alexandregpereira.hunter.shareContent.state

import br.alexadregpereira.hunter.shareContent.event.ShareContentEvent.Import
import br.alexadregpereira.hunter.shareContent.event.ShareContentEventDispatcher
import br.alexandregpereira.hunter.localization.AppReactiveLocalization
import br.alexandregpereira.hunter.shareContent.domain.GetMonsterContentToExport
import br.alexandregpereira.hunter.shareContent.domain.GetMonstersContentToExport
import br.alexandregpereira.hunter.shareContent.domain.ImportContent
import br.alexandregpereira.hunter.shareContent.domain.ImportContentException
import br.alexandregpereira.hunter.shareContent.state.ShareContentImportError.InvalidContent
import br.alexandregpereira.hunter.state.MutableActionHandler
import br.alexandregpereira.hunter.state.UiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class ShareContentStateHolder(
    private val dispatcher: CoroutineDispatcher,
    appLocalization: AppReactiveLocalization,
    private val eventDispatcher: ShareContentEventDispatcher,
    private val importContent: ImportContent,
    private val getMonsterContentToExport: GetMonsterContentToExport,
    private val getMonstersContentToExport: GetMonstersContentToExport,
) : UiModel<ShareContentState>(
    ShareContentState(strings = appLocalization.getLanguage().getStrings())
), MutableActionHandler<ShareContentUiEvent> by MutableActionHandler() {

    init {
        appLocalization.languageFlow
            .onEach { setState { copy(strings = it.getStrings()) } }
            .launchIn(scope)
    }

    fun onImport() {
        if (state.value.contentToImport.isBlank()) {
            return
        }
        importContent(state.value.contentToImport)
            .flowOn(dispatcher)
            .onEach {
                eventDispatcher.dispatchEvent(Import.OnFinish)
                delay(1000)
                setState { copy(contentToImport = "", importError = null) }
            }
            .catch { cause ->
                if (cause is ImportContentException) {
                    cause.printStackTrace()
                    val importError = when (cause) {
                        is ImportContentException.InvalidContent -> InvalidContent
                    }
                    setState { copy(importError = importError) }
                } else throw cause
            }
            .launchIn(scope)
    }

    fun onPasteImportContent(content: String) = setState {
        copy(contentToImport = content.trim(), importError = null)
    }

    fun fetchMonsterContentToExport(monsterIndex: String?, actualClipboardContent: String?) {
        val contentToExportFlow = monsterIndex?.let { getMonsterContentToExport(it) }
            ?: getMonstersContentToExport()
        contentToExportFlow
            .flowOn(dispatcher)
            .onEach { contentToExport ->
                setState {
                    copy(
                        contentToExport = contentToExport,
                        exportCopyButtonEnabled = contentToExport != actualClipboardContent,
                    )
                }
            }
            .launchIn(scope)
    }

    fun onCopyContentToExport() {
        setState { copy(exportCopyButtonEnabled = false) }
        sendAction(ShareContentUiEvent.CopyContentUiToExport(state.value.contentToExport))
    }
}
