package com.example.clubbers.data.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.clubbers.data.entities.Admin
import kotlinx.coroutines.flow.Flow

interface AdminsDAO {
    // Get all admins
    @Query("SELECT * FROM admins ORDER BY admin_name ASC")
    fun getAdmins(): Flow<List<Admin>>

    // Get admin by id
    @Query("SELECT * FROM admins WHERE adminId = :adminId")
    fun getAdminById(adminId: String): Flow<Admin>

    // Get admin by admin_name
    @Query("SELECT * FROM admins WHERE admin_name = :adminName")
    fun getAdminByAdminName(adminName: String): Flow<Admin>

    // Insert admin
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(admin: Admin)

    // Delete admin
    @Query("DELETE FROM admins WHERE adminId = :adminId")
    suspend fun delete(adminId: String)
}