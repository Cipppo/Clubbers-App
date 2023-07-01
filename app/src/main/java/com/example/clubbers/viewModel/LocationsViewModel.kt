package com.example.clubbers.viewModel

import androidx.lifecycle.ViewModel
import com.example.clubbers.data.details.LocationDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LocationsViewModel: ViewModel() {
    private val _location = MutableStateFlow(LocationDetails("", 0.0, 0.0))
    val location: StateFlow<LocationDetails> get() = _location

    fun setEventLocation(location: LocationDetails) {
        _location.value = location
    }
}