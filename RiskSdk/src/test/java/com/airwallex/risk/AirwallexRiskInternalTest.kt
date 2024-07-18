package com.airwallex.risk

import android.util.Log
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals

class AirwallexRiskInternalTest {

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
    }

    @Test
    fun `test start`() {
        val riskContext: RiskContext = mockk()
        every { riskContext.deviceId } returns UUID.randomUUID()
        every { riskContext.sessionId } returns UUID.randomUUID()
        every { riskContext.description() } returns "description"
        val eventManager: EventManager = mockk()
        every { eventManager.start() } returns mockk()

        val shared = AirwallexRiskInternal(
            riskContext = riskContext,
            eventManager = eventManager
        )

        shared.start()
        verify { eventManager.start() }
    }

    @Test
    fun `test set user id`() {
        val riskContext: RiskContext = mockk()
        every { riskContext.deviceId } returns UUID.randomUUID()
        every { riskContext.sessionId } returns UUID.randomUUID()
        every { riskContext.description() } returns "description"
        every { riskContext.updateUserId(any()) } returns mockk()

        val eventManager: EventManager = mockk()

        val shared = AirwallexRiskInternal(
            riskContext = riskContext,
            eventManager = eventManager
        )

        val userId = "abcde"
        shared.setUserId(userId)
        verify { riskContext.updateUserId(userId) }
    }

    @Test
    fun `test set account id`() {
        val riskContext: RiskContext = mockk()
        every { riskContext.deviceId } returns UUID.randomUUID()
        every { riskContext.sessionId } returns UUID.randomUUID()
        every { riskContext.description() } returns "description"
        every { riskContext.updateAccountId(any()) } returns mockk()

        val eventManager: EventManager = mockk()

        val shared = AirwallexRiskInternal(
            riskContext = riskContext,
            eventManager = eventManager
        )

        val accountId = "abcde"
        shared.setAccountId(accountId)
        verify { riskContext.updateAccountId(accountId) }
    }

    @Test
    fun `test log`() {
        val riskContext: RiskContext = mockk()
        every { riskContext.deviceId } returns UUID.randomUUID()
        every { riskContext.sessionId } returns UUID.randomUUID()
        every { riskContext.description() } returns "description"
        every { riskContext.updateAccountId(any()) } returns mockk()
        every { riskContext.createEvent(any(), any(), any()) } returns mockk()

        val eventManager: EventManager = mockk()

        every { eventManager.queue(any()) } returns Unit

        val shared = AirwallexRiskInternal(
            riskContext = riskContext,
            eventManager = eventManager
        )

        shared.log(eventType = "event", screen = "screen")
        verify { eventManager.queue(any()) }
    }

    @Test
    fun `test header`() {
        val deviceId = UUID.randomUUID()
        val riskContext: RiskContext = mockk()
        every { riskContext.deviceId } returns deviceId
        every { riskContext.sessionId } returns UUID.randomUUID()

        val eventManager: EventManager = mockk()

        val shared = AirwallexRiskInternal(
            riskContext = riskContext,
            eventManager = eventManager
        )

        assertEquals(shared.header.field, Constants.headerKey)
        assertEquals(shared.header.value, deviceId.toString())
    }

    @Test
    fun `test session id`() {
        val sessionId = UUID.randomUUID()
        val riskContext: RiskContext = mockk()
        every { riskContext.deviceId } returns UUID.randomUUID()
        every { riskContext.sessionId } returns sessionId

        val eventManager: EventManager = mockk()

        val shared = AirwallexRiskInternal(
            riskContext = riskContext,
            eventManager = eventManager
        )

        assertEquals(shared.sessionId, sessionId)
    }
}