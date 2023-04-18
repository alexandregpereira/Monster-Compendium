//
//  DamageIconColors.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 18/04/23.
//

import SwiftUI

struct DamageIconColors {
    let acid: Color
    let cold: Color
    let fire: Color
    let lightning: Color
    let necrotic: Color
    let poison: Color
    let psychic: Color
    let radiant: Color
    let thunder: Color
    let bludgeoning: Color
    let piercing: Color
    let slashing: Color
    
    init(isDark: Bool) {
        acid = Color(hex: isDark ? "#BCFC7D" : "#5E8636")!
        cold = Color(hex: isDark ? "#95F2F8" : "#00B7C2")!
        fire = Color(hex: isDark ? "#FF6060" : "#E90000")!
        lightning = Color(hex: isDark ? "#FFEB81" : "#FFD600")!
        necrotic = Color(hex: isDark ? "#D8D8D8" : "#000000")!
        poison = Color(hex: isDark ? "#DC8BF9" : "#6D3780")!
        psychic = Color(hex: isDark ? "#84DEC9" : "#00664E")!
        radiant = Color(hex: isDark ? "#F2A762" : "#FF7A00")!
        thunder = Color(hex: isDark ? "#A1BBE1" : "#6D6D6D")!
        bludgeoning = Color(hex: isDark ? "#E1A8A8" : "#613A3A")!
        piercing = Color(hex: isDark ? "#BAAFE2" : "#464058")!
        slashing = Color(hex: isDark ? "#9ECAEA" : "#364450")!
    }
}
