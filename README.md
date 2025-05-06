# Airwallex Risk Android SDK

![Bitrise](https://app.bitrise.io/app/311ac459dba8618d/status.svg?token=PdOUUoDjBwL_Z5NEKGiMmQ&branch=main)
![Platforms](https://img.shields.io/badge/platforms-Android-333333.svg)

The Airwallex Risk SDK is required for Airwallex Scale customer apps.

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

The SDK must be started as early as possible in your application lifecycle. We recommend calling the `start` method in your `Application` subclass's `onCreate`:

```kotlin
import android.app.Application
import com.airwallex.risk.AirwallexRisk
import com.airwallex.risk.RiskConfiguration

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val accountId = null // "YOUR-ACCOUNT-ID-GOES-HERE"

        AirwallexRisk.start(
            applicationContext = applicationContext,
            accountId = accountId,
            configuration = RiskConfiguration(isProduction = !BuildConfig.DEBUG)
        )
    }
}
```

**Notes**: 
- `accountId` is required in all Airwallex Scale customer apps. This will be your Airwallex account ID.
- the optional `RiskConfiguration` may also be used if needed. For test/debug builds you can set `isProduction = false`.

#### Update user

After the app user signs in to their Airwallex account, the app must send the users ID through to the SDK as follows. This will be persisted in the SDK until the next time this method is called. Set to `null` on sign out.

:warning: **Important**: The user ID here is the signed in user's individual Airwallex account ID, not your own system user ID.

```kotlin
import com.airwallex.risk.AirwallexRisk

AirwallexRisk.setUserId(userID = "USER_ID")
```
  
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
