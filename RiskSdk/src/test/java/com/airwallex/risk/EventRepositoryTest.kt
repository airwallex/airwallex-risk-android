package com.airwallex.risk

import com.airwallex.risk.helpers.Fixtures
import kotlin.test.Test
import kotlin.test.assertEquals

class EventRepositoryTest {

    @Test
    fun `test repository`() {
        val event1 = Fixtures.createEvent(eventType = "login")
        val event2 = Fixtures.createEvent(eventType = "payment")
        val event3 = Fixtures.createEvent(eventType = "test")

        val repository = EventRepository()
        repository.add(event1)
        repository.addAll(listOf(event2, event3))

        assertEquals(listOf(event1, event2, event3), repository.popAll())
    }
}