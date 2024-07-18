package com.airwallex.risk

/**
 * Configuration context for the Airwallex Risk SDK.
 * @param isProduction set to `false` for test environments.
 * @Param tenant only modify if requested by Airwallex.
 * @param bufferTimeMillis requests buffer time in milliseconds.
 */
class RiskConfiguration(
    isProduction: Boolean = true,
    val tenant: Tenant = Tenant.SCALE,
    val bufferTimeMillis: Long = 20_000L
) {
    internal val environment: Environment

    init {
        this.environment = if (isProduction) Environment.PRODUCTION else Environment.DEMO
    }
}
