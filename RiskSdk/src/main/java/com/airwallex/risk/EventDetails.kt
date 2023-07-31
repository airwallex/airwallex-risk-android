package com.airwallex.risk

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventDetails(
    val type: String,
    val screen: Screen,
    @SerialName("createdAtUTC")
    val createdAtUtc: Long
)