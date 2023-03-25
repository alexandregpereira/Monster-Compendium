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
                        
                        ZStack {
                            if viewModel.isLoading {
                                Color.white.ignoresSafeArea()
                                .transition(.loadingTransition)
                            }
                        }
                        .animation(.spring(response: 3), value: viewModel.isLoading)
                        
                        AppBarIconView(image: Image(systemName: "x.circle.fill"), onClicked: { viewModel.onClose() })
                            .padding(4)
                            .padding(.top, geometry.safeAreaInsets.top)
                    }
                    .clipped()
                    .transition(.offset(y: geometry.size.height + geometry.safeAreaInsets.bottom + geometry.safeAreaInsets.top))
                }
            }.frame(
                minWidth: 0,
                maxWidth: .infinity,
                minHeight: 0,
                maxHeight: .infinity,
                alignment: .topLeading
            )
            .ignoresSafeArea()
            .animation(.spring(response: 0.3, dampingFraction: 0.98), value: state.isShowing)
        }
    }
}

private extension AnyTransition {
    
    static var loadingTransition: AnyTransition {
        .asymmetric(
            insertion: .opacity.animation(.spring(response: 0.1)),
            removal: .opacity.animation(.spring(response: 0.3))
        )
    }
}
