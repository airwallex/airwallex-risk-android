package com.airwallex.risk

import android.content.SharedPreferences
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class StorageManagerTest {

    @MockK lateinit var sharedPreferences: SharedPreferences

    private val editor: SharedPreferences.Editor = mockk()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { editor.putString(any(), any()) } returns editor
        every { editor.putBoolean(any(), any()) } returns editor
        every { editor.apply() } returns Unit

        every { sharedPreferences.edit() } returns editor
        every { sharedPreferences.getString(any(), null) } returns null
    }

    @Test
    fun `test device id - null initial value - auto create new uuid`() {
        every { sharedPreferences.getString(any(), null) } returns null

        val storageManager = StorageManager(sharedPreferences = sharedPreferences)

        val value1 = storageManager.deviceId
        assertNotNull(value1)

        verify { editor.putString(any(), any()) }
    }

    @Test
    fun `test device id - already set`() {
        val generatedUUID = UUID.randomUUID()
        every { sharedPreferences.getString(any(), null) } returns generatedUUID.toString()

        val storageManager = StorageManager(sharedPreferences = sharedPreferences)
        assertEquals(storageManager.deviceId, generatedUUID)
    }

    @Test
    fun `test user ID`() {
        val userId = UUID.randomUUID().toString()

        every { sharedPreferences.getString(any(), null) } returns null

        val storageManager = StorageManager(sharedPreferences = sharedPreferences)

        assertNull(storageManager.userId)

        every { sharedPreferences.getString(any(), null) } returns userId
        storageManager.userId = userId
        assertEquals(storageManager.userId, userId)
    }

    @Test
    fun `test account ID`() {
        val accountId = UUID.randomUUID().toString()

        every { sharedPreferences.getString(any(), null) } returns null

        val storageManager = StorageManager(sharedPreferences = sharedPreferences)

        assertNull(storageManager.accountId)

        every { sharedPreferences.getString(any(), null) } returns accountId
        storageManager.accountId = accountId
        assertEquals(storageManager.accountId, accountId)
    }

    @Test
    fun `test installation event`() {
        every { sharedPreferences.getBoolean(any(), false) } returns false

        val storageManager = StorageManager(sharedPreferences = sharedPreferences)

        assertFalse(storageManager.hasSentInstallationEvent)

        every { sharedPreferences.getBoolean(any(), false) } returns true
        storageManager.hasSentInstallationEvent = true
        assertTrue(storageManager.hasSentInstallationEvent)
    }
}