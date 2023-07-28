package com.airwallex.risk

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

internal class EventManager(
    private val repository: EventRepository,
    private val apiService: ApiService,
    private val automaticEventProvider: AutomaticEventProvider,
    backgroundDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val repeatingScope = CoroutineScope(backgroundDispatcher)
    private var repeatingJob: Job? = null

    fun start() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
                startRepeatingJob()
            }

            override fun onStop(owner: LifecycleOwner) {
                super.onStop(owner)
                stopRepeatingJob()
            }
        })

        startRepeatingJob()
        automaticEventProvider.sendOpenEvent()
    }

    fun queue(event: Event) {
        repository.add(event)
    }

    private fun startRepeatingJob() {
        if (repeatingJob != null) {
            return
        }

        repeatingJob = repeatingScope.launch {
            repeat(Int.MAX_VALUE) {
                if (isActive) {
                    postEvents()
                    // delay for 10 seconds
                    delay(10_000L)
                }
            }
        }
    }

    private fun stopRepeatingJob() {
        repeatingJob?.cancel()
        repeatingJob = null
    }

    private suspend fun postEvents() {
        val events = repository.popAll()

        if (events.isNotEmpty()) {
            try {
                apiService.postEvents(events = events)
            } catch (e: Exception) {
                repository.addAll(events)
            }
        }
    }
}