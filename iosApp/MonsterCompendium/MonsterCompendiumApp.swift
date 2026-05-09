//
//  MonsterCompendiumApp.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 27/01/23.
//

import SwiftUI
import KotlinApp
import FirebaseCore

@main
struct MonsterCompendiumApp: App {

    init() {
        FirebaseApp.configure()
        IosAppModuleKt.doInitKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            ComposeView()
                .onOpenURL { url in
                    _ = url.startAccessingSecurityScopedResource()
                    defer { url.stopAccessingSecurityScopedResource() }
                    FileOpenHandlerKt.handleCompendiumFileOpen(
                        name: url.lastPathComponent,
                    )
                }
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
