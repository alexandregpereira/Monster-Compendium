/*
 * Copyright (c) 2024 Alexandre Gomes Pereira
 *
 * SPDX-License-Identifier: MIT
 *
 * Licensed under the MIT License. See the LICENSE file in this module directory
 * for the full license text.
 */

package br.alexandregpereira.hunter.analytics

interface Analytics {

    fun track(eventName: String, params: Map<String, Any?> = emptyMap())

    fun setUserProperty(name: String, value: Any)

    fun getDeviceId(): String?

    fun logException(throwable: Throwable)
}

class EmptyAnalytics : Analytics {

    override fun track(eventName: String, params: Map<String, Any?>) {}

    override fun setUserProperty(name: String, value: Any) {}

    override fun getDeviceId(): String? = null

    override fun logException(throwable: Throwable) {
        throwable.printStackTrace()
    }
}
