package com.airwallex.risk

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.testing.TestLifecycleOwner
import com.airwallex.risk.helpers.FakeAutomaticEventProvider
import com.airwallex.risk.helpers.Fixtures
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class EventManagerTest {

    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun `test sending events`() = runTest {
        val repository = EventRepository()
        val apiService: ApiService = mockk()
        val automaticEventProvider = FakeAutomaticEventProvider()
        val lifecycleOwner = TestLifecycleOwner()

        coEvery { apiService.postEvents(any()) } just runs

        val eventManager = EventManager(
            repository = repository,
            apiService = apiService,
            automaticEventProvider = automaticEventProvider,
            bufferTimeMillis = 20_000L,
            lifecycleOwner = lifecycleOwner,
            repeatCount = 1,
            backgroundDispatcher = dispatcher
        )

        eventManager.start()

        val event = Fixtures.createEvent(eventType = "login", path = "path")
        eventManager.queue(event)

        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_START)
        advanceTimeBy(20_000)

        assertEquals(repository.popAll(), emptyList())
    }

    @Test
    fun `test sending events - failure`() = runTest {
        val repository = EventRepository()
        val apiService: ApiService = mockk()
        val automaticEventProvider = FakeAutomaticEventProvider()
        val lifecycleOwner = TestLifecycleOwner()

        coEvery { apiService.postEvents(any()) } throws IllegalArgumentException()

        val eventManager = EventManager(
            repository = repository,
            apiService = apiService,
            automaticEventProvider = automaticEventProvider,
            bufferTimeMillis = 20_000L,
            lifecycleOwner = lifecycleOwner,
            repeatCount = 1,
            backgroundDispatcher = dispatcher
        )

        eventManager.start()

        val event = Fixtures.createEvent(eventType = "login")
        eventManager.queue(event)

        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_START)
        advanceTimeBy(20_000)

        assertEquals(repository.popAll(), listOf(event))
    }
}