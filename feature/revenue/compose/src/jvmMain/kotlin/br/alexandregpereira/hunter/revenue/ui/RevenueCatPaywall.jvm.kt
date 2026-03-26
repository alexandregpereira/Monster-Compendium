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
import androidx.compose.ui.Modifier

/**
 * JVM/Desktop fallback implementation of the paywall.
 * RevenueCat Paywall UI is not available on desktop platforms,
 * so this displays a placeholder or fallback content.
 */
@Composable
internal actual fun RevenueCatPaywall(
    shouldDisplayDismissButton: Boolean,
    onDismiss: () -> Unit,
    onPurchaseCompleted: () -> Unit,
    onPurchaseError: (message: String) -> Unit,
    modifier: Modifier,
) {
    // Desktop/JVM fallback - RevenueCat Paywall UI is not available
    PaywallScreenContent(
        subscribe = {
            // On desktop, just dismiss as there's no in-app purchase available
            onDismiss()
        },
    )
}
