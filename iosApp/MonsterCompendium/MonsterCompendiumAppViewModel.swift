//
//  MonsterCompendiumAppViewModel.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 02/03/23.
//

import Foundation

@MainActor class MonsterCompendiumAppViewModel : ObservableObject {
    
    @Published var state: MonsterCompendiumAppUiState = MonsterCompendiumAppUiState()
    
    func onShowMonsterDetail(monsterId: String) {
        state.monsterId = monsterId
        state.isMonsterDetailShowing = true
    }
    
    func onHideMonsterDetail() {
        state.isMonsterDetailShowing = false
    }
}
