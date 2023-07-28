package com.airwallex.risk

internal enum class Environment(internal val host: String) {
    PRODUCTION("https://www.airwallex.com/bws/v2/m/"),
    DEMO("https://demo.airwallex.com/bws/v2/m/"),
    STAGING("https://staging.airwallex.com/bws/v2/m/"),
    DEV("https://dev.airwallex.com/bws/v2/m/")
}