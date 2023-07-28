package com.airwallex.risk

import kotlinx.serialization.Serializable

@Serializable
internal data class App(
    val name: String,
    val version: String,
    val language: String,
    val sdk: Sdk
) {
    @Serializable
    internal data class Sdk(
        val version: String
    )

    constructor(dataCollector: DataCollector) : this(
        name = dataCollector.appName,
        version = dataCollector.appVersion,
        language = dataCollector.appLanguage,
        sdk = Sdk(
            version = dataCollector.sdkVersion
        )
    )
}
