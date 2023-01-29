//
//  UiState.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 29/01/23.
//

import Foundation

struct MonsterUiState {
    let index: String
    var name: String = ""
    var type: MonsterTypeUiState = MonsterTypeUiState.ABERRATION
    var challengeRating: Float = 0.0
    var imageUrl: String = ""
    var backgroundColorLight: String = ""
    var backgroundColorDark: String = ""
}

enum MonsterTypeUiState {
    case ABERRATION
    case BEAST
    case CELESTIAL
    case CONSTRUCT
    case DRAGON
    case ELEMENTAL
    case FEY
    case FIEND
    case GIANT
    case HUMANOID
    case MONSTROSITY
    case OOZE
    case PLANT
    case UNDEAD
}

extension MonsterUiState {
    static let sampleData: [MonsterUiState] = (1...100).map {
        MonsterUiState(
            index: "\($0)",
            name: "Aboleth\($0)",
            type: MonsterTypeUiState.ABERRATION,
            challengeRating: 20.0,
            imageUrl: "https://raw.githubusercontent.com/alexandregpereira/hunter-api/main/images/aboleth.png",
            backgroundColorLight: "#d3dedc",
            backgroundColorDark: "#d3dedc"
        )
    }
}

