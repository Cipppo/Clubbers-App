package com.example.clubbers.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.clubbers.data.views.UsersAndAdminsView
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersAndAdminsViewsDAO {
    // Get all users
    @Query("SELECT * FROM users ORDER BY user_name ASC")
    fun getUsers(): Flow<List<UsersAndAdminsView>>

    // Get user by id
    @Query("SELECT * FROM users WHERE user_id = :userId")
    fun getUserById(userId: Int): Flow<UsersAndAdminsView>

    // Get user by username
    @Query("SELECT * FROM users WHERE user_name = :userName")
    fun getUserByUserName(userName: String): Flow<UsersAndAdminsView>

    // Get User by Email
    @Query("SELECT * FROM users WHERE user_email = :userEmail")
    fun getUserByEmail(userEmail: String): Flow<UsersAndAdminsView>
}