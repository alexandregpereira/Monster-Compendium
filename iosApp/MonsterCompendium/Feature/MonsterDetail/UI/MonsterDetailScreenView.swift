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
                if state.showDetail {
                    ZStack(alignment: .topLeading) {
                        if !state.monsters.isEmpty {
                            MonsterDetailView(monster: state.monsters[Int(state.initialMonsterListPositionIndex)])
                        }
                        
                        ZStack {
                            if state.isLoading {
                                Color.white.ignoresSafeArea()
                                .transition(.loadingTransition)
                            }
                        }
                        .animation(.spring(), value: state.isLoading)
                        
                        AppBarIconView(image: Image(systemName: "x.circle.fill"), onClicked: { viewModel.onClose() })
                            .padding(4)
                            .padding(.top, geometry.safeAreaInsets.top)
                    }
                    .clipped()
                    .transition(.offset(y: geometry.size.height + geometry.safeAreaInsets.bottom + geometry.safeAreaInsets.top))
                    .environmentObject(MonsterDetailStringsWrapper(state.strings))
                }
            }.frame(
                minWidth: 0,
                maxWidth: .infinity,
                minHeight: 0,
                maxHeight: .infinity,
                alignment: .topLeading
            )
            .ignoresSafeArea()
            .animation(.spring(response: 0.3, dampingFraction: 0.98), value: state.showDetail)
        }
    }
}

private extension AnyTransition {
    
    static var loadingTransition: AnyTransition {
        .asymmetric(
            insertion: .opacity.animation(.spring(response: 0.1)),
            removal: .opacity.animation(.spring(response: 0.5))
        )
    }
}
