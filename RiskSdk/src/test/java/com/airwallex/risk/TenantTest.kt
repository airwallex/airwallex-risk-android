package com.airwallex.risk

import org.junit.Test
import kotlin.test.assertEquals

class TenantTest {

    @Test
    fun `test tenants`() {
        assertEquals(Tenant.AIRWALLEX_MOBILE.value, "Mobile app")
        assertEquals(Tenant.SCALE.value, "Scale")
        assertEquals(Tenant.PA.value, "PA checkout")
    }
}
