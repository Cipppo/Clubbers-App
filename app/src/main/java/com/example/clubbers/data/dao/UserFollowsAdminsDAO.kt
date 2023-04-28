package com.example.clubbers.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.clubbers.data.entities.Admin
import com.example.clubbers.data.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserFollowsAdminsDAO {
    // get all admins a user follows
    @Query("SELECT * FROM user_follows_admin WHERE user_id = :userId")
    fun getAdmins(userId: Int): Flow<List<Admin>>

    // get all users that follow an admin
    @Query("SELECT * FROM user_follows_admin WHERE admin_id = :adminId")
    fun getUsers(adminId: Int): Flow<List<User>>

    // insert a user following an admin
    @Query("INSERT INTO user_follows_admin (user_id, admin_id) VALUES (:userId, :adminId)")
    suspend fun insert(userId: Int, adminId: Int)

    // delete a user from following an admin
    @Query("DELETE FROM user_follows_admin WHERE user_id = :userId AND admin_id = :adminId")
    suspend fun delete(userId: Int, adminId: Int)
}