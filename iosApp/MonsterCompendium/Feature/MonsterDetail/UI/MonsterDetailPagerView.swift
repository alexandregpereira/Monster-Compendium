//
//  MonsterDetailPagerView.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 26/03/23.
//

import SwiftUI

// Not working properly
struct MonsterDetailPagerView: View {
    let monsters: [MonsterDetailItemUiState]
    let onPagerChanged: (Int) -> Void
    private var selection: Binding<Int>
    
    init(
        monsters: [MonsterDetailItemUiState],
        initialMonsterListPositionIndex: Binding<Int>,
        onPagerChanged: @escaping (Int) -> Void
    ) {
        self.monsters = monsters
        self.selection = initialMonsterListPositionIndex
        self.onPagerChanged = onPagerChanged
    }
    
    var body: some View {
        TabView(selection: selection) {
            ForEach(Array(monsters.enumerated()), id: \.element.id) { index, item in
                MonsterDetailView(monster: item)
                    .tag(index)
            }
        }
        .tabViewStyle(PageTabViewStyle(indexDisplayMode: .automatic))
    }
}
