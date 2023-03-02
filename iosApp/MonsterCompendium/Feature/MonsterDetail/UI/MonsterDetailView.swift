//
//  MonsterDetailView.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 02/03/23.
//

import SwiftUI
import SDWebImageSwiftUI

struct MonsterDetailView : View {
    
    let monster: MonsterDetailItemUiState
    
    var body: some View {
        VStack {
            ZStack {
                Color(hex: monster.backgroundColorLight)
                WebImage(url: URL(string: monster.imageUrl))
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(
                      minWidth: 0,
                      maxWidth: .infinity,
                      minHeight: 0,
                      maxHeight: .infinity,
                      alignment: .topLeading
                    )
                    .padding(EdgeInsets(top: 16, leading: 16, bottom: 16, trailing: 16))
            }
        }
    }
}

struct MonsterDetailView_Previews: PreviewProvider {
    static var previews: some View {
        MonsterDetailView(
            monster: MonsterDetailItemUiState(
                id: "id",
                imageUrl: "https://raw.githubusercontent.com/alexandregpereira/hunter-api/main/images/aboleth.png",
                backgroundColorLight: "#d3dedc"
            )
        )
    }
}
