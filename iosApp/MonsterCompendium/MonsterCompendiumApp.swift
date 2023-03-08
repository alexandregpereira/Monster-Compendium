//
//  MonsterCompendiumApp.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 27/01/23.
//

import SwiftUI
import shared

@main
struct MonsterCompendiumApp: App {

    init() {
        IosAppModuleKt.doInitKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            ZStack {
                MonsterCompendiumScreenView()
                MonsterDetailScreenView()
            }
        }
    }
}
