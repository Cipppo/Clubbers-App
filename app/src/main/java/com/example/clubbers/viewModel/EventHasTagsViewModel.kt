package com.example.clubbers.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clubbers.data.entities.Event
import com.example.clubbers.data.entities.EventHasTag
import com.example.clubbers.data.entities.Tag
import com.example.clubbers.data.repos.EventHasTagsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventHasTagsViewModel @Inject constructor(
    private val repository: EventHasTagsRepository
) : ViewModel() {

    fun getTagsByEventId(eventId: Int): Flow<List<Tag>> = repository.getTagsByEventId(eventId)

    fun getEventsByTagId(tagId: Int): Flow<List<Event>> = repository.getEventsByTagId(tagId)

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