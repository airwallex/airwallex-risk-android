package com.airwallex.risk

import org.junit.Test
import kotlin.test.assertEquals

class EnvironmentTest {

    @Test
    fun `test environment strings`() {
        assertEquals(Environment.PRODUCTION.host, "https://bws.airwallex.com/bws/v2/m/")
        assertEquals(Environment.DEMO.host, "https://bws-demo.airwallex.com/bws/v2/m/")
        assertEquals(Environment.STAGING.host, "https://bws-staging.airwallex.com/bws/v2/m/")
    }
}