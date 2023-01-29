//
//  MonsterCompendiumView.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 29/01/23.
//

import SwiftUI

struct MonsterCompendiumView: View {
    
    let monsters: [MonsterUiState]
    let onMonsterItemClick: (String) -> Void
    
    let columns = [
        GridItem(spacing: 16, alignment: .top),
        GridItem(alignment: .top)
    ]
    
    var body: some View {
        ScrollView {
            LazyVGrid(columns: columns, spacing: 32) {
                ForEach(monsters, id: \.index) { monster in
                    MonsterCardView(monster: monster)
                        .onTapGesture {
                            onMonsterItemClick(monster.index)
                        }
                }
            }
        }
        .padding()
    }
}

struct MonsterCompendiumView_Previews: PreviewProvider {
    static var previews: some View {
        MonsterCompendiumView(monsters: MonsterUiState.sampleData, onMonsterItemClick: { print($0) })
    }
}
