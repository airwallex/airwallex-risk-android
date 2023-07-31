package com.airwallex.risk

import android.content.SharedPreferences
import androidx.core.content.edit
import java.util.UUID

internal interface IStorageManager {
    var deviceId: UUID
    var userId: String?
    var accountId: String?
    var hasSentInstallationEvent: Boolean
}

internal class StorageManager(
    val sharedPreferences: SharedPreferences
) : IStorageManager {

    companion object {
        private const val DEVICE_ID_KEY = "com.airwallex:device_id"
        private const val USER_ID_KEY = "com.airwallex:user_id"
        private const val ACCOUNT_ID_KEY = "com.airwallex:account_id"
        private const val HAS_SENT_INSTALLATION_EVENT = "com.airwallex:installevent"
    }

    override var deviceId: UUID
        get() {
            val value = sharedPreferences.getString(DEVICE_ID_KEY, null)
            if (value != null) {
                return UUID.fromString(value)
            } else {
                val randomUUID = UUID.randomUUID()
                this.deviceId = randomUUID
                return randomUUID
            }
        }
        set(value) = sharedPreferences.edit { putString(DEVICE_ID_KEY, value.toString()) }

    override var userId: String?
        get() = sharedPreferences.getString(USER_ID_KEY, null)
        set(value) {
            sharedPreferences.edit { putString(USER_ID_KEY, value) }
        }

    override var accountId: String?
        get() = sharedPreferences.getString(ACCOUNT_ID_KEY, null)
        set(value) {
            sharedPreferences.edit { putString(ACCOUNT_ID_KEY, value.toString()) }
        }

    override var hasSentInstallationEvent: Boolean
        get() = sharedPreferences.getBoolean(HAS_SENT_INSTALLATION_EVENT, false)
        set(value) {
            sharedPreferences.edit { putBoolean(HAS_SENT_INSTALLATION_EVENT, value) }
        }
}