//
//  MonsterDetailScreenView.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 02/03/23.
//

import SwiftUI

struct MonsterDetailScreenView : View {
    
    @ObservedObject var viewModel: MonsterDetailViewModel
    
    init() {
        self.viewModel = MonsterDetailViewModel()
    }
    
    var body: some View {
        let state = viewModel.state
        GeometryReader { geometry in
            ZStack(alignment: .topLeading) {
                MonsterDetailView(monster: state.monster)
                
                AppBarIconView(image: Image(systemName: "x.circle.fill"), onClicked: { viewModel.onClose() })
                    .padding()
            }.frame(
                minWidth: 0,
                maxWidth: .infinity,
                minHeight: 0,
                maxHeight: .infinity,
                alignment: .topLeading
            )
            .offset(y: state.isShowing ? 0 : geometry.size.height + 40)
            .animation(Animation.easeInOut(duration: 0.2), value: state.isShowing ? 0 : geometry.size.height + 40)
            .transition(.move(edge: .bottom))
        }
    }
}
