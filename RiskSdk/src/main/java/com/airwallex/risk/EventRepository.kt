package com.airwallex.risk

internal class EventRepository {
    private val events: MutableList<Event> = mutableListOf()

    fun add(event: Event) {
        events.add(event)
    }

    fun addAll(events: List<Event>) {
        this.events.addAll(events)
    }

    fun popAll(): List<Event> {
        val poppedEvents = ArrayList(events)
        events.removeAll(poppedEvents)
        return poppedEvents
    }
}