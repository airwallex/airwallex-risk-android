package com.airwallex.risk

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
internal data class Event(
    @Serializable(with = UUIDSerializer::class)
    val eventId: UUID,
    val accountId: String?,
    val userId: String?,
    @Serializable(with = UUIDSerializer::class)
    @SerialName("deviceId")
    val deviceId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val sessionId: UUID,
    val tenant: Tenant,
    val app: App,
    val device: Device,
    val event: EventDetails
)