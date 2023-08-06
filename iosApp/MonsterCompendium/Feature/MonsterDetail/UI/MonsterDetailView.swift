//
//  MonsterDetailView.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 02/03/23.
//

import SwiftUI

struct MonsterDetailView : View {
    let monster: MonsterDetailItemUiState
    
    var body: some View {
        GeometryReader { geometry in
            let imageHeigh = geometry.size.height * 0.75
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
                            .padding(16)
                    }
                }
                .frame(width: geometry.size.width, height: imageHeigh, alignment: .center)
                .padding(.top, geometry.safeAreaInsets.top + 32)
                .id(monster.id)
                
                ScrollView {
                    LazyVStack(spacing: 1) {
                        Spacer().frame(maxWidth: .infinity)
                            .frame(height: imageHeigh)
                            .padding(.top, geometry.safeAreaInsets.top + 32)
                            .id(0)
                        
                        MonsterTitleView(title: monster.name, subTitle: monster.subtitle)
                            .background(Color.white)
                            .cornerRadius(10)
                            .id(1)
                        
                        MonsterInfoPart1(monster: monster)
                        
                        MonsterInfoPart2(monster: monster)
                        
                        MonsterInfoPart3(monster: monster)
                    }
                }
            }
        }
    }
}

struct MonsterInfoPart1: View {
    let monster: MonsterDetailItemUiState
    
    var body: some View {
        LoreBlock(text: monster.lore)
            .background(Color.white)
            .cornerRadius(10)
            .id(2)
        
        StatsBlock(stats: monster.stats)
            .background(Color.white)
            .cornerRadius(10)
            .id(3)
        
        SpeedBlock(speed: monster.speed)
            .background(Color.white)
            .cornerRadius(10)
            .id(4)
    
        AbilityScoreBlock(abilityScores: monster.abilityScores)
            .background(Color.white)
            .cornerRadius(10)
            .id(5)
    }
}

struct MonsterInfoPart2: View {
    let monster: MonsterDetailItemUiState
    
    var body: some View {
        ProficiencyBlock(
            title: NSLocalizedString("monster_detail_saving_throws", comment: "monster_detail_saving_throws"),
            proficiencies: monster.savingThrows.map{
                ProficiencyUiState(
                    index: $0.index,
                    modifier: $0.modifier,
                    name: NSLocalizedString("monster_detail_saving_throw_\($0.name.lowercased())", comment: "monster_detail_saving_throw_\($0.name.lowercased())")
                )
            }
        ).background(Color.white)
            .cornerRadius(10)
            .id(6)
        
        ProficiencyBlock(
            title: NSLocalizedString("monster_detail_skills", comment: "monster_detail_skills"),
            proficiencies: monster.skills
        ).background(Color.white)
            .cornerRadius(10)
            .id(7)
    }
}

struct MonsterInfoPart3: View {
    let monster: MonsterDetailItemUiState
    
    var body: some View {
        DamageBlock(title: NSLocalizedString("monster_detail_vulnerabilities", comment: "monster_detail_vulnerabilities"), damages: monster.damageVulnerabilities)
            .background(Color.white)
            .cornerRadius(10)
            .id(8)
        
        DamageBlock(title: NSLocalizedString("monster_detail_resistances", comment: "monster_detail_resistances"), damages: monster.damageResistances)
            .background(Color.white)
            .cornerRadius(10)
            .id(9)
        
        DamageBlock(title: NSLocalizedString("monster_detail_immunities", comment: "monster_detail_immunities"), damages: monster.damageImmunities)
            .background(Color.white)
            .cornerRadius(10)
            .id(10)
    }
}

struct LoreBlock: View {
    let text: String
    let onClick: () -> Void

    init(text: String, onClick: @escaping () -> Void = {}) {
        self.text = text
        self.onClick = onClick
    }

    var body: some View {
        Text(text)
            .font(.system(size: 14, weight: .light))
            .italic()
            .padding(.horizontal, 16)
            .padding(.bottom, 16)
            .padding(.top, 16)
            .onTapGesture {
                if text.hasSuffix("...") {
                    onClick()
                }
            }
            .frame(maxWidth: .infinity, alignment: .leading)
    }
}

struct StatsBlock: View {
    var stats: StatsUiState

    var body: some View {
        Block {
            StatsGrid(stats: stats)
        }
    }
}

private struct StatsGrid: View {
    var stats: StatsUiState

    var body: some View {
        HStack(spacing: 64) {
            IconInfo(
                image: Image(systemName: "shield.fill"),
                iconColor: .blue,
                iconAlpha: 1.0,
                title: NSLocalizedString("monster_detail_armor_class", comment: "monster_detail_armor_class"),
                iconText: "\(stats.armorClass)"
            )

            IconInfo(
                image: Image(systemName: "heart.fill"),
                iconColor: .red,
                iconAlpha: 1.0,
                title: stats.hitDice,
                iconText: "\(stats.hitPoints)"
            )
        }
        .frame(maxWidth: .infinity)
    }
}

struct SpeedBlock: View {
    let speed: SpeedUiState
    
    var body: some View {
        let prefixTitle = NSLocalizedString("monster_detail_speed_title", comment: "monster_detail_speed_title")
        let hoverText = NSLocalizedString("monster_detail_speed_hover", comment: "monster_detail_speed_hover")
        let title = speed.hover ? "\(prefixTitle) (\(hoverText))" : prefixTitle
        
        Block(title: title) {
            Grid(size: speed.values.count) { i in
                IconInfo(
                    image: Image(getImageName(speed.values[i].name)),
                    title: speed.values[i].valueFormatted
                )
            }
        }
    }
    
    private func getImageName(_ speedType: String) -> String {
        switch speedType.lowercased() {
        case "burrow":
            return "ghost"
        case "climb":
            return "climbing"
        case "fly":
            return "superhero"
        case "walk":
            return "running"
        case "swim":
            return "swimmer"
        default:
            return "running"
        }
    }
}

struct AbilityScoreBlock: View {
    let abilityScores: [AbilityScoreUiState]
    
    var body: some View {
        let title = NSLocalizedString("monster_detail_ability_scores", comment: "monster_detail_ability_scores")
        
        Block(title: title) {
            HStack(spacing: 48) {
                AbilityScoreView(abilityScore: abilityScores[0])
                AbilityScoreView(abilityScore: abilityScores[1])
                AbilityScoreView(abilityScore: abilityScores[2])
            }
            .padding(.bottom, 24)
            .frame(maxWidth: .infinity)
            
            HStack(spacing: 48) {
                AbilityScoreView(abilityScore: abilityScores[3])
                AbilityScoreView(abilityScore: abilityScores[4])
                AbilityScoreView(abilityScore: abilityScores[5])
            }
            .frame(maxWidth: .infinity)
        }
    }
    
}

struct DamageBlock: View {
    let title: String
    let damages: [DamageUiState]
    
    var body: some View {
        let damagesWithoutOtherType = damages.filter { $0.type.lowercased() != "other" }
        OptionalBlock(title: title, value: damages) { damages in
            Grid(size: damagesWithoutOtherType.count) { i in
                IconInfo(
                    image: Image(getImageName(damagesWithoutOtherType[i].type)),
                    iconColor: getImageColor(damagesWithoutOtherType[i].type),
                    iconAlpha: 1,
                    title: damagesWithoutOtherType[i].name
                )
            }
            
            ForEach(damages.filter { $0.type.lowercased() == "other" }, id: \.index) { damage in
                Text(damage.name)
                    .font(.system(size: 14, weight: .light))
                    .padding(.top, 16)
                    .padding(.horizontal, 16)
            }
        }
    }
    
    private func getImageName(_ type: String) -> String {
        return type.lowercased() == "necrotic" ? "human-skull" : type.lowercased()
    }
    
    private func getImageColor(_ type: String) -> Color {
        let colors = DamageIconColors(isDark: false)
            
        switch type.lowercased() {
        case "acid":
            return colors.acid
        case "bludgeoning":
            return colors.bludgeoning
        case "cold":
            return colors.cold
        case "fire":
            return colors.fire
        case "force":
            return .blue
        case "lightning":
            return colors.lightning
        case "necrotic":
            return colors.necrotic
        case "piercing":
            return colors.piercing
        case "poison":
            return colors.poison
        case "psychic":
            return colors.psychic
        case "radiant":
            return colors.radiant
        case "slashing":
            return colors.slashing
        case "thunder":
            return colors.thunder
        default:
            return .black
        }
    }
}

struct AbilityScoreView: View {
    let abilityScore: AbilityScoreUiState
    
    var body: some View {
        ZStack(alignment: .center) {
            Image("ic-ability-score")
                .resizable()
                .frame(width: 69, height: 89)
                .opacity(0.7)
            
            VStack(spacing: 1) {
                let name = NSLocalizedString("monster_detail_saving_throw_\(abilityScore.type.lowercased())", comment: "monster_detail_saving_throw_\(abilityScore.type.lowercased())")
                Text(String(name.prefix(3)).uppercased())
                    .font(.system(size: 12, weight: .regular))
                
                Text("\(abilityScore.value)")
                    .font(.system(size: 28, weight: .bold))
                
                let abilityScoreModifier = abilityScore.modifier > 0 ? "+\(abilityScore.modifier)" : abilityScore.modifier.description
                Text(abilityScoreModifier)
                    .font(.system(size: 12, weight: .regular))
                    .padding(.top, 4)
            }
            .frame(height: 89)
        }
    }
}

struct ProficiencyBlock: View {
    let title: String
    let proficiencies: [ProficiencyUiState]
    
    var body: some View {
        OptionalBlock(title: title, value: proficiencies) { proficiencies in
            Grid(size: proficiencies.count) { index in
                Bonus(
                    value: proficiencies[index].modifier,
                    name: proficiencies[index].name
                )
            }
        }
    }
}

struct Grid<Content: View>: View {
    let size: Int
    let content: (Int) -> Content
    
    var body: some View {
        VStack(spacing: 24) {
            let rowCount: Int = (size + 2) / 3
            ForEach(0..<rowCount, id: \.self) { rowIndex in
                HStack(spacing: 16) {
                    ForEach(0..<3) { columnIndex in
                        let index = rowIndex * 3 + columnIndex
                        if index < size {
                            content(index)
                                .padding(.horizontal, 16)
                                .padding(.top, rowIndex == 0 ? 0 : 8)
                                .padding(.bottom, rowIndex == 2 ? 0 : 8)
                        }
                    }
                }
            }
        }
        .frame(maxWidth: .infinity)
    }
}

struct Bonus: View {
    let value: Int
    let name: String
    let iconSize: CGFloat = 56
    let alpha: CGFloat = 0.7
    
    var body: some View {
        VStack {
            ZStack(alignment: .center) {
                BonusImage(size: iconSize)
                Text("+\(value)")
                    .fontWeight(.regular)
                    .font(.system(size: 18))
            }

            Text(name)
                .fontWeight(.regular)
                .font(.system(size: 14))
                .lineLimit(1)
                .padding(4)
        }
        .frame(maxWidth: 120)
        .opacity(alpha)
    }
}

struct BonusImage: View {
    let size: CGFloat
    
    var body: some View {
        ZStack {
            Circle()
                .stroke(lineWidth: 2)
                .foregroundColor(Color.black)

            Circle()
                .stroke(lineWidth: 1)
                .foregroundColor(Color.black)
                .padding(4)
        }
        .frame(width: size, height: size)
    }
}

struct BlockTitle: View {
    let title: String
    
    var body: some View {
        Text(title)
            .fontWeight(.regular)
            .font(.system(size: 18))
            .padding(.bottom, 24)
            .frame(maxWidth: .infinity, alignment: .leading)
    }
}

struct Block<Content: View>: View {
    let title: String?
    let content: () -> Content
    
    init(
        title: String? = nil,
        @ViewBuilder content: @escaping () -> Content
    ) {
        self.title = title
        self.content = content
    }
    
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            if let title = title {
                BlockTitle(title: title)
            }
            content()
                .frame(maxWidth: .infinity, alignment: .leading)
        }
        .frame(maxWidth: .infinity)
        .padding(16)
    }
}

struct OptionalBlock<Content: View, T>: View {
    let title: String?
    let value: T
    let content: (T) -> Content
    
    init(
        title: String? = nil,
        value: T,
        @ViewBuilder content: @escaping (T) -> Content
    ) {
        self.title = title
        self.value = value
        self.content = content
    }
    
    var body: some View {
        if let valueStr = value as? String, valueStr.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
            EmptyView()
        } else if let valueList = value as? [Any], valueList.isEmpty {
            EmptyView()
        } else {
            Block(title: title) {
                content(value)
            }
        }
    }
}

struct MonsterDetailView_Previews: PreviewProvider {
    static var previews: some View {
        MonsterDetailView(
            monster: MonsterDetailItemUiState(
                id: "sample_id",
                imageUrl: "https://raw.githubusercontent.com/alexandregpereira/hunter-api/main/images/aboleth.png",
                backgroundColorLight: "#ABCDEF",
                name: "Sample Monster",
                subtitle: "Sample Subtitle",
                stats: StatsUiState(
                    armorClass: 15,
                    hitPoints: 100,
                    hitDice: "10d10+30"
                ),
                speed: SpeedUiState(
                    hover: false,
                    values: [
                        SpeedValueUiState(name: "walk", valueFormatted: "30 ft.")
                    ]
                ),
                abilityScores: [
                    AbilityScoreUiState(type: "Strength", value: 18, modifier: 4),
                    AbilityScoreUiState(type: "Dexterity", value: 14, modifier: 2),
                    AbilityScoreUiState(type: "Constitution", value: 16, modifier: 3),
                    AbilityScoreUiState(type: "Constitution", value: 16, modifier: 3),
                    AbilityScoreUiState(type: "Constitution", value: 16, modifier: 3),
                    AbilityScoreUiState(type: "Constitution", value: 16, modifier: 3)
                ],
                savingThrows: [
                    SavingThrowUiState(index: "0", modifier: 5, name: "Constitution"),
                    SavingThrowUiState(index: "0", modifier: 5, name: "Strength"),
                    SavingThrowUiState(index: "0", modifier: 5, name: "Strength"),
                    SavingThrowUiState(index: "0", modifier: 5, name: "Strength"),
                    SavingThrowUiState(index: "1", modifier: 3, name: "Dexterity")
                ],
                skills: [
                    ProficiencyUiState(index: "0", modifier: 6, name: "Athletics"),
                    ProficiencyUiState(index: "1", modifier: 4, name: "Acrobatics")
                ],
                damageVulnerabilities: [
                    DamageUiState(index: "0", type: "FIRE", name: "Fire"),
                    DamageUiState(index: "1", type: "OTHER", name: "Something"),
                    DamageUiState(index: "2", type: "OTHER", name: "Something more detailed")
                ],
                damageResistances: [
                    DamageUiState(index: "1", type: "COLD", name: "Cold")
                ],
                damageImmunities: [
                    DamageUiState(index: "2", type: "POISON", name: "Poison")
                ],
                conditionImmunities: [
                    ConditionUiState(index: "0", name: "Charmed")
                ],
                senses: [
                    "darkvision 60 ft.",
                    "passive Perception 12"
                ],
                languages: "Common, Draconic",
                specialAbilities: [
                    AbilityDescriptionUiState(name: "Sample Ability", description: "Sample ability description.")
                ],
                actions: [
                    ActionUiState(
                        damageDices: [
                            DamageDiceUiState(dice: "1d6", damage: DamageUiState(index: "0", type: "FIRE", name: "Bludgeoning"))
                        ],
                        attackBonus: 5,
                        abilityDescription: AbilityDescriptionUiState(name: "Sample Attack", description: "Sample attack description.")
                    )
                ],
                legendaryActions: [],
                reactions: [],
                spellcastings: [
                    SpellcastingUiState(
                        name: "Sample Spellcasting",
                        description: "Sample spellcasting description.",
                        spellsByGroup: [
                            "Cantrips": [
                                SpellPreviewUiState(index: "0", name: "Fire Bolt", school: "Evocation"),
                                SpellPreviewUiState(index: "1", name: "Mage Hand", school: "Conjuration")
                            ],
                            "1st Level": [
                                SpellPreviewUiState(index: "2", name: "Magic Missile", school: "Evocation"),
                                SpellPreviewUiState(index: "3", name: "Shield", school: "Abjuration")
                            ]
                        ]
                    )
                ],
                lore: "Long ago, in the heart of the Enchanted Forest, a legendary creature was born. Its name was whispered among the trees: the fearsome and mysterious Shadowbeast. Feared by many, it prowled the woods at night, leaving no trace behind except its chilling howl. For centuries..."
            )
        )
        .previewLayout(.sizeThatFits)
    }
}
