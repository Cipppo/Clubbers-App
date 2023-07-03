package com.example.clubbers.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clubbers.data.entities.Event
import com.example.clubbers.data.entities.EventHasTag
import com.example.clubbers.data.entities.Tag
import com.example.clubbers.data.repos.EventHasTagsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventHasTagsViewModel @Inject constructor(
    private val repository: EventHasTagsRepository
) : ViewModel() {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> get() = _events

    private val _tags = MutableStateFlow<List<Tag>>(emptyList())
    val tags: StateFlow<List<Tag>?> get() = _tags

    fun getTagsByEventId(eventId: Int) = viewModelScope.launch {
        repository.getTagsByEventId(eventId)
            .collect { tags ->
                _tags.value = tags
            }
    }

    fun getTagsForEvents(eventIds: List<Int>) = viewModelScope.launch {
        repository.getTagsForEvents(eventIds)
            .collect { tags ->
                _tags.value = tags
            }
    }

    fun getEventsByTagId(tagId: Int) = viewModelScope.launch {
        repository.getEventsByTagId(tagId)
            .collect { events ->
                _events.value = events
            }
    }

    fun getEventsByTagName(tagName: String) = viewModelScope.launch {
        repository.getEventsForTagName(tagName)
            .collect { events ->
                _events.value = events
            }
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