package com.airwallex.risk

import com.airwallex.risk.helpers.FakeDataCollector
import com.airwallex.risk.helpers.FakeRiskContext
import com.airwallex.risk.helpers.Fixtures
import io.mockk.MockKAnnotations
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.Before
import org.junit.Test
import java.time.Instant
import kotlin.test.assertEquals

class EventTest {

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test event creation`() {
        val riskContext = FakeRiskContext()
        val dataCollector = FakeDataCollector()

        val eventType = "event type"
        val path = "path"

        val event = Event(
            riskContext = riskContext,
            dataCollector = dataCollector,
            eventType = eventType,
            path = path,
            createdAtUtc = 10
        )

        assertEquals(event.accountId, riskContext.accountId)
        assertEquals(event.userId, riskContext.userId)
        assertEquals(event.tenant, riskContext.tenant)
        assertEquals(event.event.type, eventType)
        assertEquals(event.event.screen.path, path)
        assertEquals(event.app.name, dataCollector.appName)
    }

    @Test
    fun `test serialization`() {
        val event = Fixtures.createEvent("login")
        val encoded = Json.encodeToJsonElement(event)

        val decoded = Json.decodeFromJsonElement<Event>(encoded)
        assertEquals(event, decoded)
    }
}