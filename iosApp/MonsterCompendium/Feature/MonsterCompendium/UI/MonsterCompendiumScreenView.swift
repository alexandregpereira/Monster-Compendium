//
//  MonsterCompendiumScreenView.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 29/01/23.
//

import SwiftUI

struct MonsterCompendiumScreenView: View {
    
    @ObservedObject var viewModel: MonsterCompendiumViewModel
    let onMonsterItemClick: (String) -> Void
    
    init(onMonsterItemClick: @escaping (String) -> Void) {
        self.viewModel = MonsterCompendiumViewModel()
        self.onMonsterItemClick = onMonsterItemClick
    }
    
    var body: some View {
        let state = viewModel.state
        if viewModel.isLoading {
            ProgressView()
                .progressViewStyle(CircularProgressViewStyle())
                .transition(.opacity.animation(.spring()))
        } else {
            MonsterCompendiumView(items: state.items, onMonsterItemClick: onMonsterItemClick)
                .transition(.opacity.animation(.spring()))
        }
    }
}
