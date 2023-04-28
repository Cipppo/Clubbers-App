package com.example.clubbers.data.repos

import com.example.clubbers.data.dao.AdminsDAO
import com.example.clubbers.data.entities.Admin
import kotlinx.coroutines.flow.Flow

class AdminsRepository(private val adminsDAO: AdminsDAO) {

    val admins = adminsDAO.getAdmins()

    suspend fun insertNewAdmin(admin: Admin) {
        adminsDAO.insert(admin)
    }

    suspend fun deleteAdmin(admin: Admin) {
        adminsDAO.delete(admin.adminId)
    }

    fun getAdminById(adminId: Int): Flow<Admin> {
        return adminsDAO.getAdminById(adminId)
    }

    fun getAdminByAdminName(adminName: String): Flow<Admin> {
        return adminsDAO.getAdminByAdminName(adminName)
    }
}