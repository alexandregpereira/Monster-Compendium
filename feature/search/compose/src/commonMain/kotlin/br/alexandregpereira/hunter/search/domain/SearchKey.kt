package br.alexandregpereira.hunter.search.domain

internal enum class SearchKey(
    val key: String,
    val valueType: SearchValueType = SearchValueType.String,
    val hasOnPreview: Boolean = false,
) {
    Name("-name", hasOnPreview = true),
    Cr("-challenge", hasOnPreview = true, valueType = SearchValueType.Float),
    Legendary("-legendaryMonster", valueType = SearchValueType.Boolean),
    Spellcaster("-spellcaster", valueType = SearchValueType.Boolean),
    Spell("-spellName"),
    Lore("-lore"),
    Type("-type", hasOnPreview = true),
    Edited("-edited", valueType = SearchValueType.Boolean, hasOnPreview = true),
    Cloned("-cloned", valueType = SearchValueType.Boolean, hasOnPreview = true),
    Imported("-imported", valueType = SearchValueType.Boolean, hasOnPreview = true),
    Fly("-fly", valueType = SearchValueType.Boolean),
    Burrow("-burrow", valueType = SearchValueType.Boolean),
    Hover("-hover", valueType = SearchValueType.Boolean),
    Swim("-swim", valueType = SearchValueType.Boolean),
    Group("-group", hasOnPreview = true),
    Alignment("-alignment", hasOnPreview = true),
    Size("-size", hasOnPreview = true),
    Source("-source", hasOnPreview = true),
    DamageVulnerability("-damageVulnerability"),
    DamageResistance("-damageResistance"),
    DamageImmunity("-damageImmunity"),
    SpecialAbility("-specialAbility"),
    Action("-action"),
    LegendaryAction("-legendaryAction"),
    Senses("-senses", hasOnPreview = true),
    ImagePortrait("-imagePortrait", valueType = SearchValueType.Boolean, hasOnPreview = true),
    ImageLandscape("-imageHorizontal", valueType = SearchValueType.Boolean, hasOnPreview = true),
    ImageUrl("-imageUrl", hasOnPreview = true),
}

internal enum class SearchValueType {
    String,
    Boolean,
    Float
}
