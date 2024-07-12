package com.airwallex.risk

import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

@Serializable
internal data class Event(
    @Serializable(with = UUIDSerializer::class)
    val eventId: UUID,
    val accountId: String?,
    val userId: String?,
    @Serializable(with = UUIDSerializer::class)
    val deviceId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val sessionId: UUID,
    val tenant: Tenant,
    val app: App,
    val device: Device,
    val event: EventDetails
) {
    constructor(
        riskContext: IRiskContext,
        dataCollector: IDataCollector,
        eventType: String,
        path: String?,
        createdAtUtc: Long
    ) : this(
        eventId = UUID.randomUUID(),
        accountId = riskContext.accountId,
        userId = riskContext.userId,
        deviceId = riskContext.deviceId,
        sessionId = riskContext.sessionId,
        tenant = riskContext.tenant,
        app = App(dataCollector = dataCollector),
        device = Device(dataCollector = dataCollector),
        event = EventDetails(
            type = eventType,
            screen = Screen(path = path),
            createdAtUtc = createdAtUtc
        )
    )
}