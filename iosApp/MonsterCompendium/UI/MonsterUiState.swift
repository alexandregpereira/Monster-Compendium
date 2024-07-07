//
//  MonsterUiState.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 29/01/23.
//

import Foundation
import KotlinApp

struct MonsterUiState {
    let index: String
    var name: String = ""
    var challengeRating: Float = 0.0
    var imageUrl: String = ""
    var backgroundColorLight: String = ""
    var backgroundColorDark: String = ""
    var isImageHorizontal: Bool
}

extension MonsterPreviewState {
    static let sampleData: [MonsterPreviewState] = (1...100).map {
        MonsterPreviewState(
            index: "\($0)",
            name: "Aboleth\($0)",
            type: MonsterType.aberration,
            challengeRating: "20",
            imageUrl: "https://raw.githubusercontent.com/alexandregpereira/hunter-api/main/images/aboleth.png",
            backgroundColorLight: "#d3dedc",
            backgroundColorDark: "#d3dedc",
            isImageHorizontal: $0 % 5 == 0
        )
    }
}
