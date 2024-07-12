package com.airwallex.risk.helpers

import com.airwallex.risk.Environment
import com.airwallex.risk.Event
import com.airwallex.risk.IRiskContext
import com.airwallex.risk.Tenant
import java.util.UUID

internal class FakeRiskContext : IRiskContext {

    override var userId: String? = null
    override var accountId: String? = null
    override var hasSentInstallationEvent: Boolean = false
    override val userAgent: String = "user agent"
    override val sessionId: UUID = UUID.randomUUID()
    override val deviceId: UUID = UUID.randomUUID()
    override val environment: Environment = Environment.STAGING
    override val tenant: Tenant = Tenant.SCALE

    override fun description(): String = "description"

    override fun updateAccountId(accountId: String?) {
        this.accountId = accountId
    }

    override fun updateUserId(userId: String?) {
        this.userId = userId
    }

    override fun createEvent(eventType: String, path: String?, createdAtUtc: Long): Event =
        Fixtures.createEvent(
            eventType = eventType,
            path = path,
            createdAtUtc = createdAtUtc,
            accountId = accountId,
            userId = userId,
            deviceId = deviceId,
            sessionId = sessionId,
            tenant = tenant
        )
}