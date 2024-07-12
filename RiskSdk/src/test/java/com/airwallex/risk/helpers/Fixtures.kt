package com.airwallex.risk.helpers

import com.airwallex.risk.App
import com.airwallex.risk.Device
import com.airwallex.risk.Event
import com.airwallex.risk.EventDetails
import com.airwallex.risk.Screen
import com.airwallex.risk.Tenant
import java.time.Instant
import java.util.UUID

internal object Fixtures {
    fun createEvent(
        eventType: String,
        path: String? = null,
        createdAtUtc: Long = System.currentTimeMillis(),
        accountId: String? = "account id",
        userId: String? = "user id",
        deviceId: UUID = UUID.randomUUID(),
        sessionId: UUID = UUID.randomUUID(),
        tenant: Tenant = Tenant.SCALE
    ): Event = Event(
        eventId = UUID.randomUUID(),
        accountId = accountId,
        userId = userId,
        deviceId = deviceId,
        sessionId = sessionId,
        tenant = tenant,
        app = App(
            name = "App",
            version = "Version",
            language = "en",
            sdk = App.Sdk(
                version = "1.0.0"
            )
        ),
        device = Device(
            os = Device.Os(
                name = "Android",
                version = "13.0",
                region = "au",
                language = "en",
                timezone = "UTC+10"
            ),
            model = Device.Model(
                name = "Pixel",
                brand = "Google"
            )
        ),
        event = EventDetails(
            type = eventType,
            screen = Screen(
                path = path
            ),
            createdAtUtc = createdAtUtc
        )
    )
}