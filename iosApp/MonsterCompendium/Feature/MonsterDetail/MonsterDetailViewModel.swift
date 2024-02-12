//
//  MonsterDetailViewModel.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 02/03/23.
//

import Foundation
import shared

@MainActor class MonsterDetailViewModel : ObservableObject {
    
    @Published var state: MonsterDetailState = MonsterDetailState.Companion.shared.Empty
    private let feature: MonsterDetailFeature
    private var stateWatcher : Closeable? = nil
    
    init() {
        feature = MonsterDetailFeature()
        stateWatcher = feature.state.collect { (state: MonsterDetailState) -> Void in
            self.state = state
        }
    }
    
    func onClose() {
        feature.stateHolder.onClose()
    }
    
    func onPagerChanged(_ newPagePosition: Int) {
        feature.stateHolder.onMonsterChanged(monsterIndex: self.state.monsters[newPagePosition].index, scrolled: true)
    }
    
    deinit {
        stateWatcher?.close()
    }
}
