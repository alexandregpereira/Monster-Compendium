//
//  MonsterDetailView.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 02/03/23.
//

import SwiftUI

struct MonsterDetailView : View {
    let monster: MonsterDetailItemUiState
    
    var body: some View {
        GeometryReader { geometry in
            ZStack(alignment: .topLeading) {
                Color(hex: monster.backgroundColorLight)?.ignoresSafeArea()
                AsyncImage(
                    url: URL(string: monster.imageUrl),
                    scale: 1,
                    transaction: Transaction(animation: .spring())
                ) { phase in
                    if let image = phase.image {
                        image.resizable()
                            .aspectRatio(contentMode: .fit)
                            .padding(16)
                    }
                }
                .frame(width: geometry.size.width, height: 600, alignment: .center)
                .padding(.top, geometry.safeAreaInsets.top + 32)
                .id(monster.id)
                
                ScrollView {
                    LazyVStack {
                        Spacer().frame(maxWidth: .infinity)
                            .frame(height: 600)
                            .padding(.top, geometry.safeAreaInsets.top + 32)
                            .id(0)
                        
                        MonsterTitleView(title: monster.name, subTitle: monster.subtitle)
                            .background(Color.white)
                            .id(1)
                    }
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
        .previewLayout(.sizeThatFits)
    }
}
