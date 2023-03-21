//
//  TableContentPopup.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 08/03/23.
//

import SwiftUI

struct TableContentPopup: View {
    let tableContent: [TableContentItemState]
    let alphabet: [String]
    let alphabetSelectedIndex: Int
    let tableContentSelectedIndex: Int
    let tableContentInitialIndex: Int
    let screen: ScreenType
    let onOpenButtonClicked: () -> Void
    let onCloseButtonClicked: () -> Void
    let onTableContentClicked: (Int) -> Void
    let onAlphabetIndexClicked: (Int) -> Void
    
    enum ScreenType {
        case circleLetter
        case alphabetGrid
        case tableContent
    }
    
    var body: some View {
        ZStack(alignment: .bottomTrailing) {
            
            ZStack(alignment: .bottomTrailing) {
                if screen == .circleLetter {
                    CircleLetter(
                        letter: alphabet[alphabetSelectedIndex],
                        onClick: onOpenButtonClicked
                    )
                    .transition(.circleLetterTransition)
                } else {
                    ZStack(alignment: .bottomTrailing) {
                        if screen == .alphabetGrid {
                            AlphabetGrid(
                                alphabet: alphabet,
                                selectedIndex: alphabetSelectedIndex,
                                onAlphabetIndexClicked: onAlphabetIndexClicked
                            )
                            .transition(.alphabetGridTransition)
                        } else {
                            TableContent(
                                items: tableContent,
                                selectedIndex: tableContentSelectedIndex,
                                onTap: onTableContentClicked,
                                initialIndex: tableContentInitialIndex
                            )
                            .transition(.tableContentTransition)
                        }
                        
                        CloseButton(backgroundColor: Color.gray, onClick: onCloseButtonClicked)
                            .opacity(screen == .alphabetGrid || screen == .tableContent ? 1 : 0)
                    }
                    .frame(
                        minWidth: 0,
                        maxWidth: .infinity,
                        minHeight: 0,
                        maxHeight: .infinity,
                        alignment: .bottomTrailing
                    )
                    .transition(.tableContentPopupTransition)
                }
            }
            .animation(.spring(), value: screen)
        }
        .padding(16)
        .frame(
            minWidth: 0,
            maxWidth: .infinity,
            minHeight: 0,
            maxHeight: .infinity,
            alignment: .bottomTrailing
        )
    }
}

private extension AnyTransition {
    
    static var tableContentPopupTransition: AnyTransition {
        .asymmetric(
            insertion: .scale(
                scale: 0,
                anchor: .bottomTrailing
            )
            .animation(.spring(response: 0.2, dampingFraction: 0.6))
            .combined(
                with: .fadeIn()
            ),
            removal: scale(
                scale: 0,
                anchor: .bottomTrailing
            )
            .animation(.spring(response: 0.2, dampingFraction: 1))
            .combined(
                with: .fadeOut()
            )
        )
    }

    static var tableContentTransition: AnyTransition {
        .modifier(
            active: ScaleModifier(scale: CGSize(width: 1, height: 0.3), anchor: .bottom),
            identity: ScaleModifier(scale: CGSize(width: 1, height: 1), anchor: .bottom)
        )
        .animation(.spring(response: 0.2, dampingFraction: 0.6))
        .combined(with: .fadeIn())
    }

    
    static var alphabetGridTransition: AnyTransition {
        .modifier(
            active: ScaleModifier(scale: CGSize(width: 1, height: 2), anchor: .bottom),
            identity: ScaleModifier(scale: CGSize(width: 1, height: 1), anchor: .bottom)
        )
        .animation(.spring(response: 0.2, dampingFraction: 1))
        .combined(with: .fadeOut())
    }
    
    static var circleLetterTransition: AnyTransition {
        .asymmetric(
            insertion: .scale(
                scale: 5,
                anchor: .bottomTrailing
            )
            .animation(.spring(response: 0.2, dampingFraction: 0.6))
            .combined(
                with: .opacity.animation(.spring(response: 0.2, dampingFraction: 1))
            ),
            removal: scale(
                scale: 5,
                anchor: .bottomTrailing
            )
            .animation(.spring(response: 0.2, dampingFraction: 1))
            .combined(
                with: .fadeOut()
            )
        )
    }
    
    static func fadeOut(response: Double = 0.1) -> AnyTransition {
        .opacity.animation(.spring(response: response, dampingFraction: 1))
    }
    
    static func fadeIn(response: Double = 0.2) -> AnyTransition {
        .opacity.animation(.spring(response: response, dampingFraction: 1))
    }

    struct ScaleModifier: ViewModifier {
        let scale: CGSize
        let anchor: UnitPoint

        func body(content: Content) -> some View {
            content
                .scaleEffect(scale, anchor: anchor)
        }
    }
}

private struct AlphabetGrid: View {
    let alphabet: [String]
    let selectedIndex: Int
    let onAlphabetIndexClicked: (Int) -> Void
    
    var body: some View {
        LazyVGrid(columns: [
            GridItem(.flexible()),
            GridItem(.flexible()),
            GridItem(.flexible()),
            GridItem(.flexible()),
            GridItem(.flexible())
        ], alignment: .center, spacing: 16) {
            ForEach(Array(alphabet.indices), id: \.self) { index in
                let letter = alphabet[index]
                let color = selectedIndex == index ? Color.gray : Color.clear
                
                Button(action: {
                    onAlphabetIndexClicked(index)
                }) {
                    ZStack {
                        Circle()
                            .foregroundColor(color)
                            .frame(width: 40, height: 40)
                        Text(letter)
                            .font(.system(size: 16))
                            .foregroundColor(Color.black)
                    }
                }
            }
        }
        .padding(8)
        .background(Color.white)
        .cornerRadius(16)
        .shadow(color: Color.black.opacity(0.2), radius: 4, x: 0, y: 2)
    }
}

private struct CircleLetter: View {
    let letter: String
    let onClick: () -> Void
    
    var body: some View {
        Button(action: onClick) {
            ZStack {
                Circle()
                    .foregroundColor(.black)
                    .frame(width: 56, height: 56)
                    .shadow(radius: 2, y: 2)
                Text(letter)
                    .font(.system(size: 24))
                    .foregroundColor(.white)
                    .padding(.bottom, 4)
            }
        }
    }
}

private struct CloseButton: View {
    let backgroundColor: Color
    let onClick: () -> Void
    
    var body: some View {
        Button(action: onClick) {
            ZStack {
                Circle()
                    .foregroundColor(backgroundColor)
                    .frame(width: 40, height: 40)
                Image(systemName: "xmark")
                    .foregroundColor(Color.white.opacity(0.7))
                    .font(.system(size: 24))
            }
        }
        .padding(.bottom, 16)
        .padding(.trailing, 16)
        .padding(.top, 8)
    }
}


struct AlphabetGrid_Previews: PreviewProvider {
    static let alphabet = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"]
    
    static var previews: some View {
        AlphabetGrid(alphabet: alphabet, selectedIndex: 2, onAlphabetIndexClicked: { index in
            print("Selected alphabet index: \(index)")
        })
    }
}

struct CircleLetter_Previews: PreviewProvider {
    static var previews: some View {
        CircleLetter(letter: "A", onClick: {
            print("CircleLetter tapped!")
        })
        .padding()
        .previewLayout(.fixed(width: 100, height: 100))
    }
}

struct CloseButton_Previews: PreviewProvider {
    static var previews: some View {
        CloseButton(backgroundColor: .gray, onClick: {
            print("Close button tapped!")
        })
        .previewLayout(.sizeThatFits)
    }
}

struct TableContentPopup_Previews: PreviewProvider {
    static let alphabet = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"]

    static var items: [TableContentItemState] = {
        var items = [TableContentItemState]()
        for i in 0..<50 {
            let type = i % 3 == 0 ? TableContentItemTypeState.HEADER1 : (i % 3 == 1 ? TableContentItemTypeState.HEADER2 : TableContentItemTypeState.BODY)
            let text = "Item \(i)"
            let item = TableContentItemState(id: text, text: text, type: type)
            items.append(item)
        }
        return items
    }()
    
    @State static var screenType: TableContentPopup.ScreenType = .alphabetGrid
    
    static var previews: some View {
        
        TableContentPopup(
            tableContent: items,
            alphabet: alphabet,
            alphabetSelectedIndex: 0,
            tableContentSelectedIndex: 0,
            tableContentInitialIndex: 0,
            screen: screenType,
            onOpenButtonClicked: {
                withAnimation {
                    screenType = .alphabetGrid
                }
            },
            onCloseButtonClicked: {
                screenType = .circleLetter
            },
            onTableContentClicked: {_ in
                withAnimation {
                    screenType = .circleLetter
                }
            },
            onAlphabetIndexClicked: {_ in screenType = .tableContent }
                
        )
    }
}
