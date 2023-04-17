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
                .frame(width: geometry.size.width, height: 600, alignment: .center)
                .padding(.top, geometry.safeAreaInsets.top + 32)
                .id(monster.id)
                
                ScrollView {
                    LazyVStack(spacing: 1) {
                        Spacer().frame(maxWidth: .infinity)
                            .frame(height: 600)
                            .padding(.top, geometry.safeAreaInsets.top + 32)
                            .id(0)
                        
                        MonsterTitleView(title: monster.name, subTitle: monster.subtitle)
                            .background(Color.white)
                            .cornerRadius(10)
                            .id(1)
                        
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
            }
        }
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
    }
}

struct SpeedBlock: View {
    let speed: SpeedUiState
    
    var body: some View {
        let prefixTitle = NSLocalizedString("monster_detail_speed_title", comment: "monster_detail_speed_title")
        let hoverText = NSLocalizedString("monster_detail_speed_hover", comment: "monster_detail_speed_hover")
        let title = speed.hover ? "\(prefixTitle) (\(hoverText))" : prefixTitle
        
        Block(title: title) {
            HStack(alignment: .center, spacing: 32) {
                ForEach(speed.values, id: \.name) { speedValue in
                    let image = getImageName(speedValue.name)
                    IconInfo(image: Image(image), title: speedValue.valueFormatted)
                }
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
            VStack {
                HStack(spacing: 24) {
                    AbilityScore(abilityScore: abilityScores[0])
                    AbilityScore(abilityScore: abilityScores[1])
                    AbilityScore(abilityScore: abilityScores[2])
                }
                .padding(.bottom, 24)
                
                HStack(spacing: 24) {
                    AbilityScore(abilityScore: abilityScores[3])
                    AbilityScore(abilityScore: abilityScores[4])
                    AbilityScore(abilityScore: abilityScores[5])
                }
            }
        }
    }
    
}

struct AbilityScore: View {
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
                .frame(maxWidth: .infinity)
        }
        .frame(maxWidth: .infinity)
        .padding(16)
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
                    SavingThrowUiState(index: "0", modifier: 5, name: "Strength"),
                    SavingThrowUiState(index: "1", modifier: 3, name: "Dexterity")
                ],
                skills: [
                    ProficiencyUiState(index: "0", modifier: 6, name: "Athletics"),
                    ProficiencyUiState(index: "1", modifier: 4, name: "Acrobatics")
                ],
                damageVulnerabilities: [
                    DamageUiState(index: "0", name: "Fire")
                ],
                damageResistances: [
                    DamageUiState(index: "1", name: "Cold")
                ],
                damageImmunities: [
                    DamageUiState(index: "2", name: "Poison")
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
                            DamageDiceUiState(dice: "1d6", damage: DamageUiState(index: "0", name: "Bludgeoning"))
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
