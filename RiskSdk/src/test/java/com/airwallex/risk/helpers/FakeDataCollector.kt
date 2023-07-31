package com.airwallex.risk.helpers

import com.airwallex.risk.IDataCollector

class FakeDataCollector : IDataCollector {
    override val appName: String = "App Name"
    override val appVersion: String = "1.0.0"
    override val appLanguage: String = "en"
    override val deviceLanguage: String = "zh"
    override val carrierIso: String = "au"
    override val timezone: String = "timezone"
    override val deviceOsName: String = "android"
    override val deviceOsVersion: String = "13"
    override val deviceModel: String = "Pixel"
    override val deviceBrand: String = "Google"
    override val sdkVersion: String = "1.0.1"
    override val userAgent: String = "user agent"
}