package com.example.clubbers.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clubbers.data.entities.Admin
import com.example.clubbers.data.entities.User
import com.example.clubbers.data.entities.UserFollowsAdmin
import com.example.clubbers.data.repos.UserFollowsAdminsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserFollowsAdminsViewModel @Inject constructor(
    private val repository: UserFollowsAdminsRepository
) : ViewModel() {

    fun getUsers(adminId: Int): Flow<List<User>> = repository.getUsers(adminId)

    fun getAdmins(userId: Int): Flow<List<Admin>> = repository.getAdmins(userId)

    fun insert(userFollowsAdmin: UserFollowsAdmin) = viewModelScope.launch {
        repository.insert(userFollowsAdmin)
    }

    fun delete(userFollowsAdmin: UserFollowsAdmin) = viewModelScope.launch {
        repository.delete(userFollowsAdmin)
    }
}