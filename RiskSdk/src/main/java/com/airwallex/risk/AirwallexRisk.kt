package com.airwallex.risk

import android.content.Context
import android.util.Log
import java.util.UUID

class AirwallexRisk private constructor() {

    companion object {
        private const val sharedPreferencesFilename: String = Constants.sharedPreferencesFilename

        private var shared: AirwallexRiskInternal? = null

        /**
         * Airwallex risk request header.
         * This header should be attached to each request made to `www.airwallex.com`
         */
        val header: Header?
            get() {
                if (shared == null) {
                    Log.d(Constants.logTag, Constants.logSdkNotStartedError)
                    return null
                }
                return shared?.header
            }

        /**
         * Airwallex session ID. Unique per app run.
         */
        val sessionId: UUID?
            get() = shared?.sessionId

        /**
         * Starts the shared `AirwallexRisk` SDK instance.
         * Parameters:
         * @param applicationContext The context of the single, global Application object of the current process.
         * @param accountId The ID associated with your account. Optional.
         * @param configuration Configuration options.
         */
        fun start(
            applicationContext: Context,
            accountId: String?,
            configuration: RiskConfiguration
        ) {
            if (shared != null) {
                Log.d(Constants.logTag, Constants.logSdkAlreadyStartedError)
                return
            }

            val sharedPreferences = applicationContext.getSharedPreferences(
                sharedPreferencesFilename, Context.MODE_PRIVATE
            )

            val storageManager = StorageManager(sharedPreferences = sharedPreferences)

            val riskContext = RiskContext(
                environment = configuration.environment,
                tenant = configuration.tenant,
                storageManager = storageManager,
                dataCollector = DataCollector(
                    applicationContext = applicationContext
                )
            )

            val eventRepository = EventRepository()

            val shared = AirwallexRiskInternal(
                riskContext = riskContext,
                eventManager = EventManager(
                    repository = eventRepository,
                    apiService = ApiService(
                        riskContext = riskContext
                    ),
                    automaticEventProvider = AutomaticEventProvider(
                        repository = eventRepository,
                        riskContext = riskContext
                    ),
                    bufferTimeMillis = configuration.bufferTimeMillis
                )
            )
            this.shared = shared
            shared.setAccountId(accountId = accountId)
            shared.start()
        }

        /**
         * Sets the signed in Airwallex user ID.
         * Use this method after user sign in/out to store the user ID to be sent with events.
         * @param userId Signed in Airwallex user ID. Set `null` on sign out.
         */
        fun setUserId(userId: String?) {
            val shared = shared
            if (shared == null) {
                Log.d(Constants.logTag, Constants.logSdkNotStartedError)
                return
            }
            shared.setUserId(userId = userId)
        }

        /**
         * Sets the Airwallex account ID.
         * Use this method to update the account ID if it changes.
         * @param accountId Airwallex account ID.
         */
        fun setAccountId(accountId: String?) {
            val shared = shared
            if (shared == null) {
                Log.d(Constants.logTag, Constants.logSdkNotStartedError)
                return
            }
            shared.setAccountId(accountId = accountId)
        }

        /**
         * Adds a new event to the queue.
         * This is a public method for client apps to log specific lifecycle events, eg. login, logout.
         * @param event App event that triggered this method call.
         * @param screen Current app view. Optional.
         */
        fun log(event: String, screen: String?) {
            val shared = shared
            if (shared == null) {
                Log.d(Constants.logTag, Constants.logSdkNotStartedError)
                return
            }
            shared.log(eventType = event, screen = screen)
        }

        /**
         * Adds a new event to the queue.
         * This is a public method for client apps to log specific lifecycle events.
         * @param event Predefined app event.
         * @param screen Current app view. Optional.
         */
        fun log(event: Events, screen: String? = null) {
            val shared = shared
            if (shared == null) {
                Log.d(Constants.logTag, Constants.logSdkNotStartedError)
                return
            }
            shared.log(eventType = event.value, screen = screen)
        }
    }

    /**
     * Standardized event names used across integrations.
     */
    enum class Events(val value: String) {
        /**
         * User starts a new transaction flow
         * Use when user begins any payment/transfer process, before entering details
         */
        TRANSACTION_INITIATED("transaction_initiated"),

        /**
         * User accessed/viewed card PIN
         * Use when user successfully views their card PIN through app
         */
        CARD_PIN_VIEWED("card_pin_viewed"),

        /**
         * User accessed/viewed card CVC/CVV
         * Use when user successfully views their card CVC/security code
         */
        CARD_CVC_VIEWED("card_cvc_viewed"),

        /**
         * User changed their phone number
         * Use when phone number change is successfully saved
         */
        PROFILE_PHONE_UPDATED("profile_phone_updated"),

        /**
         * User changed their email address
         * Use when email change is successfully saved
         */
        PROFILE_EMAIL_UPDATED("profile_email_updated")
    }
}