package br.alexandregpereira.hunter.revenue

interface IsPremium {
    suspend operator fun invoke(ignoreCache: Boolean = false): Boolean
}

fun IsPremium(isPremium: () -> Boolean): IsPremium = object : IsPremium {
    override suspend fun invoke(ignoreCache: Boolean): Boolean = isPremium()
}
