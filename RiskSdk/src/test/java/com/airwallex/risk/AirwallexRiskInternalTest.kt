package com.airwallex.risk

import android.util.Log
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Before
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals

class AirwallexRiskInternalTest {

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
    }

    @Test
    fun `test start`() {
        val riskContext: RiskContext = mockk()
        every { riskContext.deviceId } returns UUID.randomUUID()
        every { riskContext.sessionId } returns UUID.randomUUID()
        every { riskContext.description() } returns "description"
        val eventManager: EventManager = mockk()
        every { eventManager.start() } returns mockk()

        val shared = AirwallexRiskInternal(
            riskContext = riskContext,
            eventManager = eventManager
        )

        shared.start()
        verify { eventManager.start() }
    }

    @Test
    fun `test set user id from null triggers login event only`() {
        val riskContext: RiskContext = mockk()
        every { riskContext.deviceId } returns UUID.randomUUID()
        every { riskContext.sessionId } returns UUID.randomUUID()
        every { riskContext.description() } returns "description"
        every { riskContext.userId } returns null
        every { riskContext.updateUserId(any()) } returns mockk()
        every { riskContext.createEvent(any(), any(), any()) } returns mockk()

        val eventManager: EventManager = mockk()
        every { eventManager.queue(any()) } returns Unit

        val shared = AirwallexRiskInternal(
            riskContext = riskContext,
            eventManager = eventManager
        )

        val userId = "abcde"
        shared.setUserId(userId)
        verify { riskContext.updateUserId(userId) }
        verify(exactly = 1) { riskContext.createEvent(Constants.userLoginEventName, null, any()) }
        verify(exactly = 0) { riskContext.createEvent(Constants.userLogoutEventName, null, any()) }
        verify(exactly = 1) { eventManager.queue(any()) }
    }

    @Test
    fun `test set user id to null from existing triggers logout event only`() {
        val riskContext: RiskContext = mockk()
        every { riskContext.deviceId } returns UUID.randomUUID()
        every { riskContext.sessionId } returns UUID.randomUUID()
        every { riskContext.description() } returns "description"
        every { riskContext.userId } returns "existingUser"
        every { riskContext.updateUserId(any()) } returns mockk()
        every { riskContext.createEvent(any(), any(), any()) } returns mockk()

        val eventManager: EventManager = mockk()
        every { eventManager.queue(any()) } returns Unit

        val shared = AirwallexRiskInternal(
            riskContext = riskContext,
            eventManager = eventManager
        )

        shared.setUserId(null)
        verify { riskContext.createEvent(Constants.userLogoutEventName, null, any()) }
        verify { riskContext.updateUserId(null) }
        verify(exactly = 0) { riskContext.createEvent(Constants.userLoginEventName, null, any()) }
        verify(exactly = 1) { eventManager.queue(any()) }
    }

    @Test
    fun `test set user id switching users triggers logout then login`() {
        val riskContext: RiskContext = mockk()
        every { riskContext.deviceId } returns UUID.randomUUID()
        every { riskContext.sessionId } returns UUID.randomUUID()
        every { riskContext.description() } returns "description"
        every { riskContext.userId } returns "existingUser"
        every { riskContext.updateUserId(any()) } returns mockk()
        every { riskContext.createEvent(any(), any(), any()) } returns mockk()

        val eventManager: EventManager = mockk()
        every { eventManager.queue(any()) } returns Unit

        val shared = AirwallexRiskInternal(
            riskContext = riskContext,
            eventManager = eventManager
        )

        shared.setUserId("newUser")
        verifyOrder {
            riskContext.createEvent(Constants.userLogoutEventName, null, any())
            eventManager.queue(any())
            riskContext.updateUserId("newUser")
            riskContext.createEvent(Constants.userLoginEventName, null, any())
            eventManager.queue(any())
        }
    }

    @Test
    fun `test set user id to same value does nothing`() {
        val riskContext: RiskContext = mockk()
        every { riskContext.deviceId } returns UUID.randomUUID()
        every { riskContext.sessionId } returns UUID.randomUUID()
        every { riskContext.description() } returns "description"
        every { riskContext.userId } returns "sameUser"

        val eventManager: EventManager = mockk()

        val shared = AirwallexRiskInternal(
            riskContext = riskContext,
            eventManager = eventManager
        )

        shared.setUserId("sameUser")
        verify(exactly = 0) { riskContext.updateUserId(any()) }
        verify(exactly = 0) { eventManager.queue(any()) }
    }

    @Test
    fun `test set account id for scale tenant from null triggers login event`() {
        val riskContext: RiskContext = mockk()
        every { riskContext.deviceId } returns UUID.randomUUID()
        every { riskContext.sessionId } returns UUID.randomUUID()
        every { riskContext.description() } returns "description"
        every { riskContext.tenant } returns Tenant.SCALE
        every { riskContext.accountId } returns null
        every { riskContext.updateAccountId(any()) } returns mockk()
        every { riskContext.createEvent(any(), any(), any()) } returns mockk()

        val eventManager: EventManager = mockk()
        every { eventManager.queue(any()) } returns Unit

        val shared = AirwallexRiskInternal(
            riskContext = riskContext,
            eventManager = eventManager
        )

        shared.setAccountId("newAccountId")
        verify { riskContext.updateAccountId("newAccountId") }
        verify(exactly = 1) { riskContext.createEvent(Constants.accountLoginEventName, null, any()) }
        verify(exactly = 0) { riskContext.createEvent(Constants.accountLogoutEventName, null, any()) }
        verify(exactly = 1) { eventManager.queue(any()) }
    }

    @Test
    fun `test set account id for scale tenant to null triggers logout event`() {
        val riskContext: RiskContext = mockk()
        every { riskContext.deviceId } returns UUID.randomUUID()
        every { riskContext.sessionId } returns UUID.randomUUID()
        every { riskContext.description() } returns "description"
        every { riskContext.tenant } returns Tenant.SCALE
        every { riskContext.accountId } returns "existingAccount"
        every { riskContext.updateAccountId(any()) } returns mockk()
        every { riskContext.createEvent(any(), any(), any()) } returns mockk()

        val eventManager: EventManager = mockk()
        every { eventManager.queue(any()) } returns Unit

        val shared = AirwallexRiskInternal(
            riskContext = riskContext,
            eventManager = eventManager
        )

        shared.setAccountId(null)
        verify { riskContext.createEvent(Constants.accountLogoutEventName, null, any()) }
        verify { riskContext.updateAccountId(null) }
        verify(exactly = 0) { riskContext.createEvent(Constants.accountLoginEventName, null, any()) }
        verify(exactly = 1) { eventManager.queue(any()) }
    }

    @Test
    fun `test set account id for scale tenant switching accounts triggers logout then login`() {
        val riskContext: RiskContext = mockk()
        every { riskContext.deviceId } returns UUID.randomUUID()
        every { riskContext.sessionId } returns UUID.randomUUID()
        every { riskContext.description() } returns "description"
        every { riskContext.tenant } returns Tenant.SCALE
        every { riskContext.accountId } returns "existingAccount"
        every { riskContext.updateAccountId(any()) } returns mockk()
        every { riskContext.createEvent(any(), any(), any()) } returns mockk()

        val eventManager: EventManager = mockk()
        every { eventManager.queue(any()) } returns Unit

        val shared = AirwallexRiskInternal(
            riskContext = riskContext,
            eventManager = eventManager
        )

        shared.setAccountId("newAccountId")
        verifyOrder {
            riskContext.createEvent(Constants.accountLogoutEventName, null, any())
            eventManager.queue(any())
            riskContext.updateAccountId("newAccountId")
            riskContext.createEvent(Constants.accountLoginEventName, null, any())
            eventManager.queue(any())
        }
    }

    @Test
    fun `test set account id for scale tenant to same value does nothing`() {
        val riskContext: RiskContext = mockk()
        every { riskContext.deviceId } returns UUID.randomUUID()
        every { riskContext.sessionId } returns UUID.randomUUID()
        every { riskContext.description() } returns "description"
        every { riskContext.tenant } returns Tenant.SCALE
        every { riskContext.accountId } returns "sameAccount"

        val eventManager: EventManager = mockk()

        val shared = AirwallexRiskInternal(
            riskContext = riskContext,
            eventManager = eventManager
        )

        shared.setAccountId("sameAccount")
        verify(exactly = 0) { riskContext.updateAccountId(any()) }
        verify(exactly = 0) { eventManager.queue(any()) }
    }

    @Test
    fun `test set account id for non-scale tenant does not trigger events`() {
        val riskContext: RiskContext = mockk()
        every { riskContext.deviceId } returns UUID.randomUUID()
        every { riskContext.sessionId } returns UUID.randomUUID()
        every { riskContext.description() } returns "description"
        every { riskContext.tenant } returns Tenant.AIRWALLEX_MOBILE
        every { riskContext.updateAccountId(any()) } returns mockk()

        val eventManager: EventManager = mockk()

        val shared = AirwallexRiskInternal(
            riskContext = riskContext,
            eventManager = eventManager
        )

        shared.setAccountId("newAccountId")
        verify { riskContext.updateAccountId("newAccountId") }
        verify(exactly = 0) { eventManager.queue(any()) }
    }

    @Test
    fun `test set account id for PA tenant does not trigger events`() {
        val riskContext: RiskContext = mockk()
        every { riskContext.deviceId } returns UUID.randomUUID()
        every { riskContext.sessionId } returns UUID.randomUUID()
        every { riskContext.description() } returns "description"
        every { riskContext.tenant } returns Tenant.PA
        every { riskContext.updateAccountId(any()) } returns mockk()

        val eventManager: EventManager = mockk()

        val shared = AirwallexRiskInternal(
            riskContext = riskContext,
            eventManager = eventManager
        )

        shared.setAccountId("newAccountId")
        verify { riskContext.updateAccountId("newAccountId") }
        verify(exactly = 0) { eventManager.queue(any()) }

        shared.setAccountId(null)
        verify { riskContext.updateAccountId(null) }
        verify(exactly = 0) { eventManager.queue(any()) }
    }

    @Test
    fun `test log`() {
        val riskContext: RiskContext = mockk()
        every { riskContext.deviceId } returns UUID.randomUUID()
        every { riskContext.sessionId } returns UUID.randomUUID()
        every { riskContext.description() } returns "description"
        every { riskContext.updateAccountId(any()) } returns mockk()
        every { riskContext.createEvent(any(), any(), any()) } returns mockk()

        val eventManager: EventManager = mockk()

        every { eventManager.queue(any()) } returns Unit

        val shared = AirwallexRiskInternal(
            riskContext = riskContext,
            eventManager = eventManager
        )

        shared.log(eventType = "event", screen = "screen")
        verify { eventManager.queue(any()) }
    }

    @Test
    fun `test header`() {
        val deviceId = UUID.randomUUID()
        val riskContext: RiskContext = mockk()
        every { riskContext.deviceId } returns deviceId
        every { riskContext.sessionId } returns UUID.randomUUID()

        val eventManager: EventManager = mockk()

        val shared = AirwallexRiskInternal(
            riskContext = riskContext,
            eventManager = eventManager
        )

        assertEquals(shared.header.field, Constants.headerKey)
        assertEquals(shared.header.value, deviceId.toString())
    }

    @Test
    fun `test session id`() {
        val sessionId = UUID.randomUUID()
        val riskContext: RiskContext = mockk()
        every { riskContext.deviceId } returns UUID.randomUUID()
        every { riskContext.sessionId } returns sessionId

        val eventManager: EventManager = mockk()

        val shared = AirwallexRiskInternal(
            riskContext = riskContext,
            eventManager = eventManager
        )

        assertEquals(shared.sessionId, sessionId)
    }
}