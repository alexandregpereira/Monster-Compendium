//
//  MonsterCompendiumApp.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 27/01/23.
//

import SwiftUI
import shared

@main
struct MonsterCompendiumApp: App {
    
    @ObservedObject var viewModel: MonsterCompendiumAppViewModel
    
    init() {
        IosAppModuleKt.doInitKoin()
        self.viewModel = MonsterCompendiumAppViewModel()
    }
    
    var body: some Scene {
        let state = viewModel.state
        WindowGroup {
            ZStack {
                MonsterCompendiumScreenView(
                    onMonsterItemClick: { viewModel.onShowMonsterDetail(monsterId: $0) }
                )
                
                if state.isMonsterDetailShowing {
                    MonsterDetailScreenView(monsterId: state.monsterId, onCloseClick: { viewModel.onHideMonsterDetail() })
                }
            }
        }
    }
}
