package com.airwallex.risk

/*
 * Required to wrap `BuildConfig` in order to mock values in tests.
 */
internal object BuildConfigHelper {
    const val sdkVersion = BuildConfig.SDK_VERSION
}