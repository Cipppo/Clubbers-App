package com.example.clubbers.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clubbers.data.entities.Admin
import com.example.clubbers.data.repos.AdminsRepository
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

    fun getAdminById(adminId: Int) = viewModelScope.launch {
        repository.getAdminById(adminId)
    }

    fun getAdminByAdminName(adminName: String) = viewModelScope.launch {
        repository.getAdminByAdminName(adminName)
    }
}