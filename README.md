# Airwallex Risk Android SDK

![Bitrise](https://app.bitrise.io/app/311ac459dba8618d/status.svg?token=PdOUUoDjBwL_Z5NEKGiMmQ&branch=main)
![Platforms](https://img.shields.io/badge/platforms-Android-333333.svg)

The Airwallex Risk SDK provides device intelligence and fraud detection capabilities for merchant and platform apps that integrate with Airwallex services.

## Use Cases

This SDK supports two primary scenarios:

1. **Payment Acceptance (PA)**: For merchant mobile apps accepting payments
2. **Connected Accounts**: For platforms that programmatically create connected accounts for businesses and individuals, and enable them with financial capabilities

## Table of contents

<!--ts-->
  * [Requirements](#requirements)
  * [Installation](#installation)
  * [Usage](#usage)
    * [Quick start](#quick-start)
    * [Update user](#update-user) 
    * [Events](#events)
    * [Request header](#request-header)

<!--te-->

## Requirements

The Airwallex Risk Android SDK requires Android Studio Flamingo or later with Java 17 and is compatible with apps targeting Android 8 or above.

### Installation

#### Gradle

In your app's `build.gradle`, add:

```
implementation "io.github.airwallex:RiskSdk:1.0.10"
```

## Usage

#### Quick start

The SDK must be started as early as possible in your application lifecycle. We recommend calling the `start` method in your `Application` subclass's `onCreate`.

**For Payment Acceptance (PA) scenario:**

```kotlin
import android.app.Application
import com.airwallex.risk.AirwallexRisk
import com.airwallex.risk.RiskConfiguration
import com.airwallex.risk.Tenant

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AirwallexRisk.start(
            applicationContext = applicationContext,
            accountId = "YOUR_MERCHANT_ACCOUNT_ID", // Required: The PA merchant's account ID at Airwallex
            configuration = RiskConfiguration(
                isProduction = !BuildConfig.DEBUG,
                tenant = Tenant.PA
            )
        )
    }
}
```

**For Connected Accounts scenario:**

```kotlin
import android.app.Application
import com.airwallex.risk.AirwallexRisk
import com.airwallex.risk.RiskConfiguration
import com.airwallex.risk.Tenant

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AirwallexRisk.start(
            applicationContext = applicationContext,
            accountId = null, // Optional: Set connected account ID later via AirwallexRisk.setAccountId()
            configuration = RiskConfiguration(
                isProduction = !BuildConfig.DEBUG,
                tenant = Tenant.SCALE
            )
        )
    }
}
```

**Notes**:
- **Payment Acceptance**:
  - `accountId` is **required** and should be the PA merchant's account ID at Airwallex
  - `tenant` must be set to `Tenant.PA`
- **Connected Accounts**:
  - `accountId` is **optional** at startup (set it later via `AirwallexRisk.setAccountId()` once the platform user signs in and the connected account is available)
  - `tenant` must be set to `Tenant.SCALE`
- The optional `RiskConfiguration` may also be used if needed. For test/debug builds you can set `isProduction = false`.

#### Update user

The SDK needs to be updated when users sign in or out.

**For Payment Acceptance (PA) scenario:**

There are two cases for setting user ID:

**Case 1: Registered user checkout (Recommended)**

When using [registered user checkout](https://www.airwallex.com/docs/payments__create-a-customer), set the user ID to the Airwallex Customer ID after creating the customer:

```kotlin
import com.airwallex.risk.AirwallexRisk

// After creating a customer at Airwallex via Create a Customer API
AirwallexRisk.setUserId(userId = "AIRWALLEX_CUSTOMER_ID") // Set to Airwallex Customer ID
AirwallexRisk.setUserId(userId = null) // Set to null on sign out
```

:warning: **Important**: The user ID should be the **Airwallex Customer ID** returned from the [Create a Customer API](https://www.airwallex.com/docs/payments__create-a-customer).

**Case 2: Guest checkout**

For guest checkout flows where users don't register, you can skip setting the user ID:

```kotlin
// No need to call AirwallexRisk.setUserId() for guest checkout
```

**For Connected Accounts scenario:**

**Required:** When a platform user (connected account) signs in or out, set only the account ID:

```kotlin
import com.airwallex.risk.AirwallexRisk

// On platform user sign in
val connectedAccountId = "CONNECTED_ACCOUNT_ID" // The connected account's Airwallex account ID from Create a connected account API
AirwallexRisk.setAccountId(accountId = connectedAccountId)

// On platform user sign out
AirwallexRisk.setAccountId(accountId = null)
```

:warning: **Important**: For Connected Accounts, do **NOT** set `userId`. Only set `accountId` to the connected account's Airwallex account ID (not the platform's account ID). The account ID is the `id` returned from the [Create a connected account API](https://www.airwallex.com/docs/api#/Scale/Accounts/_api_v1_accounts_create/post).
  
#### Events

Some app events must be logged to the SDK. These events include:
- _User logs in:_ When a user logs in, send the event "login". Make sure you **set the user ID** (above) before sending this event.
- _Create a payout transaction:_ When a user submits a payment transaction, send the event "payout".

Use the following snippet to send event name and current screen name.

```kotlin
import com.airwallex.risk.AirwallexRisk

AirwallexRisk.log(
  event = "EVENT_NAME",
  screen = "SCREEN_NAME"
)
```

##### Standardized Event Names

To ensure consistency across integrations, use these standardized event names for common scenarios:

| **Section** | **Event Name** | **Description** | **When to Use** |
|-------------|----------------|-----------------|-----------------|
| **Transaction Events** |
| | `transaction_initiated` | User starts a new transaction flow | When user begins any payment/transfer process, before entering details |
| **Card Security Events** |
| | `card_pin_viewed` | User accessed/viewed card PIN | When user successfully views their card PIN through app |
| | `card_cvc_viewed` | User accessed/viewed card CVC/CVV | When user successfully views their card CVC/security code |
| **Profile Update Events** |
| | `profile_phone_updated` | User changed their phone number | When phone number change is successfully saved |
| | `profile_email_updated` | User changed their email address | When email change is successfully saved |

**Naming Convention:**
- Format: `category_action` (lowercase, underscore-separated)
- Tense: Past tense for completed actions (`viewed`, `updated`, `initiated`)

**Example Implementation:**
```kotlin
AirwallexRisk.log(event = "transaction_initiated", screen = "YOUR_APP_SCREEN_NAME")
```

#### Request header

When your app sends a request to Airwallex, you must add the provided header into your request before sending. This will be slightly different depending on your networking library, so check the documentation describing how to add headers for the library you're using.

Some networking libraries you may be using
- [Ktor](https://ktor.io/docs/request.html#parameters)
- [OkHttp](https://square.github.io/okhttp/4.x/okhttp/okhttp3/-request/-builder/add-header)

```kotlin
import com.airwallex.risk.AirwallexRisk

val header = AirwallexRisk.header

// Add a header using `header.field` and `header.value` to airwallex.com network requests
```

The header consists of
- the field, which will always be `"x-risk-device-id"`, and
- the value, which will be an internally generated device identifier.
