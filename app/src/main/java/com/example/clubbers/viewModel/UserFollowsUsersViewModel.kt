package com.example.clubbers.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clubbers.data.entities.UserFollowsUser
import com.example.clubbers.data.repos.UserFollowsUsersRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserFollowsUsersViewModel @Inject constructor(
    private val repository: UserFollowsUsersRepository
) : ViewModel() {

    fun getFollowers(userId: Int) = viewModelScope.launch {
        repository.getFollowers(userId)
    }

    fun getFollowed(userId: Int) = viewModelScope.launch {
        repository.getFollowed(userId)
    }

    fun insert(userFollowsUser: UserFollowsUser) = viewModelScope.launch {
        repository.insert(userFollowsUser)
    }

    fun delete(userFollowsUser: UserFollowsUser) = viewModelScope.launch {
        repository.delete(userFollowsUser)
    }
}