package com.airwallex.risk

import android.content.Context
import android.util.Log

class AirwallexRisk private constructor(
    private val riskContext: RiskContext,
    private val eventManager: EventManager
) {
    val header: Header = Header(
        field = Constants.headerKey,
        value = riskContext.deviceId.toString()
    )

    companion object {
        private const val sharedPreferencesFilename: String = Constants.sharedPreferencesFilename

        private var shared: AirwallexRisk? = null

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
                applicationContext = applicationContext,
                environment = configuration.environment,
                tenant = configuration.tenant,
                storageManager = storageManager
            )

            val eventRepository = EventRepository()

            val shared = AirwallexRisk(
                riskContext = riskContext,
                eventManager = EventManager(
                    repository = eventRepository,
                    apiService = ApiService(
                        riskContext = riskContext
                    ),
                    automaticEventProvider = AutomaticEventProvider(
                        repository = eventRepository,
                        riskContext = riskContext
                    )
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
            shared.riskContext.updateUserId(userId = userId)
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
            shared.riskContext.updateAccountId(accountId = accountId)
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
    }

    fun start() {
        Log.d(Constants.logTag, riskContext.description())

        eventManager.start()
    }

    fun setUserId(userId: String?) {
        riskContext.updateUserId(userId = userId)
    }

    fun setAccountId(accountId: String?) {
        riskContext.updateAccountId(accountId = accountId)
    }

    fun log(eventType: String, screen: String?) {
        val event = riskContext.createEvent(
            eventType = eventType,
            path = screen
        )

        eventManager.queue(event)
    }
}