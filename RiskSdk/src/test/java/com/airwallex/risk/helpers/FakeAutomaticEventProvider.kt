package com.airwallex.risk.helpers

import com.airwallex.risk.IAutomaticEventProvider

class FakeAutomaticEventProvider : IAutomaticEventProvider {

    override fun sendOpenEvent() {
        // no op
    }
}