package com.airwallex.risk

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

/*
 * Required to wrap `Build` in order to mock values in tests.
 */
internal object BuildHelper {
    val osVersion: String = Build.VERSION.RELEASE ?: ""
    val model: String = Build.MODEL ?: ""
    val brand: String = Build.BRAND ?: ""
    val sdkVersion: Int = Build.VERSION.SDK_INT

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
    fun isVersionAtLeastTiramisu() = sdkVersion >= Build.VERSION_CODES.TIRAMISU
}