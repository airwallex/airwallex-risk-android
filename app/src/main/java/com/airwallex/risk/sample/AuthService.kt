package com.airwallex.risk.sample

import com.airwallex.risk.AirwallexRisk
import java.util.UUID

/**
 * Mock auth manager for the login example.
 */
class AuthService {
    data class User(
        val id: String,
        val username: String
    )

    fun login(username: String, password: String): User {
        // When the user logs in, set the user id
        val user = User(id = UUID.randomUUID().toString(), username = username)
        // update userId will trigger user_login event automatically
        AirwallexRisk.setUserId(userId = user.id)
        return user
    }

    fun logout() {
        // update userId to null will trigger user_logout event automatically
        AirwallexRisk.setUserId(userId = null)
    }
}
