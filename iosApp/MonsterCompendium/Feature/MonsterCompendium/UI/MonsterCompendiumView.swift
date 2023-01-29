//
//  MonsterCompendiumView.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 29/01/23.
//

import SwiftUI

struct MonsterCompendiumView: View {
    
    let items: [MonsterCompendiumItemUiState]
    let onMonsterItemClick: (String) -> Void
    
    var body: some View {
        ScrollView {
            LazyVStack(alignment: .leading) {
                ForEach(items) { item in
                    switch item {
                    case .title (let title):
                        let font = title.isHeader ? Font.largeTitle : Font.title
                        let topPadding = title.isHeader ? 16.0 : 8
                        let bottomPadding = title.isHeader ? 32.0 : 16
                        Text(title.value)
                            .font(font)
                            .padding(EdgeInsets(top: 16, leading: 16, bottom: bottomPadding, trailing: 16))
                    case .item (let item):
                        let monster = item.value
                        MonsterCardView(monster: monster)
                            .padding(EdgeInsets(top: 8, leading: 16, bottom: 8, trailing: 16))
                            .onTapGesture {
                                onMonsterItemClick(monster.index)
                            }
                    }
                }
            }
        }
    }
}

struct MonsterCompendiumView_Previews: PreviewProvider {
    static var previews: some View {
        MonsterCompendiumView(items: MonsterCompendiumItemUiState.sampleData, onMonsterItemClick: { print($0) })
    }
}
