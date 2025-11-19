package com.airwallex.risk.sample

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.airwallex.risk.sample.databinding.ActivityMainBinding

class MainActivity: FragmentActivity() {

    lateinit var binding: ActivityMainBinding
    private val authService = AuthService()
    private val paymentManager = PaymentManager()
    private var isAuthenticated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // set up view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        updateUI()
    }

    private fun setupListeners() {
        binding.loginButton.setOnClickListener {
            login()
        }

        binding.logoutButton.setOnClickListener {
            logout()
        }

        binding.sendPaymentButton.setOnClickListener {
            submitPayment()
        }

        binding.transactionInitiatedButton.setOnClickListener {
            logPredefinedEvent(com.airwallex.risk.AirwallexRisk.Events.TRANSACTION_INITIATED)
        }

        binding.cardPinViewedButton.setOnClickListener {
            logPredefinedEvent(com.airwallex.risk.AirwallexRisk.Events.CARD_PIN_VIEWED)
        }

        binding.cardCvcViewedButton.setOnClickListener {
            logPredefinedEvent(com.airwallex.risk.AirwallexRisk.Events.CARD_CVC_VIEWED)
        }

        binding.profilePhoneUpdatedButton.setOnClickListener {
            logPredefinedEvent(com.airwallex.risk.AirwallexRisk.Events.PROFILE_PHONE_UPDATED)
        }

        binding.profileEmailUpdatedButton.setOnClickListener {
            logPredefinedEvent(com.airwallex.risk.AirwallexRisk.Events.PROFILE_EMAIL_UPDATED)
        }
    }

    private fun login() {
        val username = binding.usernameEditText.text?.toString()?.trim() ?: "test@airwallex.com"
        val password = binding.passwordEditText.text?.toString()?.trim() ?: "Password123"
        
        authService.login(username = username, password = password)
        isAuthenticated = true
        updateUI()
    }

    private fun logout() {
        authService.logout()
        isAuthenticated = false
        updateUI()
    }

    private fun submitPayment() {
        paymentManager.submitPaymentRequest()
    }

    private fun logPredefinedEvent(event: com.airwallex.risk.AirwallexRisk.Events) {
        com.airwallex.risk.AirwallexRisk.log(event = event, screen = "payment")
    }

    private fun updateUI() {
        if (isAuthenticated) {
            // Show payment view
            binding.loginLayout.visibility = View.GONE
            binding.paymentLayout.visibility = View.VISIBLE
        } else {
            // Show login view
            binding.loginLayout.visibility = View.VISIBLE
            binding.paymentLayout.visibility = View.GONE
        }
    }
}
