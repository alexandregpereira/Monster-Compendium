//
//  MonsterDetailStrings.swift
//  MonsterCompendium
//
//  Created by alexandre.gpereira on 12/02/24.
//

import Foundation
import shared

class MonsterDetailStringsWrapper: ObservableObject {
    @Published var value: MonsterDetailStrings
    
    init(_ strings: MonsterDetailStrings) {
        self.value = strings
    }
}
