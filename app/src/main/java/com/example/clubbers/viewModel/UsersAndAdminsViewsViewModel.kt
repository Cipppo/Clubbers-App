package com.example.clubbers.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clubbers.data.repos.UsersAndAdminsViewsRepository
import com.example.clubbers.data.views.UsersAndAdminsView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersAndAdminsViewsViewModel @Inject constructor(
   private val repository: UsersAndAdminsViewsRepository
) : ViewModel() {

    private val _userAndAdmin = MutableStateFlow<UsersAndAdminsView?>(null)
    val userAndAdmin: StateFlow<UsersAndAdminsView?> get() = _userAndAdmin


    val usersAndAdmins = repository.usersAndAdmins

    fun getUserById(userId: Int) = viewModelScope.launch {
        repository.getUserById(userId)
            .collect { user ->
                _userAndAdmin.value = user
            }
    }

    fun getUserByUserName(userName: String) = viewModelScope.launch {
        repository.getUserByUserName(userName)
            .collect { user ->
                _userAndAdmin.value = user
            }
    }

    fun getUserByEmail(userEmail: String) = viewModelScope.launch {
        repository.getUserByEmail(userEmail)
            .collect { user ->
                _userAndAdmin.value = user
            }
    }

    fun isAdmin(userName: String) = repository.isAdmin(userName)
}