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

package br.alexandregpereira.hunter.revenue.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.revenuecat.purchases.kmp.models.CustomerInfo
import com.revenuecat.purchases.kmp.models.PurchasesError
import com.revenuecat.purchases.kmp.models.StoreTransaction
import com.revenuecat.purchases.kmp.ui.revenuecatui.Paywall
import com.revenuecat.purchases.kmp.ui.revenuecatui.PaywallListener
import com.revenuecat.purchases.kmp.ui.revenuecatui.PaywallOptions

@Composable
internal actual fun RevenueCatPaywall(
    shouldDisplayDismissButton: Boolean,
    onDismiss: () -> Unit,
    onPurchaseCompleted: () -> Unit,
    onPurchaseError: (message: String) -> Unit,
    modifier: Modifier,
) {
    val paywallOptions = remember(shouldDisplayDismissButton) {
        val listener = object : PaywallListener {
            override fun onPurchaseCompleted(
                customerInfo: CustomerInfo,
                storeTransaction: StoreTransaction
            ) {
                onPurchaseCompleted()
            }

            override fun onPurchaseError(error: PurchasesError) {
                onPurchaseError(error.message)
            }

            override fun onRestoreCompleted(customerInfo: CustomerInfo) {
                // Check if the user now has premium entitlements after restore
                val isPremium = customerInfo.entitlements["D&D 5e Monster Compendium Pro"]?.isActive == true
                if (isPremium) {
                    onPurchaseCompleted()
                }
            }

            override fun onRestoreError(error: PurchasesError) {
                onPurchaseError(error.message)
            }
        }

        PaywallOptions.Builder(dismissRequest = onDismiss).apply {
            this.listener = listener
            this.shouldDisplayDismissButton = shouldDisplayDismissButton
        }.build()
    }

    Paywall(options = paywallOptions)
}
