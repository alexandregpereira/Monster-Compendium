//
//  MonsterCompendiumApp.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 27/01/23.
//

import SwiftUI
import KotlinApp

@main
struct MonsterCompendiumApp: App {

    init() {
        IosAppModuleKt.doInitKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            ComposeView()
        }
    }
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerFactory().create()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView().ignoresSafeArea(.keyboard) // Compose has own keyboard handler
    }
}
