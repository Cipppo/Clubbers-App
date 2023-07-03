package com.example.clubbers.ui

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.clubbers.data.details.TagsListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class EventLocationViewModel @Inject constructor(): ViewModel() {
    private var _title = mutableStateOf("")
    val title
        get() = _title

    private var _startEventDate = mutableStateOf<LocalDateTime?>(null)
    val startEventDate
        get() = _startEventDate

    private var _endEventDate = mutableStateOf<LocalDateTime?>(null)
    val endEventDate
        get() = _endEventDate

    private var _maxParticipants = mutableStateOf(0)
    val maxParticipants
        get() = _maxParticipants

    private var _capturedImageUri = mutableStateOf<Uri?>(null)
    val capturedImageUri
        get() = _capturedImageUri

    private var eventDescription = mutableStateOf("")
    val description
        get() = eventDescription

    private var _tags = mutableStateOf<List<TagsListItem>>(listOf())
    val tags
        get() = _tags


    fun setTitle(title: String) {
        _title.value = title
    }

    fun setStartEventDate(startEventDate: LocalDateTime) {
        _startEventDate.value = startEventDate
    }

    fun setEndEventDate(endEventDate: LocalDateTime) {
        _endEventDate.value = endEventDate
    }

    fun setMaxParticipants(maxParticipants: Int) {
        _maxParticipants.value = maxParticipants
    }

    fun setCapturedImageUri(capturedImageUri: Uri) {
        _capturedImageUri.value = capturedImageUri
    }

    fun setDescription(description: String) {
        eventDescription.value = description
    }

    fun setTags(tags: List<TagsListItem>) {
        _tags.value = tags
    }
}