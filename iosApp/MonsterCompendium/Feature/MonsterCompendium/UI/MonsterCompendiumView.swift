//
//  MonsterCompendiumView.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 29/01/23.
//

import shared
import SwiftUI

struct MonsterCompendiumView: View {
    
    let items: [MonsterCompendiumItemState]
    let initialCompendiumIndex: Int
    let compendiumIndex: Int
    let onMonsterItemClick: (String) -> Void
    let onFirstVisibleIndexChange: ((Int) -> Void)?
    
    init(
        items: [MonsterCompendiumItemState],
        initialCompendiumIndex: Int = 0,
        compendiumIndex: Int = -1,
        onMonsterItemClick: @escaping (String) -> Void,
        onFirstVisibleIndexChange: ((Int) -> Void)? = nil
    ) {
        self.items = items
        self.initialCompendiumIndex = initialCompendiumIndex
        self.compendiumIndex = compendiumIndex
        self.onMonsterItemClick = onMonsterItemClick
        self.onFirstVisibleIndexChange = onFirstVisibleIndexChange
    }
    
    var body: some View {
        GeometryReader { gridProxy in
            ScrollViewReader { scrollProxy in
                ScrollView {
                    let gridItems = [
                        GridItem(.fixed(UIScreen.main.bounds.size.width / 2), spacing: 0, alignment: .leading),
                        GridItem(.fixed(UIScreen.main.bounds.size.width / 2), spacing: 0, alignment: .leading)
                    ]
                    
                    LazyVGrid(columns: gridItems, spacing: 0) {
                        ForEach(Array(items.enumerated()), id: \.element.key) { index, item in
                            MonsterCompendiumItemView(item: item, onMonsterItemClick: onMonsterItemClick)
                                .id(item.key)
                                .background(GeometryReader { itemProxy in
                                    let gridTop = gridProxy.frame(in: .global).minY
                                    let itemTop = itemProxy.frame(in: .global).minY
                                    let itemBottom = itemProxy.frame(in: .global).maxY
                                    
                                    if itemTop <= gridTop && itemBottom >= gridTop {
                                        Color.clear.preference(key: FirstVisibleItemIndexKey.self, value: index)
                                    } else {
                                        Color.clear
                                    }
                                })
                        }
                    }
                    .onPreferenceChange(FirstVisibleItemIndexKey.self) { newIndex in
                        if let newIndex = newIndex {
                            onFirstVisibleIndexChange?(newIndex)
                        }
                    }
                }
                .onChange(of: compendiumIndex) { newIndex in
                    guard newIndex >= 0 else { return }
                    withAnimation {
                        scrollProxy.scrollTo(items[newIndex].key, anchor: .top)
                    }
                }
                .onAppear {
                    scrollProxy.scrollTo(items[initialCompendiumIndex].key, anchor: .top)
                }
            }
        }
    }
}

struct MonsterCompendiumItemView: View {
    
    let item: MonsterCompendiumItemState
    let onMonsterItemClick: (String) -> Void
    
    var body: some View {
        ZStack(alignment: .leading) {
            switch item {
            case let title as MonsterCompendiumItemState.Title:
                let font = title.isHeader ? Font.system(size: 48) : Font.title
                let bottomPadding = title.isHeader ? 32.0 : 16
                Text(title.value)
                    .font(font)
                    .padding(EdgeInsets(top: 40, leading: 16, bottom: bottomPadding, trailing: 16))
                    .frame(maxWidth: .infinity, alignment: .leading)
            case let item as MonsterCompendiumItemState.Item:
                let monster = item.monster
                MonsterCardView(monster: monster)
                    .padding(EdgeInsets(top: 16, leading: 8, bottom: 16, trailing: 8))
                    .onTapGesture {
                        onMonsterItemClick(monster.index)
                    }
            default:
                EmptyView()
            }
        }
        .frame(
            width: item.isHorizontal() ? UIScreen.main.bounds.size.width : (UIScreen.main.bounds.size.width) / 2
        )
        
        if item.isHorizontal() { Color.clear }
    }
}

struct FirstVisibleItemIndexKey: PreferenceKey {
    static var defaultValue: Int? = nil
    
    static func reduce(value: inout Int?, nextValue: () -> Int?) {
        value = value ?? nextValue()
    }
}

struct MonsterCompendiumView_Previews: PreviewProvider {
    static var previews: some View {
        MonsterCompendiumView(items: MonsterCompendiumItemState.sampleData, onMonsterItemClick: { print($0) })
    }
}
