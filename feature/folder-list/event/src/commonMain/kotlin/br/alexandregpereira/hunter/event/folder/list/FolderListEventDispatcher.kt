package br.alexandregpereira.hunter.event.folder.list

import br.alexandregpereira.hunter.event.v2.EventDispatcher

class FolderListEventDispatcher : EventDispatcher<FolderListEvent> by EventDispatcher(
    extraBufferCapacity = 1,
)
