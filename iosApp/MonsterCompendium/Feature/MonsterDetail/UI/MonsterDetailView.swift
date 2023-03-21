//
//  MonsterDetailView.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 02/03/23.
//

import SwiftUI
import SDWebImageSwiftUI

struct MonsterDetailView : View {
    
    @State var isAnimating = true
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
                            .padding(EdgeInsets(top: 16, leading: 16, bottom: 16, trailing: 16))
                    }
                }
                .frame(width: geometry.size.width, height: 500, alignment: .center)
                .id(monster.id)
                
                ScrollView {
                    LazyVStack {
                        Spacer().frame(maxWidth: .infinity)
                            .frame(height: 500)
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
