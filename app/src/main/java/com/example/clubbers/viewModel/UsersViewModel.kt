package com.example.clubbers.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clubbers.data.entities.User
import com.example.clubbers.data.repos.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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




    private var _userSelected = MutableStateFlow<User?>(null)
    val userSelected: StateFlow<User?> get() = _userSelected

    fun selectUser(user: User) {
        _userSelected.value = user
    }

    fun updateUser(user: User) = viewModelScope.launch {
        repository.updateUser(user)
    }

    fun deleteUser(user: User) = viewModelScope.launch {
        repository.deleteUser(user)
    }

    private var _userById = MutableStateFlow<User?>(null)
    val userById: StateFlow<User?> get() = _userById
    fun getUserById(userId: Int) = viewModelScope.launch {
        repository.getUserById(userId)
            .collect { user ->
                _userById.value = user
            }
    }

    fun getUserByUserName(userName: String) = viewModelScope.launch {
        repository.getUserByUserName(userName)
            .collect {user ->
                _userSelected.value = user
            }
    }

    private var _userByMail = MutableStateFlow<User?>(null)
    val userByMail: StateFlow<User?> get() = _userByMail

    fun getUserByEmail(userEmail: String) = viewModelScope.launch {
        repository.getUserByEmail(userEmail)
            .collect {user ->
                _userByMail.value = user
            }
    }

    private val _userId = MutableStateFlow(0)
    val userId: StateFlow<Int> get() = _userId
    fun getUserIdByEmail(email: String) {
        viewModelScope.launch {
            repository.getUserIdByEmail(email)
                .collect { userId ->
                    _userId.value = userId
                }
        }
    }

    private var _name = MutableStateFlow("")

    val userName: StateFlow<String> get() = _name

    fun getUserFirstNameByEmail(email: String){
        viewModelScope.launch {
            repository.getUserByEmail(email).collect {
                user ->
                _name.value = user.userName + " " + user.userSurname
            }
        }
    }

    private var _bio = MutableStateFlow("")

    val userBio: StateFlow<String> get() = _bio

    fun getUserBioByEmail(email: String){
        viewModelScope.launch {
            repository.getUserByEmail(email).collect{
                user ->
                    _bio.value = user.userBio.toString()
            }
        }
    }

    fun getAllUsers(): Flow<List<User>> = repository.getAllUsers()
}