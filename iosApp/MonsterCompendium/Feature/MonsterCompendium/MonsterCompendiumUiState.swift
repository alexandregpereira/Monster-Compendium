//
//  MonsterCompendiumUiState.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 29/01/23.
//

import Foundation

struct MonsterCompendiumUiState {
    var items: [MonsterCompendiumItemUiState] = []
    var alphabet: [String] = []
    var tableContent: [TableContentItemState] = []
    var tableContentPopupScreenType: TableContentPopup.ScreenType = .circleLetter
    var alphabetSelectedIndex: Int = 0
    var tableContentSelectedIndex: Int = 0
    var tableContentInitialIndex: Int = 0
}
