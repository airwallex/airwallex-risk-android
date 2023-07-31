package com.airwallex.risk

import com.airwallex.risk.helpers.FakeRiskContext
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class AutomaticEventProviderTest {

    @Test
    fun `test installation event`() {
        val repository = EventRepository()
        val riskContext = FakeRiskContext()

        val eventProvider = AutomaticEventProvider(
            repository = repository,
            riskContext = riskContext
        )

        assertFalse(riskContext.hasSentInstallationEvent)
        eventProvider.sendOpenEvent()

        val events = repository.popAll()
        assertEquals(events.size, 1)
        val event = events[0]
        assertEquals(event.event.type, Constants.installationEventName)
    }

    @Test
    fun `test open event`() {
        val repository = EventRepository()
        val riskContext = FakeRiskContext()

        val eventProvider = AutomaticEventProvider(
            repository = repository,
            riskContext = riskContext
        )

        riskContext.hasSentInstallationEvent = true
        eventProvider.sendOpenEvent()

        val events = repository.popAll()
        assertEquals(events.size, 1)
        val event = events[0]
        assertEquals(event.event.type, Constants.openEventName)
    }
}