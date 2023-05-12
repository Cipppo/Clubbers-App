package com.example.clubbers.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LocationsViewModel: ViewModel() {
    private val _location = MutableStateFlow("")


    val location: StateFlow<String> get() = _location

    fun setEventLocation(location: String) {
        _location.value = location
    }
}