//
//  MonsterCompendiumItemUiState.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 29/01/23.
//

import Foundation

enum MonsterCompendiumItemUiState : Identifiable {
    
    var id: String {
        switch self {
        case .title (let title): return title.id
        case .item (let item): return item.value.index
        }
    }
    
    struct Title {
        let value: String
        let id: String
        let isHeader: Bool
    }
    
    struct Item {
        let value: MonsterUiState
    }
    
    case title(Title)
    case item(Item)
}

extension MonsterCompendiumItemUiState {
    static let sampleData: [MonsterCompendiumItemUiState] = (0...100).map {
        if $0 == 0 {
            return MonsterCompendiumItemUiState.title(MonsterCompendiumItemUiState.Title(value: "A", id: "\($0)", isHeader: true))
        } else if $0 % 6 == 0 {
            return MonsterCompendiumItemUiState.title(MonsterCompendiumItemUiState.Title(value: "\($0)", id: "\($0)", isHeader: false))
        } else {
            return MonsterCompendiumItemUiState.item(MonsterCompendiumItemUiState.Item(
                value: MonsterUiState(
                    index: "\($0)",
                    name: "Aboleth\($0)",
                    challengeRating: 20.0,
                    imageUrl: "https://raw.githubusercontent.com/alexandregpereira/hunter-api/main/images/aboleth.png",
                    backgroundColorLight: "#d3dedc",
                    backgroundColorDark: "#d3dedc",
                    isImageHorizontal: $0 % 5 == 0
                )
            ))
        }
    }
}
