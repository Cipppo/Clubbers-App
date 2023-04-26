package com.example.clubbers.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDAO {
    // Get all users
    @Query("SELECT * FROM users ORDER BY user_name ASC")
    fun getUsers(): Flow<List<User>>

    // Get user by id
    @Query("SELECT * FROM users WHERE userId = :userId")
    fun getUserById(userId: String): Flow<User>

    // Insert user
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    // Delete user
    @Query("DELETE FROM users WHERE userId = :userId")
    suspend fun delete(userId: String)
}