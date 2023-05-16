package com.example.clubbers.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clubbers.data.entities.Event
import com.example.clubbers.data.repos.EventsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val repository: EventsRepository
) : ViewModel() {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> get() = _events
    fun getAllEvents() = viewModelScope.launch {
        repository.events.collect() { events ->
            _events.value = events
        }
    }

    fun insertNewEvent(event: Event) = viewModelScope.launch {
        repository.insertNewEvent(event)
    }

    private var _eventSelected: Event? = null
    val eventSelected
        get() = _eventSelected

    fun selectEvent(event: Event) {
        _eventSelected = event
    }

    fun deleteEvent(event: Event) = viewModelScope.launch {
        repository.deleteEvent(event)
    }

    fun getEventById(eventId: Int) = viewModelScope.launch {
        repository.getEventById(eventId)
            .collect {
                _eventSelected = it
            }
    }

    fun getEventsByName(eventName: String) = viewModelScope.launch {
        repository.getEventsByName(eventName)
            .collect {
                _events.value = it
            }
    }

    fun updateEvent(event: Event) = viewModelScope.launch {
        repository.updateEvent(event)
    }
}