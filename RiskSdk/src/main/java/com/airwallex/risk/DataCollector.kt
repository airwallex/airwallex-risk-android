package com.airwallex.risk

import android.app.Service
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.telephony.TelephonyManager
import java.util.Locale
import java.util.TimeZone

internal interface IDataCollector {
    val appName: String
    val appVersion: String
    val appLanguage: String
    val deviceLanguage: String
    val carrierIso: String
    val timezone: String
    val deviceOsName: String
    val deviceOsVersion: String
    val deviceModel: String
    val deviceBrand: String
    val sdkVersion: String
    val userAgent: String
}

internal class DataCollector(
    applicationContext: Context,
    private val systemLocale: Locale = Resources.getSystem().configuration.locales[0],
    private val appLocale: Locale = Locale.getDefault()
) : IDataCollector {
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

    override val appName: String = if (applicationContext.applicationInfo.labelRes != 0) {
        applicationContext.getString(applicationContext.applicationInfo.labelRes)
    } else {
        applicationContext.applicationInfo.nonLocalizedLabel.toString()
    }

    override val appVersion: String = packageContext.versionName?.toString() ?: ""

    override val appLanguage: String
        get() = appLocale.language

    override val deviceLanguage: String
        get() = systemLocale.language

    override val carrierIso: String
        get() = telephonyManager.networkCountryIso ?: ""

    override val timezone: String
        get() = TimeZone.getDefault().id

    override val deviceOsName = "Android"

    override val deviceOsVersion = BuildHelper.osVersion

    override val deviceModel: String = BuildHelper.model
    override val deviceBrand: String = BuildHelper.brand

    override val sdkVersion: String = BuildConfigHelper.sdkVersion

    override val userAgent: String = "${appName}/${appVersion} (${packageName})"

    // endregion
}
