package com.airwallex.risk

internal object Constants {
    const val logTag: String = "AirwallexRisk"
    const val logSdkAlreadyStartedError: String = "AirwallexRisk has already started"
    const val logSdkNotStartedError: String = "AirwallexRisk not started"
    const val sharedPreferencesFilename: String = "airwallex"
    const val headerKey: String = "x-risk-device-id"

    const val openEventName: String = "open"
    const val installationEventName: String = "installation"
}