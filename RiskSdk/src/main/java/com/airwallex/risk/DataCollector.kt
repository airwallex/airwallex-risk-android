package com.airwallex.risk

import android.app.Service
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.telephony.TelephonyManager
import java.util.Locale
import java.util.TimeZone

internal class DataCollector(
    applicationContext: Context,
    private val systemLocale: Locale = Resources.getSystem().configuration.locales[0],
    private val appLocale: Locale = Locale.getDefault()
) {
    // region Private

    private val packageContext: PackageInfo = if (BuildHelper.isVersionAtLeastTiramisu()) {
        applicationContext.packageManager.getPackageInfo(
            applicationContext.packageName,
            PackageManager.PackageInfoFlags.of(0)
        )
    } else {
        @Suppress("DEPRECATION")
        applicationContext.packageManager.getPackageInfo(applicationContext.packageName, 0)
    }

    private val telephonyManager = applicationContext.applicationContext.getSystemService(
        Service.TELEPHONY_SERVICE) as TelephonyManager

    private val packageName: String = packageContext.packageName

    // endregion

    // region Exposed data

    val appName: String = if (applicationContext.applicationInfo.labelRes != 0) {
        applicationContext.getString(applicationContext.applicationInfo.labelRes)
    } else {
        applicationContext.applicationInfo.nonLocalizedLabel.toString()
    }

    val appVersion: String = packageContext.versionName?.toString() ?: ""

    val appLanguage: String
        get() = appLocale.language

    val deviceLanguage: String
        get() = systemLocale.language

    val carrierIso: String
        get() = telephonyManager.networkCountryIso ?: ""

    val timezone: String
        get() = TimeZone.getDefault().id

    val deviceOsName = "Android"

    val deviceOsVersion = BuildHelper.osVersion

    val deviceModel: String = BuildHelper.model
    val deviceBrand: String = BuildHelper.brand

    val sdkVersion: String = BuildConfigHelper.sdkVersion

    val userAgent: String = "${appName}/${appVersion} (${packageName})"

    // endregion
}
