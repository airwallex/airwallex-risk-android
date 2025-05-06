package com.airwallex.risk

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Tenant(val value: String) {
    @SerialName("Mobile app")
    AIRWALLEX_MOBILE("Mobile app"),
    @SerialName("Scale")
    SCALE("Scale"),
    @SerialName("PA checkout")
    PA("PA checkout")
}
