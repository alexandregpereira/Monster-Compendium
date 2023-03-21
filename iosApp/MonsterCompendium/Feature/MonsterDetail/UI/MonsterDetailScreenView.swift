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
                if state.isShowing {
                    ZStack(alignment: .topLeading) {
                        MonsterDetailView(monster: state.monster)
                        
                        AppBarIconView(image: Image(systemName: "x.circle.fill"), onClicked: { viewModel.onClose() })
                            .padding()
                    }
                    .transition(.offset(y: geometry.size.height + geometry.safeAreaInsets.bottom))
                }
            }.frame(
                minWidth: 0,
                maxWidth: .infinity,
                minHeight: 0,
                maxHeight: .infinity,
                alignment: .topLeading
            )
            .animation(.spring(response: 0.3), value: state.isShowing)
        }
    }
}
