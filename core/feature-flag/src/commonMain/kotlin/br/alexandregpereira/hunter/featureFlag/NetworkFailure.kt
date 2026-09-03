package br.alexandregpereira.hunter.featureFlag

/**
 * Whether this was caused by the device not being able to reach the network.
 *
 * Such failures say nothing about the app: the flags could not be fetched and the caller already
 * falls back to its default value. Reporting them only buries the real issues, since
 * "api.lab.amplitude.com" is on most DNS block lists and a large share of users hit it on every
 * launch.
 */
internal expect fun Throwable.isNetworkFailure(): Boolean

internal const val MAX_CAUSE_DEPTH = 10
