package com.example.clubbers.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clubbers.data.entities.Event
import com.example.clubbers.data.repos.EventsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
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

    fun getEventById(eventId: Int): Flow<Event> = repository.getEventById(eventId)

    fun updateEvent(event: Event) = viewModelScope.launch {
        repository.updateEvent(event)
    }
}