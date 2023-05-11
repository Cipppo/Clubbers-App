package com.example.clubbers.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clubbers.data.entities.Event
import com.example.clubbers.data.entities.Participates
import com.example.clubbers.data.entities.User
import com.example.clubbers.data.repos.ParticipatesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParticipatesViewModel @Inject constructor(
    private val repository: ParticipatesRepository
) : ViewModel() {

    private val _participants = MutableStateFlow(emptyList<User>())
    val participants: StateFlow<List<User>> get() = _participants

    private val _events = MutableStateFlow(emptyList<Event>())
    val events: StateFlow<List<Event>> get() = _events

    fun getParticipants(eventId: Int) = viewModelScope.launch {
        repository.getParticipants(eventId)
            .collect { participants ->
                _participants.value = participants
            }
    }

    fun getEvents(userId: Int) = viewModelScope.launch {
        repository.getEvents(userId)
            .collect { events ->
                _events.value = events
            }
    }

    fun addNewParticipant(participates: Participates) = viewModelScope.launch {
        repository.insert(participates)
    }

    fun deleteParticipant(participates: Participates) = viewModelScope.launch {
        repository.delete(participates)
    }

    fun deleteAllParticipantsFromEvent(eventId: Int) = viewModelScope.launch {
        repository.deleteAll(eventId)
    }
}