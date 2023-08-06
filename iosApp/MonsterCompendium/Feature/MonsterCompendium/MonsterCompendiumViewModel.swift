//
//  MonsterCompendiumViewModel.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 29/01/23.
//

import Foundation
import shared

@MainActor class MonsterCompendiumViewModel : ObservableObject {
    
    private let feature: MonsterCompendiumFeature

    @Published var isLoading: Bool = false
    @Published var state: MonsterCompendiumUiState = MonsterCompendiumUiState()
    @Published var compendiumIndex: Int = -1
    private var stateWatcher : Closeable? = nil
    private var actionWatcher : Closeable? = nil
    
    init() {
        feature = MonsterCompendiumFeature()
        stateWatcher = feature.state.collect { (state: MonsterCompendiumStateIos) -> Void in
            self.isLoading = state.isLoading
            self.state = state.asMonsterCompendiumUiState()
            self.state.alphabet = state.alphabet
            self.state.tableContent = state.asTableContentItemState()
            self.state.tableContentPopupScreenType = state.popupOpened && state.tableContentOpened ? .tableContent : (state.popupOpened ? .alphabetGrid : .circleLetter)
            self.state.alphabetSelectedIndex = Int(state.alphabetSelectedIndex)
            self.state.tableContentSelectedIndex = Int(state.tableContentIndex)
            self.state.tableContentInitialIndex = Int(state.tableContentInitialIndex)
        }
        actionWatcher = feature.action.collect{ (action: MonsterCompendiumActionIos) -> Void in
            guard let compendiumIndex = action.compendiumIndex else { return }
            self.compendiumIndex = compendiumIndex.intValue
        }
    }
    
    func getInitialScrollItemPosition() -> Int {
        return Int(feature.stateHolder.initialScrollItemPosition)
    }
    
    func onItemClick(index: String) {
        feature.stateHolder.onItemClick(index: index)
    }
    
    func onFirstVisibleItemChange(position: Int) {
        feature.stateHolder.onFirstVisibleItemChange(position: Int32(position))
    }

    func onPopupOpened() {
        feature.stateHolder.onPopupOpened()
    }

    func onPopupClosed() {
        feature.stateHolder.onPopupClosed()
    }

    func onAlphabetIndexClicked(position: Int) {
        feature.stateHolder.onAlphabetIndexClicked(position: Int32(position))
    }

    func onTableContentIndexClicked(position: Int) {
        feature.stateHolder.onTableContentIndexClicked(position: Int32(position))
    }
    
    deinit {
        stateWatcher?.close()
        actionWatcher?.close()
    }
}

extension MonsterCompendiumStateIos {
    
    func asMonsterCompendiumItemUiState() -> [MonsterCompendiumItemUiState] {
        self.items.map { item in
            if (item.title != nil) {
                return MonsterCompendiumItemUiState.title(MonsterCompendiumItemUiState.Title(value: item.title!.value, id: item.title!.id, isHeader: item.title!.isHeader))
            } else {
                return MonsterCompendiumItemUiState.item(MonsterCompendiumItemUiState.Item(value: item.monster!.asUiState()))
            }
        }
    }
    
    func asMonsterCompendiumUiState() -> MonsterCompendiumUiState {
        return MonsterCompendiumUiState(
            items: asMonsterCompendiumItemUiState()
        )
    }
    
    func asTableContentItemState() -> [TableContentItemState] {
        self.tableContent.map { itemIos in
            TableContentItemState(
                id: itemIos.id,
                text: itemIos.text,
                type: {
                    switch itemIos.type {
                    case Monster_compendiumTableContentItemType.header1:
                        return .HEADER1
                    case Monster_compendiumTableContentItemType.header2:
                        return .HEADER2
                    case Monster_compendiumTableContentItemType.body:
                        return .BODY
                    default:
                        return .BODY
                    }
                }()
            )
        }
    }
}

extension Monster {

    func asUiState() -> MonsterUiState {
        let monster = self
        return MonsterUiState(
            index: monster.index,
            name: monster.name,
            challengeRating: monster.challengeRating,
            imageUrl: monster.imageData.url,
            backgroundColorLight: monster.imageData.backgroundColor.light,
            backgroundColorDark: monster.imageData.backgroundColor.dark,
            isImageHorizontal: monster.imageData.isHorizontal
        )
    }
}
