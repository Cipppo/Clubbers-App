package com.example.clubbers.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clubbers.data.entities.UserFollowsAdmin
import com.example.clubbers.data.repos.UserFollowsAdminsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserFollowsAdminsViewModel @Inject constructor(
    private val repository: UserFollowsAdminsRepository
) : ViewModel() {

    fun getUsers(adminId: Int) = viewModelScope.launch {
        repository.getUsers(adminId)
    }

    fun getAdmin(userId: Int) = viewModelScope.launch {
        repository.getAdmins(userId)
    }

    fun insert(userFollowsAdmin: UserFollowsAdmin) = viewModelScope.launch {
        repository.insert(userFollowsAdmin)
    }

    fun delete(userFollowsAdmin: UserFollowsAdmin) = viewModelScope.launch {
        repository.delete(userFollowsAdmin)
    }
}