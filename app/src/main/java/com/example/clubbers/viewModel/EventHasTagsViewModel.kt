package com.example.clubbers.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clubbers.data.entities.EventHasTag
import com.example.clubbers.data.repos.EventHasTagsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventHasTagsViewModel @Inject constructor(
    private val repository: EventHasTagsRepository
) : ViewModel() {

    fun getTagsByEventId(eventId: Int) = viewModelScope.launch {
        repository.getTagsByEventId(eventId)
    }

    fun getEventsByTagId(tagId: Int) = viewModelScope.launch {
        repository.getEventsByTagId(tagId)
    }

    fun addNewTagToEvent(eventHasTag: EventHasTag) = viewModelScope.launch {
        repository.insert(eventHasTag)
    }

    fun addMultipleTagsToEvent(eventHasTags: List<EventHasTag>) = viewModelScope.launch {
        repository.insertMultiple(eventHasTags)
    }

    fun deleteTagFromEvent(eventHasTag: EventHasTag) = viewModelScope.launch {
        repository.delete(eventHasTag)
    }

    fun deleteAllTagsForEvent(eventId: Int) = viewModelScope.launch {
        repository.deleteAll(eventId)
    }
}