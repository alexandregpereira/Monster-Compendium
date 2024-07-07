//
//  MonsterCompendiumItemUiState.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 29/01/23.
//

import Foundation
import KotlinApp

extension MonsterCompendiumItemState {
    static let sampleData: [MonsterCompendiumItemState] = (0...100).map {
        if $0 == 0 {
            return MonsterCompendiumItemState.Title(id: "\($0)", value: "A", isHeader: true)
        } else if $0 % 6 == 0 {
            return MonsterCompendiumItemState.Title(id: "\($0)", value: "\($0)", isHeader: false)
        } else {
            return MonsterCompendiumItemState.Item(
                monster: MonsterPreviewState.sampleData[0]
            )
        }
    }
}
