package com.airwallex.risk

/**
 * Configuration context for the Airwallex Risk SDK.
 * @param environment Airwallex risk environment, set to production for release builds.
 * @Param tenant only modify if requested by Airwallex.
 * @param bufferTimeMillis requests buffer time in milliseconds.
 */
class RiskConfiguration(
    val environment: Environment = Environment.PRODUCTION,
    val tenant: Tenant = Tenant.SCALE,
    val bufferTimeMillis: Long = 20_000L
) {

    /**
     * Configuration context for the Airwallex Risk SDK.
     * @param isProduction set to `false` for test environments.
     * @Param tenant only modify if requested by Airwallex.
     */
    constructor(
        isProduction: Boolean = true,
        tenant: Tenant = Tenant.SCALE
    ) : this(
        environment = if (isProduction) Environment.PRODUCTION else Environment.DEMO,
        tenant = tenant
    )
}
