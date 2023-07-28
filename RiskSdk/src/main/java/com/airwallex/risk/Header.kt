package com.airwallex.risk

/**
 * Airwallex header type. Can be mapped to URL request headers.
 */
data class Header(
    val field: String,
    val value: String
)
