/*
 * Copyright (C) 2026 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.revenue

internal interface RevenueSdk {

    fun initialize(apiKey: String)

    @Throws(RevenueSdkException::class)
    suspend fun isPremiumEnabled(): Boolean

    suspend fun purchase()
}

internal class PremiumRevenueSdk : RevenueSdk {
    override fun initialize(apiKey: String) {}
    override suspend fun isPremiumEnabled(): Boolean = true
    override suspend fun purchase() {}
}

internal sealed class RevenueSdkException(
    message: String,
    cause: Throwable? = null
) : Throwable(message, cause) {

    class FailToVerifyPremium(code: String, message: String) : RevenueSdkException(
        message = "Error verifying that user isPremium. Code: $code, Message: $message",
    )

    class FailToInitializeRevenueSdk(cause: Throwable) : RevenueSdkException(
        message = "Error initializing Revenue SDK",
        cause = cause,
    )
}
