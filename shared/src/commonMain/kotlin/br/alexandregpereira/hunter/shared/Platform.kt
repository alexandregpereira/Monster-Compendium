package br.alexandregpereira.hunter.shared

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform