package com.airwallex.risk

import android.util.Log

internal class AirwallexRiskInternal internal constructor(
    private val riskContext: IRiskContext,
    private val eventManager: EventManager
) {
    val header: Header = Header(
        field = Constants.headerKey,
        value = riskContext.deviceId.toString()
    )

    fun start() {
        Log.d(Constants.logTag, riskContext.description())

        eventManager.start()
    }

    fun setUserId(userId: String?) {
        riskContext.updateUserId(userId = userId)
    }

    fun setAccountId(accountId: String?) {
        riskContext.updateAccountId(accountId = accountId)
    }

    fun log(eventType: String, screen: String?) {
        val event = riskContext.createEvent(
            eventType = eventType,
            path = screen
        )

        eventManager.queue(event)
    }
}