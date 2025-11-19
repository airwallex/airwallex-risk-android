package com.airwallex.risk

import android.util.Log
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class PredefinedEventsTest {

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
    }

    @Test
    fun `test predefined event values`() {
        assertEquals("transaction_initiated", AirwallexRisk.Events.TRANSACTION_INITIATED.value)
        assertEquals("card_pin_viewed", AirwallexRisk.Events.CARD_PIN_VIEWED.value)
        assertEquals("card_cvc_viewed", AirwallexRisk.Events.CARD_CVC_VIEWED.value)
        assertEquals("profile_phone_updated", AirwallexRisk.Events.PROFILE_PHONE_UPDATED.value)
        assertEquals("profile_email_updated", AirwallexRisk.Events.PROFILE_EMAIL_UPDATED.value)
    }

    @Test
    fun `test log with predefined event`() {
        val riskContext: RiskContext = mockk()
        every { riskContext.deviceId } returns java.util.UUID.randomUUID()
        every { riskContext.sessionId } returns java.util.UUID.randomUUID()
        every { riskContext.description() } returns "description"
        every { riskContext.createEvent(any(), any(), any()) } returns mockk()

        val eventManager: EventManager = mockk()
        every { eventManager.queue(any()) } returns Unit

        val shared = AirwallexRiskInternal(
            riskContext = riskContext,
            eventManager = eventManager
        )

        shared.log(eventType = AirwallexRisk.Events.TRANSACTION_INITIATED.value, screen = "checkout")
        verify { riskContext.createEvent("transaction_initiated", "checkout", any()) }
        verify { eventManager.queue(any()) }
    }

    @Test
    fun `test log with each predefined event type`() {
        val riskContext: RiskContext = mockk()
        every { riskContext.deviceId } returns java.util.UUID.randomUUID()
        every { riskContext.sessionId } returns java.util.UUID.randomUUID()
        every { riskContext.description() } returns "description"
        every { riskContext.createEvent(any(), any(), any()) } returns mockk()

        val eventManager: EventManager = mockk()
        every { eventManager.queue(any()) } returns Unit

        val shared = AirwallexRiskInternal(
            riskContext = riskContext,
            eventManager = eventManager
        )

        val events = listOf(
            AirwallexRisk.Events.TRANSACTION_INITIATED,
            AirwallexRisk.Events.CARD_PIN_VIEWED,
            AirwallexRisk.Events.CARD_CVC_VIEWED,
            AirwallexRisk.Events.PROFILE_PHONE_UPDATED,
            AirwallexRisk.Events.PROFILE_EMAIL_UPDATED
        )

        events.forEach { event ->
            shared.log(eventType = event.value, screen = null)
            verify { riskContext.createEvent(event.value, null, any()) }
        }

        verify(exactly = events.size) { eventManager.queue(any()) }
    }

    @Test
    fun `test log with predefined event and screen`() {
        val riskContext: RiskContext = mockk()
        every { riskContext.deviceId } returns java.util.UUID.randomUUID()
        every { riskContext.sessionId } returns java.util.UUID.randomUUID()
        every { riskContext.description() } returns "description"
        every { riskContext.createEvent(any(), any(), any()) } returns mockk()

        val eventManager: EventManager = mockk()
        every { eventManager.queue(any()) } returns Unit

        val shared = AirwallexRiskInternal(
            riskContext = riskContext,
            eventManager = eventManager
        )

        val screen = "profile_screen"
        shared.log(eventType = AirwallexRisk.Events.PROFILE_EMAIL_UPDATED.value, screen = screen)
        verify { riskContext.createEvent("profile_email_updated", screen, any()) }
        verify { eventManager.queue(any()) }
    }
}
