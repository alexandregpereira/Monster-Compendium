package br.alexandregpereira.hunter.search

fun String.removeAccents(): String {
    val accentMappings = mapOf(
        'á' to 'a', 'é' to 'e', 'í' to 'i', 'ó' to 'o', 'ú' to 'u',
        'â' to 'a', 'ê' to 'e', 'î' to 'i', 'ô' to 'o', 'û' to 'u',
        'ã' to 'a', 'õ' to 'o', 'ç' to 'c',
        'à' to 'a', 'è' to 'e', 'ì' to 'i', 'ò' to 'o', 'ù' to 'u',
        'Á' to 'A', 'É' to 'E', 'Í' to 'I', 'Ó' to 'O', 'Ú' to 'U',
        'Â' to 'A', 'Ê' to 'E', 'Î' to 'I', 'Ô' to 'O', 'Û' to 'U',
        'Ã' to 'A', 'Õ' to 'O', 'Ç' to 'C',
        'À' to 'A', 'È' to 'E', 'Ì' to 'I', 'Ò' to 'O', 'Ù' to 'U'
    )

    return map { char -> accentMappings[char] ?: char }.joinToString("")
}
