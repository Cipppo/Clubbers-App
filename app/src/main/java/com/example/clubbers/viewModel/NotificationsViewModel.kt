package com.example.clubbers.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clubbers.data.entities.Notification
import com.example.clubbers.data.repos.NotificationsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val repository: NotificationsRepository
) : ViewModel(){

    private var _allNotifications =  MutableStateFlow<List<Notification>>(emptyList())

    val allNotifications: StateFlow<List<Notification?>> get() = _allNotifications

    fun getAllNotifications() = viewModelScope.launch{
        repository.getNotifications().collect {
            notification -> _allNotifications.value = notification
        }
    }

    private var _allUserNotifications = MutableStateFlow<List<Notification>>(emptyList())

    val allUserNotifications: StateFlow<List<Notification>> get() = _allUserNotifications

    fun getAllUserNotifications(userId: Int) = viewModelScope.launch{
        repository.getAllUserNotifications(userId).collect {
            notification -> _allUserNotifications.value = notification
        }
    }

    fun setNotificationAsRead(notificationId: Int) = viewModelScope.launch {
        repository.setNotificationAsRead(notificationId)
    }

    fun readAllNotifications(userId: Int) = viewModelScope.launch {
        repository.readAllNotifications(userId)
    }

}