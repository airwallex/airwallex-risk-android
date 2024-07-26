package com.airwallex.risk

import org.junit.Test
import kotlin.test.assertEquals

class RiskConfigurationTest {

    @Test
    fun `test configuration`() {
        val config1 = RiskConfiguration(environment = Environment.PRODUCTION, tenant = Tenant.PA, bufferTimeMillis = 5_000L)
        assertEquals(config1.environment, Environment.PRODUCTION)
        assertEquals(config1.tenant, Tenant.PA)
        assertEquals(config1.bufferTimeMillis, 5_000L)

        val config2 = RiskConfiguration(isProduction = false, tenant = Tenant.SCALE)
        assertEquals(config2.environment, Environment.DEMO)
        assertEquals(config2.tenant, Tenant.SCALE)
        assertEquals(config2.bufferTimeMillis, 20_000L)
    }
}