package com.airwallex.risk.sample

import android.app.Application
import com.airwallex.risk.AirwallexRisk
import com.airwallex.risk.RiskConfiguration

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val accountId = null // "YOUR-ACCOUNT-ID-GOES-HERE"

        AirwallexRisk.start(
            applicationContext = applicationContext,
            accountId = accountId,
            configuration = RiskConfiguration(isProduction = !BuildConfig.DEBUG)
        )

        // Ensure any network requests sent to airwallex.com attach the Airwallex Risk header
        val header = AirwallexRisk.header
    }
}