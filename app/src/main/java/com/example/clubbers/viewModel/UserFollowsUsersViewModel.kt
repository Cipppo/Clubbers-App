package com.example.clubbers.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clubbers.data.entities.User
import com.example.clubbers.data.entities.UserFollowsUser
import com.example.clubbers.data.repos.UserFollowsUsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserFollowsUsersViewModel @Inject constructor(
    private val repository: UserFollowsUsersRepository
) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> get() = _users

    fun getFollowers(userId: Int) = viewModelScope.launch {
        repository.getFollowers(userId)
            .collect { users ->
                _users.value = users
            }
    }

    fun getFollowed(userId: Int) = viewModelScope.launch {
        repository.getFollowed(userId)
            .collect { users ->
                _users.value = users
            }
    }

    fun insert(userFollowsUser: UserFollowsUser) = viewModelScope.launch {
        repository.insert(userFollowsUser)
    }

    fun delete(userFollowsUser: UserFollowsUser) = viewModelScope.launch {
        repository.delete(userFollowsUser)
    }
}