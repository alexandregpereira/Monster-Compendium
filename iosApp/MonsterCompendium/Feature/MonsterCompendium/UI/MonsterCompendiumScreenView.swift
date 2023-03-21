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
        let state = viewModel.state
        ZStack {
            if viewModel.isLoading {
                ProgressView()
                    .progressViewStyle(CircularProgressViewStyle())
                    .transition(.opacity)
            } else {
                ZStack {
                    MonsterCompendiumView(
                        items: state.items,
                        compendiumIndex: viewModel.compendiumIndex,
                        onMonsterItemClick: { viewModel.onItemClick(index: $0) },
                        onFirstVisibleIndexChange: { viewModel.onFirstVisibleItemChange(position: $0) }
                    )
                    
                    TableContentPopup(
                        tableContent: state.tableContent,
                        alphabet: state.alphabet,
                        alphabetSelectedIndex: state.alphabetSelectedIndex,
                        tableContentSelectedIndex: state.tableContentSelectedIndex,
                        tableContentInitialIndex: state.tableContentInitialIndex,
                        screen: state.tableContentPopupScreenType,
                        onOpenButtonClicked: { viewModel.onPopupOpened() },
                        onCloseButtonClicked: { viewModel.onPopupClosed() },
                        onTableContentClicked: { viewModel.onTableContentIndexClicked(position: $0) },
                        onAlphabetIndexClicked: { viewModel.onAlphabetIndexClicked(position: $0) }
                    )
                }
                .transition(.opacity)
            }
        }
        .animation(.spring(), value: viewModel.isLoading)
    }
}
