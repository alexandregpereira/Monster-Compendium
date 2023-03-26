//
//  IconInfo.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 25/03/23.
//

import SwiftUI

struct IconInfo: View {
    var image: String
    var iconSize: CGFloat = 56
    var iconColor: Color = .primary
    var iconAlpha: Double = 0.7
    var title: String? = nil
    var iconText: String? = nil
    var iconTextPadding: EdgeInsets = EdgeInsets()

    var body: some View {
        VStack(alignment: .center) {
            ZStack {
                Image(systemName: image)
                    .resizable()
                    .frame(width: iconSize, height: iconSize)
                    .foregroundColor(iconColor)
                    .opacity(iconAlpha)

                if let text = iconText {
                    Text(text)
                        .fontWeight(.bold)
                        .font(.system(size: 18))
                        .foregroundColor(.white)
                        .padding(iconTextPadding)
                }
            }

            if let titleText = title {
                Text(titleText)
                    .fontWeight(.light)
                    .font(.system(size: 12))
                    .multilineTextAlignment(.center)
                    .frame(width: 72)
                    .padding(.top, 8)
            }
        }
    }
}

struct IconInfo_Previews: PreviewProvider {
    static var previews: some View {
        IconInfo(image: "star", title: "Example", iconText: "1")
    }
}
