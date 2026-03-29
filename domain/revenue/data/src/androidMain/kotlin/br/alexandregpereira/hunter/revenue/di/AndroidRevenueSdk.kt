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
import br.alexandregpereira.hunter.revenue.Offer
import br.alexandregpereira.hunter.revenue.OfferPeriod
import br.alexandregpereira.hunter.revenue.RevenueSdk
import br.alexandregpereira.hunter.revenue.RevenueSdkException
import com.revenuecat.purchases.kmp.LogLevel
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.configure
import com.revenuecat.purchases.kmp.models.CustomerInfo
import com.revenuecat.purchases.kmp.models.Package
import com.revenuecat.purchases.kmp.models.PackageType
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
                    val isPremium = customerInfo.hasPremiumPurchase()
                    continuation.resumeWith(Result.success(isPremium))
                }
            )
        }
    }

    override suspend fun purchase(offerId: String) {
        val packageToPurchase = getOfferings().firstOrNull {
            it.identifier == offerId
        } ?: throw RevenueSdkException.FailToPurchase(
            code = "OfferNotFound",
            message = "Offer with id $offerId not found",
        )
        suspendCancellableCoroutine { continuation ->
            Purchases.sharedInstance.purchase(
                packageToPurchase = packageToPurchase,
                onError = { error: PurchasesError, userCancelled: Boolean ->
                    if (userCancelled) {
                        continuation.cancel()
                    } else {
                        continuation.resumeWith(
                            Result.failure(
                                RevenueSdkException.FailToPurchase(
                                    code = error.code.toString(),
                                    message = "${error.message} " +
                                            "underlyingErrorMessage=${error.underlyingErrorMessage}",
                                )
                            )
                        )
                    }
                },
                onSuccess = { _: StoreTransaction, customerInfo: CustomerInfo ->
                    if (customerInfo.hasPremiumPurchase()) {
                        continuation.resumeWith(Result.success(Unit))
                    } else {
                        continuation.resumeWith(
                            Result.failure(
                                RevenueSdkException.FailToPurchase(
                                    code = "PurchaseNotActive",
                                    message = "Purchase completed but no active subscription found",
                                )
                            )
                        )
                    }
                }
            )
        }
    }

    override suspend fun restorePurchase() {
        suspendCancellableCoroutine { continuation ->
            Purchases.sharedInstance.restorePurchases(
                onError = { error ->
                    continuation.resumeWith(
                        Result.failure(
                            RevenueSdkException.FailToRestorePurchase(
                                code = error.code.toString(),
                                message = "${error.message} " +
                                        "underlyingErrorMessage=${error.underlyingErrorMessage}",
                            )
                        )
                    )
                },
                onSuccess = { customerInfo ->
                    if (customerInfo.hasPremiumPurchase()) {
                        continuation.resumeWith(Result.success(Unit))
                    } else {
                        continuation.resumeWith(
                            Result.failure(
                                RevenueSdkException.FailToRestorePurchase(
                                    code = "NoActiveSubscription",
                                    message = "No active subscription found during restore purchase",
                                )
                            )
                        )
                    }
                }
            )
        }
    }

    override suspend fun getOffers(): List<Offer> {
        return getOfferings().mapNotNull { pkg ->
            val period = when (pkg.packageType) {
                PackageType.ANNUAL -> OfferPeriod.YEARLY
                PackageType.MONTHLY -> OfferPeriod.MONTHLY
                PackageType.WEEKLY -> OfferPeriod.WEEKLY
                else -> null
            }
            period?.let {
                Offer(
                    id = pkg.identifier,
                    value = pkg.storeProduct.price.formatted,
                    period = it,
                )
            }
        }
    }

    private suspend fun getOfferings(): List<Package> {
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
                    val offering = offerings.current
                    val packages = offering?.availablePackages.orEmpty()
                    continuation.resumeWith(Result.success(packages))
                }
            )
        }
    }

    private fun CustomerInfo.hasPremiumPurchase(): Boolean {
        return entitlements["D&D 5e Monster Compendium Pro"]?.isActive == true
    }
}
