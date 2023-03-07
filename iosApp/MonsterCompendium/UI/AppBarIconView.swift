//
//  AppBarIcon.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 04/03/23.
//

import SwiftUI

struct AppBarIconView: View {
    let image: Image
    let contentDescription: String
    let onClicked: (() -> Void)?
    
    init(image: Image, contentDescription: String = "", onClicked: (() -> Void)? = nil) {
        self.image = image
        self.contentDescription = contentDescription
        self.onClicked = onClicked
    }
    
    var body: some View {
        image
            .resizable()
            .aspectRatio(contentMode: .fit)
            .foregroundColor(Color.black.opacity(0.7))
            .frame(width: 24, height: 24)
            .padding(8)
            .background(Color.white.opacity(0.01))
            .onTapGesture(perform: {
                onClicked?()
            })
    }
}

struct AppBarIcon_Previews: PreviewProvider {
    static var previews: some View {
        AppBarIconView(
            image: Image(systemName: "person.fill"),
            contentDescription: "Person Icon",
            onClicked: {}
        )
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
