package com.example.clubbers.viewModel

import androidx.lifecycle.ViewModel
import com.example.clubbers.data.repos.UsersAndAdminsViewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UsersAndAdminsViewsViewModel @Inject constructor(
   private val repository: UsersAndAdminsViewsRepository
) : ViewModel() {

    val usersAndAdmins = repository.usersAndAdmins

    fun getUserById(userId: Int) = repository.getUserById(userId)

    fun getUserByUserName(userName: String) = repository.getUserByUserName(userName)

    fun getUserByEmail(userEmail: String) = repository.getUserByEmail(userEmail)

    fun isAdmin(userName: String) = repository.isAdmin(userName)
}