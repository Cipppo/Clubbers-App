package com.example.clubbers.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clubbers.data.entities.Admin
import com.example.clubbers.data.repos.AdminsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
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

    private val _adminId = MutableStateFlow(0)
    val adminId: StateFlow<Int> get() = _adminId

    fun getAdminIdByEmail(email: String) {
        viewModelScope.launch {
            repository.getAdminIdByMail(email)
                .collect { adminId ->
                    _adminId.value = adminId
                }
        }
    }

    fun getAdminByMail(adminMail: String): Flow<Admin> = repository.getAdminByEmail(adminMail)
}