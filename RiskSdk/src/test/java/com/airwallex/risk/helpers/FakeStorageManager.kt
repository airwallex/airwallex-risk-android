package com.airwallex.risk.helpers

import com.airwallex.risk.IStorageManager
import java.util.UUID

class FakeStorageManager : IStorageManager {

    override var deviceId: UUID = UUID.randomUUID()

    override var userId: String? = null

    override var accountId: String? = null

    override var hasSentInstallationEvent: Boolean = false
}