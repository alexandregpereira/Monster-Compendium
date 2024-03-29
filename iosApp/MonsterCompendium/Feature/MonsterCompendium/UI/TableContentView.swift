//
//  TableContentView.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 08/03/23.
//

import shared
import SwiftUI

struct TableContent: View {
    let items: [TableContentItem]
    let selectedIndex: Int
    let onTap: (Int) -> Void
    let initialIndex: Int

    var body: some View {
        ScrollViewReader { proxy in
            ScrollView(.horizontal, showsIndicators: false) {
                LazyHGrid(rows: [GridItem(.adaptive(minimum: 56))], spacing: 8) {
                    ForEach(items, id: \.id) { item in
                        let index = items.firstIndex(where: { $0.id == item.id }) ?? 0
                        let color = selectedIndex == index ? Color.gray : Color.clear
                        
                        Button(action: {
                            onTap(index)
                        }) {
                            VStack(alignment: .leading, spacing: 8) {
                                Text(item.text)
                                    .font(fontForType(item.type))
                                    .foregroundColor(.black)
                                    .lineLimit(nil)
                                    .fixedSize(horizontal: false, vertical: true)
                                    .multilineTextAlignment(.leading)
                                    .frame(maxWidth: .infinity, alignment: .leading)
                            }
                            .frame(width: 120)
                            .padding(16)
                            .background(color)
                            .cornerRadius(16)
                            .padding(.vertical, 8)
                        }
                    }
                }
                .padding(.horizontal, 16)
                .padding(.vertical, 8)
                .onAppear() {
                    withAnimation {
                        proxy.scrollTo(items[initialIndex].id, anchor: .leading)
                    }
                }
            }
            .padding(8)
            .background(Color.white)
            .cornerRadius(16)
            .shadow(color: Color.black.opacity(0.2), radius: 4, x: 0, y: 2)
        }
    }
    
    func fontForType(_ type: TableContentItemType) -> Font {
        switch type {
        case .header1:
            return .system(size: 32, weight: .bold)
        case .header2:
            return .system(size: 18, weight: .bold)
        default:
            return .system(size: 14, weight: .regular)
        }
    }
}

struct TableContent_Previews: PreviewProvider {
    static var items: [TableContentItem] = {
        var items = [TableContentItem]()
        for i in 0..<50 {
            let type = i % 3 == 0 ? TableContentItemType.header1 : (i % 3 == 1 ? TableContentItemType.header2 : TableContentItemType.body)
            let text = "Item \(i)"
            let item = TableContentItem(text: text, type: type, id: text)
            items.append(item)
        }
        return items
    }()
    
    static var previews: some View {
        TableContent(items: items, selectedIndex: 2, onTap: { index in
            print("Tapped item at index \(index)")
        }, initialIndex: 0)
    }
}
