package com.example.clubbers.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clubbers.data.repos.ParticipatesRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class ParticipatesViewModel @Inject constructor(
    private val repository: ParticipatesRepository
) : ViewModel() {

    fun getParticipants(eventId: Int) = viewModelScope.launch {
        repository.getParticipants(eventId)
    }

    fun getEvents(userId: Int) = viewModelScope.launch {
        repository.getEvents(userId)
    }

    fun addNewParticipant(userId: Int, eventId: Int) = viewModelScope.launch {
        repository.insert(userId, eventId)
    }

    fun deleteParticipant(userId: Int, eventId: Int) = viewModelScope.launch {
        repository.delete(userId, eventId)
    }

    fun deleteAllParticipantsFromEvent(eventId: Int) = viewModelScope.launch {
        repository.deleteAll(eventId)
    }
}