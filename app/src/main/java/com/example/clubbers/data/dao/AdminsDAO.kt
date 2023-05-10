package com.example.clubbers.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.clubbers.data.entities.Admin
import kotlinx.coroutines.flow.Flow

@Dao
interface AdminsDAO {
    // Get all admins
    @Query("SELECT * FROM admins ORDER BY admin_username ASC")
    fun getAdmins(): Flow<List<Admin>>

    // Get admin by id
    @Query("SELECT * FROM admins WHERE admin_id = :adminId")
    fun getAdminById(adminId: Int): Flow<Admin>

    // Get admin by Email
    @Query("SELECT * FROM admins WHERE admin_email = :adminMail")
    fun getAdminByMail(adminMail: String): Flow<Admin>

    // Get admin id by Email
    @Query("SELECT admin_id FROM admins WHERE admin_email = :adminMail")
    fun getAdminIdByMail(adminMail: String): Flow<Int>

    // Insert admin
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(admin: Admin)

    // Update admin
    @Update
    suspend fun update(admin: Admin)

    // Delete admin
    @Query("DELETE FROM admins WHERE admin_id = :adminId")
    suspend fun delete(adminId: Int)
}