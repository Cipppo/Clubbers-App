package com.example.clubbers.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clubbers.data.entities.Admin
import com.example.clubbers.data.entities.User
import com.example.clubbers.data.entities.UserFollowsAdmin
import com.example.clubbers.data.repos.UserFollowsAdminsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserFollowsAdminsViewModel @Inject constructor(
    private val repository: UserFollowsAdminsRepository
) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> get() = _users

    private val _admins = MutableStateFlow<List<Admin>>(emptyList())
    val admins: StateFlow<List<Admin>> get() = _admins

    fun getUsers(adminId: Int) = viewModelScope.launch {
        repository.getUsers(adminId)
            .collect { users ->
                _users.value = users
            }
    }

    fun getAdmins(userId: Int) = viewModelScope.launch {
        repository.getAdmins(userId)
            .collect { admins ->
                _admins.value = admins
            }
    }

    fun insert(userFollowsAdmin: UserFollowsAdmin) = viewModelScope.launch {
        repository.insert(userFollowsAdmin)
    }

    fun delete(userFollowsAdmin: UserFollowsAdmin) = viewModelScope.launch {
        repository.delete(userFollowsAdmin)
    }
}