package com.airwallex.risk

import com.airwallex.risk.helpers.FakeDataCollector
import com.airwallex.risk.helpers.FakeStorageManager
import io.mockk.MockKAnnotations
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RiskContextTest {

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test userId`() {
        val riskContext = createRiskContext()

        val userId = "userId"
        assertNull(riskContext.userId)
        riskContext.userId = userId
        assertEquals(riskContext.userId, userId)
    }

    @Test
    fun `test accountId`() {
        val riskContext = createRiskContext()

        val accountId = "accountId"
        assertNull(riskContext.accountId)
        riskContext.accountId = accountId
        assertEquals(riskContext.accountId, accountId)
    }

    @Test
    fun `test hasSentInstallationEvent`() {
        val riskContext = createRiskContext()

        assertFalse(riskContext.hasSentInstallationEvent)
        riskContext.hasSentInstallationEvent = true
        assertTrue(riskContext.hasSentInstallationEvent)
    }

    @Test
    fun `test userAgent`() {
        val dataCollector = FakeDataCollector()
        val riskContext = createRiskContext(dataCollector = dataCollector)
        assertEquals(riskContext.userAgent, dataCollector.userAgent)
    }

    @Test
    fun `test sessionId`() {
        val riskContext = createRiskContext()
        val sessionId = riskContext.sessionId
        // shouldn't change, in memory only
        assertEquals(sessionId, riskContext.sessionId)
    }

    @Test
    fun `test deviceId`() {
        val storageManager = FakeStorageManager()
        val riskContext = createRiskContext(storageManager = storageManager)
        val deviceId = riskContext.deviceId

        assertEquals(deviceId, storageManager.deviceId)
    }

    @Test
    fun `test description`() {
        val storageManager = FakeStorageManager()
        val dataCollector = FakeDataCollector()
        val riskContext = createRiskContext(
            storageManager = storageManager,
            dataCollector = dataCollector
        )
        assertEquals(
            riskContext.description(),
            """
            Airwallex Risk started with context:
            App name: App Name
            Environment: DEV
            Tenant: Scale
            Device ID: ${storageManager.deviceId}
            Session ID: ${riskContext.sessionId}
            """.trimIndent()
        )
    }

    @Test
    fun `test createEvent`() {
        val storageManager = FakeStorageManager()
        val dataCollector = FakeDataCollector()
        val riskContext = createRiskContext(
            storageManager = storageManager,
            dataCollector = dataCollector
        )

        val userId = "userId"
        val accountId = "accountId"
        riskContext.updateUserId(userId)
        riskContext.updateAccountId(accountId)

        val event = riskContext.createEvent(
            eventType = "login",
            path = "home_screen"
        )

        assertNotNull(event.eventId)
        assertEquals(event.event.screen.path, "home_screen")
        assertEquals(event.event.type, "login")
    }

    private fun createRiskContext(
        storageManager: IStorageManager = FakeStorageManager(),
        dataCollector: IDataCollector = FakeDataCollector()
    ): RiskContext = RiskContext(
        environment = Environment.DEV,
        tenant = Tenant.SCALE,
        storageManager = storageManager,
        dataCollector = dataCollector
    )
}