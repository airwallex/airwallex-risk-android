package com.airwallex.risk.sample

import com.airwallex.risk.AirwallexRisk

/**
 * Mock payment manager for the payout example.
 */
class PaymentManager {
    fun submitPaymentRequest() {
        // Attach the risk header to any `www.airwallex.com` requests.
        // You can get the header from AirwallexRisk.header
        val header = AirwallexRisk.header
        
        // Log the transaction initiated event when user starts payment flow
        AirwallexRisk.log(
            event = AirwallexRisk.Events.TRANSACTION_INITIATED,
            screen = "payment"
        )
        
        // Simulate payment submission
        // In a real app, you would make the network request here with the risk header
    }
}
