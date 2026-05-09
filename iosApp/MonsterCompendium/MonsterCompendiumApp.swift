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
                    let tempURL = FileManager.default.temporaryDirectory
                        .appendingPathComponent(url.lastPathComponent)
                    try? FileManager.default.removeItem(at: tempURL)
                    guard (try? FileManager.default.copyItem(at: url, to: tempURL)) != nil else { return }
                    FileOpenHandlerKt.handleCompendiumFileOpen(name: tempURL.absoluteString)
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
