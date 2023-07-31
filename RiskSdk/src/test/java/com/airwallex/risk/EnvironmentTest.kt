package com.airwallex.risk

import org.junit.Test
import kotlin.test.assertEquals

class EnvironmentTest {

    @Test
    fun `test environment strings`() {
        assertEquals(Environment.PRODUCTION.host, "https://www.airwallex.com/bws/v2/m/")
        assertEquals(Environment.DEMO.host, "https://demo.airwallex.com/bws/v2/m/")
        assertEquals(Environment.STAGING.host, "https://staging.airwallex.com/bws/v2/m/")
        assertEquals(Environment.DEV.host, "https://dev.airwallex.com/bws/v2/m/")
    }
}