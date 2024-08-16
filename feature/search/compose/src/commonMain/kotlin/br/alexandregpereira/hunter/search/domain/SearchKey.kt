package br.alexandregpereira.hunter.search.domain

internal enum class SearchKey(
    val key: String,
    val valueType: SearchValueType = SearchValueType.String,
    val hasOnPreview: Boolean = false,
) {
    Name("-name", hasOnPreview = true),
    Type("-type", hasOnPreview = true),
    Cr("-cr", hasOnPreview = true, valueType = SearchValueType.Float),
    Spell("-spell"),
    Legendary("-legendary", valueType = SearchValueType.Boolean),
    Edited("-edited", valueType = SearchValueType.Boolean),
    Cloned("-cloned", valueType = SearchValueType.Boolean),
    Imported("-imported", valueType = SearchValueType.Boolean),
    Source("-source"),
}

internal enum class SearchValueType {
    String,
    Boolean,
    Float
}
