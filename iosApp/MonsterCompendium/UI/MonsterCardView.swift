//
//  MonsterCardView.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 29/01/23.
//

import SwiftUI
import SDWebImageSwiftUI

struct MonsterCardView: View {
    @Environment(\.colorScheme) var colorScheme
    
    var monster: MonsterUiState
    
    var body: some View {
        VStack(alignment: .leading) {
            ZStack {
                Color(hex: monster.backgroundColorLight)
                AsyncImage(
                    url: URL(string: monster.imageUrl),
                    scale: 1,
                    transaction: Transaction(animation: .spring())
                ) { phase in
                    if let image = phase.image {
                        image.resizable()
                            .aspectRatio(contentMode: .fit)
                    } else {
                        Color(hex: monster.backgroundColorLight)?.opacity(0.1)
                    }
                }.clipped()
                
                Circle()
                    .fill(.white)
                    .frame(width: 100, height: 100)
                    .frame(
                      minWidth: 0,
                      maxWidth: .infinity,
                      minHeight: 0,
                      maxHeight: .infinity,
                      alignment: .topLeading
                    )
                    .padding(EdgeInsets(top: -50, leading: -50, bottom: 0, trailing: 0))
                
                Text(monster.challengeRatingFormatted)
                    .frame(
                      minWidth: 0,
                      maxWidth: .infinity,
                      minHeight: 0,
                      maxHeight: .infinity,
                      alignment: .topLeading
                    )
                    .font(.callout)
                    .padding(EdgeInsets(top: 12, leading: 10, bottom: 0, trailing: 0))
                
            }.cornerRadius(16)
                .frame(height: 208)
            Text(monster.name)
                .padding(EdgeInsets(top: 0, leading: 4, bottom: 0, trailing: 4))
                .lineLimit(1)
        }
    }
}

struct MonsterCardView_Previews: PreviewProvider {
    static var monsters = MonsterUiState.sampleData
    static var previews: some View {
        MonsterCardView(monster: monsters[0])
            .padding()
            .previewLayout(.sizeThatFits)
    }
}

extension MonsterUiState {
    var challengeRatingFormatted: String {
        if self.challengeRating >= 1 {
            return Int(self.challengeRating).description
        } else {
            let safeNumber = self.challengeRating > 0 ? self.challengeRating : 0.12
            return "1/" + Int(1 / safeNumber).description
        }
    }
}

extension Color {
    init?(hex: String) {
        var hexSanitized = hex.trimmingCharacters(in: .whitespacesAndNewlines)
        hexSanitized = hexSanitized.replacingOccurrences(of: "#", with: "")

        var rgb: UInt64 = 0

        var r: CGFloat = 0.0
        var g: CGFloat = 0.0
        var b: CGFloat = 0.0
        var a: CGFloat = 1.0

        let length = hexSanitized.count

        guard Scanner(string: hexSanitized).scanHexInt64(&rgb) else { return nil }

        if length == 6 {
            r = CGFloat((rgb & 0xFF0000) >> 16) / 255.0
            g = CGFloat((rgb & 0x00FF00) >> 8) / 255.0
            b = CGFloat(rgb & 0x0000FF) / 255.0

        } else if length == 8 {
            r = CGFloat((rgb & 0xFF000000) >> 24) / 255.0
            g = CGFloat((rgb & 0x00FF0000) >> 16) / 255.0
            b = CGFloat((rgb & 0x0000FF00) >> 8) / 255.0
            a = CGFloat(rgb & 0x000000FF) / 255.0

        } else {
            return nil
        }

        self.init(red: r, green: g, blue: b, opacity: a)
    }
}
