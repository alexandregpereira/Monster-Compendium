//
//  MonsterTitle.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 04/03/23.
//

import SwiftUI

struct MonsterTitleView: View {
    let title: String
    let subTitle: String?
    
    init(title: String, subTitle: String? = nil) {
        self.title = title
        self.subTitle = subTitle
    }
    
    var body: some View {
        HStack {
            ScreenHeaderView(title: title, subTitle: subTitle)
                .padding()
            
            Spacer()
        }
        .frame(maxWidth: .infinity)
    }
}

private struct OptionIconView: View {
    let onOptionsClicked: (() -> Void)?
    
    var body: some View {
        AppBarIconView(image: Image(systemName: "ellipsis.circle.fill"), contentDescription: "Monster detail options", onClicked: onOptionsClicked)
                .padding()
    }
}

struct MonsterTitle_Previews: PreviewProvider {
    static var previews: some View {
        MonsterTitleView(title: "My Screen", subTitle: "Subtitle")
            .previewLayout(.sizeThatFits)
    }
}
