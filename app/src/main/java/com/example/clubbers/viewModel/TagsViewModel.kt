package com.example.clubbers.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clubbers.data.entities.Tag
import com.example.clubbers.data.repos.TagsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagsViewModel @Inject constructor(
    private val repository: TagsRepository
) : ViewModel() {

    val tags = repository.tags

    fun addNewTag(tag: Tag) = viewModelScope.launch {
        repository.insertNewTag(tag)
    }

    private var _tagSelected: Tag? = null
    val tagSelected
        get() = _tagSelected

    fun selectTag(tag: Tag) {
        _tagSelected = tag
    }

    fun updateTag(tag: Tag) = viewModelScope.launch {
        repository.updateTag(tag)
    }

    fun deleteTag(tag: Tag) = viewModelScope.launch {
        repository.deleteTag(tag)
    }

    fun getTagById(tagId: Int) = viewModelScope.launch {
        repository.getTagById(tagId)
            .collect { tag ->
                _tagSelected = tag
            }
    }
}