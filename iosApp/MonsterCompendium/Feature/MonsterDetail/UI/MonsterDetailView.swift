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
        ZStack {
            Color(hex: monster.backgroundColorLight)?.ignoresSafeArea()
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
                .frame(height: 600)
                .padding(EdgeInsets(top: 16, leading: 16, bottom: 16, trailing: 16))
            
            ScrollView {
                LazyVStack {
                    Spacer().frame(maxWidth: .infinity)
                        .frame(height: 600)
                        .id(0)
                    
                    MonsterTitleView(title: monster.name, subTitle: monster.subtitle)
                        .background(Color.white)
                        .id(1)
                    
                    
                }
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
                backgroundColorLight: "#d3dedc",
                name: "Aboleth",
                subtitle: "Lawful evil, creature medium"
            )
        )
    }
}
