//
//  MonsterDetailViewModel.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 02/03/23.
//

import Foundation
import shared

@MainActor class MonsterDetailViewModel : ObservableObject {
    
    @Published var isLoading: Bool = true
    @Published var state: MonsterDetailUiState = MonsterDetailUiState()
    @Published var initialMonsterListPositionIndex: Int = 0
    private let feature: MonsterDetailFeature
    private var stateWatcher : Closeable? = nil
    
    init() {
        feature = MonsterDetailFeature()
        stateWatcher = feature.state.collect { (state: MonsterDetailState) -> Void in
            self.state.isShowing = state.showDetail
            self.isLoading = state.isLoading
            self.state.monsters = state.monsters.asUiState()
            self.initialMonsterListPositionIndex = Int(state.initialMonsterListPositionIndex)
            print("monsterIndex = \(self.feature.stateHolder.monsterIndex)")
            print("initialMonsterListPositionIndex = \(state.initialMonsterListPositionIndex)")
            print("isLoading = \(state.isLoading)")
            print("showDetail = \(state.showDetail)\n")
            print("-----------------------------------\n")
        }
    }
    
    func onClose() {
        feature.stateHolder.onClose()
    }
    
    func onPagerChanged(_ newPagePosition: Int) {
        feature.stateHolder.onMonsterChanged(monsterIndex: self.state.monsters[newPagePosition].id, scrolled: true)
    }
    
    deinit {
        stateWatcher?.close()
    }
}
