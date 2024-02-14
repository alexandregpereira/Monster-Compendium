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
            if viewModel.state.isLoading {
                ProgressView()
                    .progressViewStyle(CircularProgressViewStyle())
                    .transition(.opacity)
            } else {
                ZStack {
                    if !state.items.isEmpty {
                        MonsterCompendiumView(
                            items: state.items,
                            initialCompendiumIndex: viewModel.getInitialScrollItemPosition(),
                            compendiumIndex: viewModel.compendiumIndex,
                            onMonsterItemClick: { viewModel.onItemClick(index: $0) },
                            onFirstVisibleIndexChange: { viewModel.onFirstVisibleItemChange(position: $0) }
                        )
                    }
                    
                    if !state.alphabet.isEmpty {
                        TableContentPopup(
                            tableContent: state.tableContent,
                            alphabet: state.alphabet,
                            alphabetSelectedIndex: Int(state.alphabetSelectedIndex),
                            tableContentSelectedIndex: Int(state.tableContentIndex),
                            tableContentInitialIndex: Int(state.tableContentInitialIndex),
                            screen: state.popupOpened && state.tableContentOpened ? .tableContent : (state.popupOpened ? .alphabetGrid : .circleLetter),
                            onOpenButtonClicked: { viewModel.onPopupOpened() },
                            onCloseButtonClicked: { viewModel.onPopupClosed() },
                            onTableContentClicked: { viewModel.onTableContentIndexClicked(position: $0) },
                            onAlphabetIndexClicked: { viewModel.onAlphabetIndexClicked(position: $0) }
                        )
                    }
                }
                .transition(.opacity)
            }
        }
        .animation(.spring(), value: viewModel.state.isLoading)
    }
}
