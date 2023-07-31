package com.airwallex.risk

import java.time.Instant
import java.util.UUID

internal interface IRiskContext {
    var userId: String?
    var accountId: String?
    var hasSentInstallationEvent: Boolean
    val userAgent: String
    val sessionId: UUID
    val deviceId: UUID
    val environment: Environment
    val tenant: Tenant

    fun description(): String

    fun updateAccountId(accountId: String?)

    fun updateUserId(userId: String?)

    fun createEvent(
        eventType: String,
        path: String? = null,
        createdAtUtc: Instant = Instant.now()
    ): Event
}

internal class RiskContext(
    override val environment: Environment,
    override val tenant: Tenant,
    private val storageManager: IStorageManager,
    private val dataCollector: IDataCollector
) : IRiskContext {
    override var userId: String?
        get() = storageManager.userId
        set(value) { storageManager.userId = value }

    override var accountId: String?
        get() = storageManager.accountId
        set(value) { storageManager.accountId = value }

    override var hasSentInstallationEvent: Boolean
        get() = storageManager.hasSentInstallationEvent
        set(value) { storageManager.hasSentInstallationEvent = value }

    override val userAgent: String
        get() = dataCollector.userAgent

    override val sessionId: UUID = UUID.randomUUID()

    override val deviceId: UUID
        get() = storageManager.deviceId

    override fun description(): String = """
            Airwallex Risk started with context:
            App name: ${dataCollector.appName}
            Environment: ${environment.name}
            Tenant: ${tenant.value}
            Device ID: $deviceId
            Session ID: $sessionId
        """.trimIndent()

    override fun updateAccountId(accountId: String?) {
        this.accountId = accountId
    }

    override fun updateUserId(userId: String?) {
        this.userId = userId
    }

    override fun createEvent(
        eventType: String,
        path: String?,
        createdAtUtc: Instant
    ): Event =
        Event(
            riskContext = this,
            dataCollector = dataCollector,
            eventType = eventType,
            path = path,
            createdAtUtc = createdAtUtc
        )
}