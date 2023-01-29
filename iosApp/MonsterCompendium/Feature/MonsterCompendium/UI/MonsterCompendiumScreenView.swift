//
//  MonsterCompendiumScreenView.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 29/01/23.
//

import SwiftUI

struct MonsterCompendiumScreenView: View {
    
    @ObservedObject var viewModel: MonsterCompendiumViewModel
    
    init() {
        self.viewModel = MonsterCompendiumViewModel()
    }
    
    var body: some View {
        if viewModel.isLoading {
            ProgressView()
                .progressViewStyle(CircularProgressViewStyle())
                .transition(.opacity.animation(.spring()))
        } else {
            MonsterCompendiumView(monsters: viewModel.monsters, onMonsterItemClick: { $0 })
                .transition(.opacity.animation(.spring()))
        }
    }
}
