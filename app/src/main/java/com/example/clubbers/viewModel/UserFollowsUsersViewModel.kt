package com.example.clubbers.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clubbers.data.entities.User
import com.example.clubbers.data.entities.UserFollowsUser
import com.example.clubbers.data.repos.UserFollowsUsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserFollowsUsersViewModel @Inject constructor(
    private val repository: UserFollowsUsersRepository
) : ViewModel() {

    fun getFollowers(userId: Int): Flow<List<User>> = repository.getFollowers(userId)

    fun getFollowed(userId: Int): Flow<List<User>> = repository.getFollowed(userId)

    fun insert(userFollowsUser: UserFollowsUser) = viewModelScope.launch {
        repository.insert(userFollowsUser)
    }

    fun delete(userFollowsUser: UserFollowsUser) = viewModelScope.launch {
        repository.delete(userFollowsUser)
    }
}