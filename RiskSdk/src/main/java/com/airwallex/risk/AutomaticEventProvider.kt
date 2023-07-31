package com.airwallex.risk

internal interface IAutomaticEventProvider {
    fun sendOpenEvent()
}

internal class AutomaticEventProvider(
    private val repository: EventRepository,
    val riskContext: IRiskContext
) : IAutomaticEventProvider {

    override fun sendOpenEvent() {
        val eventName = if (riskContext.hasSentInstallationEvent) {
            Constants.openEventName
        } else {
            Constants.installationEventName
        }
        val event = riskContext.createEvent(eventType = eventName)
        repository.add(event)
    }
}