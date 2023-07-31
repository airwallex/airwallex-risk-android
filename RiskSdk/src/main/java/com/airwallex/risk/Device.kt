package com.airwallex.risk

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Device(
    @SerialName("os")
    val os: Os,
    val model: Model,
) {
    @Serializable
    internal data class Os(
        val name: String,
        val version: String,
        val region: String,
        val language: String,
        val timezone: String,
    )

    @Serializable
    internal data class Model(
        val name: String,
        val brand: String
    )

    constructor(dataCollector: IDataCollector) : this(
        os = Os(
            name = dataCollector.deviceOsName,
            version = dataCollector.deviceOsVersion,
            region = dataCollector.carrierIso,
            language = dataCollector.deviceLanguage,
            timezone = dataCollector.timezone
        ),
        model = Model(
            name = dataCollector.deviceModel,
            brand = dataCollector.deviceBrand
        )
    )
}
