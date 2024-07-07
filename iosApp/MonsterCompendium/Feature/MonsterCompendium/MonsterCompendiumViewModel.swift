//
//  MonsterCompendiumViewModel.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 29/01/23.
//

import Foundation
import KotlinApp

@MainActor class MonsterCompendiumViewModel : ObservableObject {
    
    private let feature: MonsterCompendiumFeature = MonsterCompendiumFeature()

    @Published var state: MonsterCompendiumState = MonsterCompendiumState.Companion.shared.Empty
    @Published var compendiumIndex: Int = -1
    
    init() {
        feature.stateHolder.state.subscribe { (state: MonsterCompendiumState) -> Void in
            self.state = state
        }
        feature.stateHolder.action.subscribe { (action: MonsterCompendiumAction) -> Void in
            guard let action = action as? MonsterCompendiumAction.GoToCompendiumIndex else { return }
            self.compendiumIndex = Int(action.index)
        }
    }
    
    func getInitialScrollItemPosition() -> Int {
        return Int(feature.stateHolder.initialScrollItemPosition)
    }
    
    func onItemClick(index: String) {
        feature.stateHolder.onItemClick(index: index)
    }
    
    func onFirstVisibleItemChange(position: Int) {
        feature.stateHolder.onFirstVisibleItemChange(position: Int32(position))
    }

    func onPopupOpened() {
        feature.stateHolder.onPopupOpened()
    }

    func onPopupClosed() {
        feature.stateHolder.onPopupClosed()
    }

    func onAlphabetIndexClicked(position: Int) {
        feature.stateHolder.onAlphabetIndexClicked(position: Int32(position))
    }

    func onTableContentIndexClicked(position: Int) {
        feature.stateHolder.onTableContentIndexClicked(position: Int32(position))
    }
    
    deinit {
        feature.stateHolder.onCleared()
    }
}
