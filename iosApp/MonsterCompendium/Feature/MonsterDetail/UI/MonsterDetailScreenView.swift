//
//  MonsterDetailScreenView.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 02/03/23.
//

import SwiftUI

struct MonsterDetailScreenView : View {
    
    @ObservedObject var viewModel: MonsterDetailViewModel
    private let onCloseClick: () -> Void
    
    init(monsterId: String, onCloseClick: @escaping () -> Void) {
        self.viewModel = MonsterDetailViewModel(monsterId: monsterId)
        self.onCloseClick = onCloseClick
    }
    
    var body: some View {
        let state = viewModel.state
        ZStack(alignment: .topLeading) {
            MonsterDetailView(monster: state.monster)
            
            AppBarIconView(image: Image(systemName: "x.circle.fill"), onClicked: onCloseClick)
                    .padding()
        }.frame(
            minWidth: 0,
            maxWidth: .infinity,
            minHeight: 0,
            maxHeight: .infinity,
            alignment: .topLeading
          )
    }
}
