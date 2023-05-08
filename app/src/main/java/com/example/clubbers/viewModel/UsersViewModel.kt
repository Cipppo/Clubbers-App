package com.example.clubbers.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clubbers.data.entities.User
import com.example.clubbers.data.repos.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repository: UsersRepository
) : ViewModel() {

    val users = repository.users

    fun addNewUser(user: User) = viewModelScope.launch {
        repository.insertNewUser(user)
    }

    private var _userSelected: User? = null
    val userSelected
        get() = _userSelected

    fun selectUser(user: User) {
        _userSelected = user
    }

    fun updateUser(user: User) = viewModelScope.launch {
        repository.updateUser(user)
    }

    fun deleteUser(user: User) = viewModelScope.launch {
        repository.deleteUser(user)
    }

    fun getUserById(userId: Int): Flow<User> = repository.getUserById(userId)

    fun getUserByUserName(userName: String): Flow<User> = repository.getUserByUserName(userName)

    fun getUserByEmail(userEmail: String): Flow<User> = repository.getUserByEmail(userEmail)

    fun getAllUsers(): Flow<List<User>> = repository.getAllUsers()
}