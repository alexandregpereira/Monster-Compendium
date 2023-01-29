//
//  MonsterUiState.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 29/01/23.
//

import Foundation

struct MonsterUiState {
    let index: String
    var name: String = ""
    var challengeRating: Float = 0.0
    var imageUrl: String = ""
    var backgroundColorLight: String = ""
    var backgroundColorDark: String = ""
    var isImageHorizontal: Bool
}

extension MonsterUiState {
    static let sampleData: [MonsterUiState] = (1...100).map {
        MonsterUiState(
            index: "\($0)",
            name: "Aboleth\($0)",
            challengeRating: 20.0,
            imageUrl: "https://raw.githubusercontent.com/alexandregpereira/hunter-api/main/images/aboleth.png",
            backgroundColorLight: "#d3dedc",
            backgroundColorDark: "#d3dedc",
            isImageHorizontal: $0 % 5 == 0
        )
    }
}
