package com.example.clubbers.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clubbers.data.entities.Admin
import com.example.clubbers.data.repos.AdminsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
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

    private val _admin = MutableStateFlow<Admin?>(null)
    val admin: StateFlow<Admin?> get() = _admin

    fun updateAdmin(admin: Admin) = viewModelScope.launch {
        repository.updateAdmin(admin)
    }

    fun deleteAdmin(admin: Admin) = viewModelScope.launch {
        repository.deleteAdmin(admin)
    }

    fun getAdminById(adminId: Int) = viewModelScope.launch {
        repository.getAdminById(adminId)
            .collect { admin ->
                _admin.value = admin
            }
    }

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

    private val _adminByMail = MutableStateFlow<Admin?>(null)

    val adminByMail: StateFlow<Admin?> get() = _adminByMail

    fun getAdminByMail(adminMail: String){
        viewModelScope.launch {
            repository.getAdminByEmail(adminMail)
                .collect {admin ->
                    _adminByMail.value = admin
                }
        }
    }

    fun getAllAdmins(): Flow<List<Admin>> = repository.getAllAdmins()
}