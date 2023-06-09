package com.example.clubbers.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clubbers.data.entities.Event
import com.example.clubbers.data.entities.Participates
import com.example.clubbers.data.entities.User
import com.example.clubbers.data.repos.ParticipatesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ParticipatesViewModel @Inject constructor(
    private val repository: ParticipatesRepository
) : ViewModel() {

    fun getParticipants(eventId: Int): Flow<List<User>> = repository.getParticipants(eventId)

    fun getEvents(userId: Int): Flow<List<Event>> = repository.getEvents(userId)

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