package br.alexandregpereira.hunter.analytics

import cocoapods.FirebaseAnalytics.FIRAnalytics
import cocoapods.FirebaseCrashlytics.FIRCrashlytics
import platform.Foundation.NSError
import platform.Foundation.NSLocalizedDescriptionKey

@OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)
internal class IosFirebaseAnalytics : Analytics {

    override fun track(eventName: String, params: Map<String, Any?>) {
        val normalized = eventName.replace(" ", "_").replace("-", "")
        FIRAnalytics.logEventWithName(normalized, parameters = params.toNSDictionary())
    }

    override fun logException(throwable: Throwable) {
        throwable.printStackTrace()
        val crashlytics = FIRCrashlytics.crashlytics()
        crashlytics.log("${throwable::class.simpleName}: ${throwable.message}\n${throwable.stackTraceToString()}")
        val nsError = NSError.errorWithDomain(
            domain = "KotlinException",
            code = 0,
            userInfo = mapOf<Any?, Any?>(
                NSLocalizedDescriptionKey to (throwable.message ?: throwable::class.simpleName ?: "Unknown error")
            )
        )
        crashlytics.recordError(nsError)
    }
}

private fun Map<String, Any?>.toNSDictionary(): Map<Any?, *> =
    mapKeys { it.key as Any? }
