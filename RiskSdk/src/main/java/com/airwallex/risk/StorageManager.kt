package com.airwallex.risk

import android.content.SharedPreferences
import androidx.core.content.edit
import java.util.UUID

internal class StorageManager(
    val sharedPreferences: SharedPreferences
) {

    companion object {
        private const val DEVICE_ID_KEY = "com.airwallex:device_id"
        private const val USER_ID_KEY = "com.airwallex:user_id"
        private const val ACCOUNT_ID_KEY = "com.airwallex:account_id"
        private const val HAS_SENT_INSTALLATION_EVENT = "com.airwallex:installevent"
    }

    var deviceId: UUID
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

    var userId: String?
        get() = sharedPreferences.getString(USER_ID_KEY, null)
        set(value) {
            sharedPreferences.edit { putString(USER_ID_KEY, value) }
        }

    var accountId: String?
        get() = sharedPreferences.getString(ACCOUNT_ID_KEY, null)
        set(value) {
            sharedPreferences.edit { putString(ACCOUNT_ID_KEY, value.toString()) }
        }

    var hasSentInstallationEvent: Boolean
        get() = sharedPreferences.getBoolean(HAS_SENT_INSTALLATION_EVENT, false)
        set(value) {
            sharedPreferences.edit { putBoolean(HAS_SENT_INSTALLATION_EVENT, value) }
        }
}