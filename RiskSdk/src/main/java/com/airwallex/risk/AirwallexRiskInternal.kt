package com.airwallex.risk

import android.util.Log
import java.util.UUID

internal class AirwallexRiskInternal internal constructor(
    private val riskContext: IRiskContext,
    private val eventManager: EventManager
) {
    val header: Header = Header(
        field = Constants.headerKey,
        value = riskContext.deviceId.toString()
    )

    val sessionId: UUID = riskContext.sessionId

    fun start() {
        Log.d(Constants.logTag, riskContext.description())

        eventManager.start()
    }

    fun setUserId(userId: String?) {
        val existingUserId = riskContext.userId
        // If same value, do nothing
        if (userId == existingUserId) return
        // Send logout event before context update if there's an existing user
        if (existingUserId != null) {
            val logoutEvent = riskContext.createEvent(eventType = Constants.userLogoutEventName)
            eventManager.queue(logoutEvent)
        }
        riskContext.updateUserId(userId = userId)
        // Send login event after update if there's a new user
        if (userId != null) {
            val loginEvent = riskContext.createEvent(eventType = Constants.userLoginEventName)
            eventManager.queue(loginEvent)
        }
    }

    fun setAccountId(accountId: String?) {
        if (riskContext.tenant != Tenant.SCALE) {
            riskContext.updateAccountId(accountId = accountId)
            return
        }
        val existingAccountId = riskContext.accountId
        // If same value, do nothing
        if (accountId == existingAccountId) return
        // Send logout event before context update if there's an existing account
        if (existingAccountId != null) {
            val logoutEvent = riskContext.createEvent(eventType = Constants.accountLogoutEventName)
            eventManager.queue(logoutEvent)
        }
        riskContext.updateAccountId(accountId = accountId)
        // Send login event after update if there's a new account
        if (accountId != null) {
            val loginEvent = riskContext.createEvent(eventType = Constants.accountLoginEventName)
            eventManager.queue(loginEvent)
        }
    }

    fun log(eventType: String, screen: String?) {
        val event = riskContext.createEvent(
            eventType = eventType,
            path = screen
        )

        eventManager.queue(event)
    }
}