package br.alexandregpereira.file

enum class FileType(
    val folder: String
) {
    IMAGE(folder = "images"),
    ZIP(folder = "content"),
}
