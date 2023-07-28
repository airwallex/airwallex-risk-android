package com.airwallex.risk

internal class AutomaticEventProvider(
    private val repository: EventRepository,
    val riskContext: RiskContext
) {

    fun sendOpenEvent() {
        val eventName = if (riskContext.hasSentInstallationEvent) {
            Constants.openEventName
        } else {
            Constants.installationEventName
        }
        val event = riskContext.createEvent(eventType = eventName)
        repository.add(event)
    }
}