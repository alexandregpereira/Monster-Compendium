package br.alexandregpereira.hunter.strings

fun String.format(vararg args: Any): String {
    return args.foldIndexed(this) { index, string, arg ->
        string.replace("{$index}", arg.toString())
    }
}

fun String.formatWithPlural(arg: Int, plural: String): String {
    val string = if (arg != 1 && plural.isNotBlank()) plural else this
    return string.format(arg)
}
