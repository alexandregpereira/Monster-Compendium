//
//  ScreenHeader.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 04/03/23.
//

import SwiftUI

struct ScreenHeaderView: View {
    enum HeaderFontSize {
        case small, large
    }
    
    let title: String
    let subTitle: String?
    let contentPadding: EdgeInsets
    let titleFontSize: HeaderFontSize
    
    init(title: String, subTitle: String? = nil, contentPadding: EdgeInsets = .init(), titleFontSize: HeaderFontSize = .large) {
        self.title = title
        self.subTitle = subTitle
        self.contentPadding = contentPadding
        self.titleFontSize = titleFontSize
    }
    
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            Text(title)
                .font(titleFontSize == .large ? .system(size: 24, weight: .bold) : .system(size: 16, weight: .semibold))
                .padding(.top, contentPadding.top)
                .padding(.bottom, subTitle == nil ? contentPadding.bottom : 0)
                .padding(.leading, contentPadding.leading)
            if let subTitle = subTitle {
                Text(subTitle)
                    .font(.system(size: 12, weight: .light, design: .default))
                    .foregroundColor(.secondary)
                    .padding(.bottom, contentPadding.bottom)
                    .padding(.leading, contentPadding.leading)
            }
        }
    }
}

struct ScreenHeader_Previews: PreviewProvider {
    static var previews: some View {
        ScreenHeaderView(title: "My Screen", subTitle: "Subtitle")
            .padding()
            .previewLayout(.sizeThatFits)
    }
}
