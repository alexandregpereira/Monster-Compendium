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
            let gridItems = [
                GridItem(.fixed(UIScreen.main.bounds.size.width / 2), spacing: 0, alignment: .leading),
                GridItem(.fixed(UIScreen.main.bounds.size.width / 2), spacing: 0, alignment: .leading)
            ]

            LazyVGrid(columns: gridItems) {
                ForEach(items) { item in
                    
                    ZStack(alignment: .leading) {
                        switch item {
                        case .title (let title):
                            let font = title.isHeader ? Font.system(size: 48) : Font.title
                            let bottomPadding = title.isHeader ? 32.0 : 16
                            Text(title.value)
                                .font(font)
                            .padding(EdgeInsets(top: 40, leading: 16, bottom: bottomPadding, trailing: 16))
                            .frame(maxWidth: .infinity, alignment: .leading)
                        case .item (let item):
                            let monster = item.value
                            MonsterCardView(monster: monster)
                                .padding(EdgeInsets(top: 8, leading: 8, bottom: 8, trailing: 8))
                                .onTapGesture {
                                    onMonsterItemClick(monster.index)
                                }
                        }
                    }
                    .frame(
                        width: item.isHorizontal() ? UIScreen.main.bounds.size.width : (UIScreen.main.bounds.size.width) / 2
                    )
                    
                    if item.isHorizontal() { Color.clear }
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
