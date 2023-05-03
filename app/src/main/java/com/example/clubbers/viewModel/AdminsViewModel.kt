package com.example.clubbers.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clubbers.data.entities.Admin
import com.example.clubbers.data.entities.User
import com.example.clubbers.data.repos.AdminsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AdminsViewModel @Inject constructor(
    private val repository: AdminsRepository
) : ViewModel() {

    val admins = repository.admins

    fun addNewAdmin(admin: Admin) = viewModelScope.launch {
        repository.insertNewAdmin(admin)
    }

    private var _adminSelected: Admin? = null
    val adminSelected
        get() = _adminSelected

    fun selectAdmin(admin: Admin) {
        _adminSelected = admin
    }

    fun updateAdmin(admin: Admin) = viewModelScope.launch {
        repository.updateAdmin(admin)
    }

    fun deleteAdmin(admin: Admin) = viewModelScope.launch {
        repository.deleteAdmin(admin)
    }

    fun getAdminById(adminId: Int): Flow<Admin> = repository.getAdminById(adminId)

    fun getAdminByAdminName(adminName: String): Flow<Admin> = repository.getAdminByAdminName(adminName)
}