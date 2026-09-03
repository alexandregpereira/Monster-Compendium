package br.alexandregpereira.hunter.featureFlag

/**
 * Always false: [AmplitudeFeatureFlagIosClient] flattens the NSError into a plain Exception holding
 * only its localizedDescription, so there is no reliable type left to match on. iOS is not a source
 * of this noise anyway - the Amplitude fetch does not show up in its Crashlytics issues.
 */
internal actual fun Throwable.isNetworkFailure(): Boolean = false
