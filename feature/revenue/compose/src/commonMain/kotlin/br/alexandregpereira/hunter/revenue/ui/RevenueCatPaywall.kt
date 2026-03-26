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
 * Platform-specific RevenueCat Paywall implementation.
 *
 * On Android and iOS, this displays the RevenueCat Paywall UI.
 * On JVM/Desktop, this displays a fallback placeholder.
 *
 * @param shouldDisplayDismissButton Whether to show the dismiss/close button on the paywall
 * @param onDismiss Called when the paywall is dismissed
 * @param onPurchaseCompleted Called when a purchase is successfully completed
 * @param onPurchaseError Called when a purchase error occurs
 * @param modifier Modifier for the paywall container
 */
@Composable
internal expect fun RevenueCatPaywall(
    shouldDisplayDismissButton: Boolean,
    onDismiss: () -> Unit,
    onPurchaseCompleted: () -> Unit,
    onPurchaseError: (message: String) -> Unit,
    modifier: Modifier = Modifier,
)
