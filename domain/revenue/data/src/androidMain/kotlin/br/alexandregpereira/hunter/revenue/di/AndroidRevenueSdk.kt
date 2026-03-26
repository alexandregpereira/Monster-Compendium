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

package br.alexandregpereira.hunter.revenue.di

import br.alexandregpereira.hunter.analytics.BuildConfig
import br.alexandregpereira.hunter.revenue.RevenueSdk
import br.alexandregpereira.hunter.revenue.RevenueSdkException
import com.revenuecat.purchases.kmp.LogLevel
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.configure
import com.revenuecat.purchases.kmp.models.CustomerInfo
import com.revenuecat.purchases.kmp.models.Package
import com.revenuecat.purchases.kmp.models.PurchasesError
import com.revenuecat.purchases.kmp.models.StoreTransaction
import kotlinx.coroutines.suspendCancellableCoroutine

internal class AndroidRevenueSdk : RevenueSdk {

    override fun initialize(apiKey: String) {
        if (BuildConfig.DEBUG) {
            Purchases.logLevel = LogLevel.DEBUG
        }
        Purchases.configure(apiKey = apiKey)
    }

    override suspend fun isPremiumEnabled(): Boolean {
        return suspendCancellableCoroutine { continuation ->
            val purchases = try {
                Purchases.sharedInstance
            } catch (cause: Throwable) {
                val exception = RevenueSdkException.FailToInitializeRevenueSdk(cause)
                continuation.resumeWith(Result.failure(exception))
                return@suspendCancellableCoroutine
            }
            purchases.getCustomerInfo(
                onError = { error ->
                    val exception = RevenueSdkException.FailToVerifyPremium(
                        code = error.code.toString(),
                        message = "${error.message} " +
                                "underlyingErrorMessage=${error.underlyingErrorMessage}",
                    )
                    continuation.resumeWith(Result.failure(exception))
                },
                onSuccess = { customerInfo ->
                    val isPremium = customerInfo.entitlements["D&D 5e Monster Compendium Pro"]?.isActive == true
                    continuation.resumeWith(Result.success(isPremium))
                }
            )
        }
    }

    override suspend fun purchase() {
        val packageToPurchase = getOfferings()
        suspendCancellableCoroutine<Unit> { continuation ->
            Purchases.sharedInstance.purchase(
                packageToPurchase = packageToPurchase,
                onError = { error: PurchasesError, userCancelled: Boolean ->
                    // Handle error
                    continuation.resumeWith(
                        Result.failure(
                            RevenueSdkException.FailToVerifyPremium(
                                code = error.code.toString(),
                                message = "${error.message} " +
                                        "underlyingErrorMessage=${error.underlyingErrorMessage}",
                            )
                        )
                    )
                },
                onSuccess = { storeTransaction: StoreTransaction, customerInfo: CustomerInfo ->
                    if (customerInfo.entitlements["D&D 5e Monster Compendium Pro"]?.isActive == true) {
                        continuation.resumeWith(Result.success(Unit))
                    }
                }
            )
        }
    }

    private suspend fun getOfferings(): Package {
        return suspendCancellableCoroutine { continuation ->
            Purchases.sharedInstance.getOfferings(
                onError = { error ->
                    // Handle error
                    continuation.resumeWith(
                        Result.failure(
                            RevenueSdkException.FailToVerifyPremium(
                                code = error.code.toString(),
                                message = "${error.message} " +
                                        "underlyingErrorMessage=${error.underlyingErrorMessage}",
                            )
                        )
                    )
                },
                onSuccess = { offerings ->
                    val offering = offerings.current ?: return@getOfferings
                    val packageToPurchase =
                        offering.availablePackages.firstOrNull() ?: return@getOfferings
                    // Purchase the package
                    continuation.resumeWith(Result.success(packageToPurchase))
                }
            )
        }
    }
}
