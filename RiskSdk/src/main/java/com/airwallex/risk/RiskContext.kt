package com.airwallex.risk

import android.content.Context
import java.time.Instant
import java.util.UUID

internal class RiskContext(
    val applicationContext: Context,
    val environment: Environment,
    val tenant: Tenant,
    private val storageManager: StorageManager
) {
    var userId: String?
        get() = storageManager.userId
        set(value) { storageManager.userId = value }

    var accountId: String?
        get() = storageManager.accountId
        set(value) { storageManager.accountId = value }

    var hasSentInstallationEvent: Boolean
        get() = storageManager.hasSentInstallationEvent
        set(value) { storageManager.hasSentInstallationEvent = value }

    val userAgent: String
        get() = dataCollector.userAgent

    val sessionId: UUID = UUID.randomUUID()

    val deviceId: UUID
        get() = storageManager.deviceId

    private val dataCollector: DataCollector = DataCollector(
        applicationContext = applicationContext
    )

    fun description(): String = """
            Airwallex Risk started with context:
            App name: ${dataCollector.appName}
            Environment: ${environment.name}
            Tenant: ${tenant.value}
            Device ID: $deviceId
            Session ID: $sessionId
        """.trimIndent()

    fun updateAccountId(accountId: String?) {
        this.accountId = accountId
    }

    fun updateUserId(userId: String?) {
        this.userId = userId
    }

    fun createEvent(
        eventType: String,
        path: String? = null,
        createdAtUtc: Instant = Instant.now()
    ): Event =
        Event(
            eventId = UUID.randomUUID(),
            accountId = accountId,
            userId = userId,
            deviceId = deviceId,
            sessionId = sessionId,
            tenant = tenant,
            app = App(dataCollector = dataCollector),
            device = Device(dataCollector = dataCollector),
            event = EventDetails(
                type = eventType,
                screen = Screen(path = path),
                createdAtUtc = createdAtUtc.toEpochMilli()
            )
        )
}