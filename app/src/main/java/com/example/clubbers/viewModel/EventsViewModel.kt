package com.example.clubbers.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clubbers.data.entities.Event
import com.example.clubbers.data.repos.EventsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventsViewModel @Inject constructor(
    private val repository: EventsRepository
) : ViewModel() {

    val events = repository.events

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
    }

    fun updateEvent(event: Event) = viewModelScope.launch {
        repository.updateEvent(event)
    }
}