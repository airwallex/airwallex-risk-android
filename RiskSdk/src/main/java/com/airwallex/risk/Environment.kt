package com.airwallex.risk

enum class Environment(internal val host: String) {
    PRODUCTION("https://bws.airwallex.com/bws/v2/m/"),
    DEMO("https://bws-demo.airwallex.com/bws/v2/m/"),
    STAGING("https://bws-staging.airwallex.com/bws/v2/m/")
}