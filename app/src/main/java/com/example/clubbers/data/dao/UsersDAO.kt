package com.example.clubbers.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.clubbers.data.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDAO {
    // Get all users
    @Query("SELECT * FROM users ORDER BY user_name ASC")
    fun getUsers(): Flow<List<User>>

    // Get user by id
    @Query("SELECT * FROM users WHERE user_id = :userId")
    fun getUserById(userId: Int): Flow<User>

    // Get user by username
    @Query("SELECT * FROM users WHERE user_name = :userName")
    fun getUserByUserName(userName: String): Flow<User>

    // Insert user
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    // Update user
    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(user: User)

    // Delete user
    @Query("DELETE FROM users WHERE user_id = :userId")
    suspend fun delete(userId: Int)

    // Get User by Email
    @Query("SELECT * FROM users WHERE user_email = :userEmail")
    fun getUserByEmail(userEmail: String): Flow<User>

    // Get user id by Email
    @Query("SELECT user_id FROM users WHERE user_email = :userEmail")
    fun getUserIdByEmail(userEmail: String): Flow<Int>

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT user_id FROM users WHERE user_email = :userEmail")
    fun getIdFromEmail(userEmail: String): Flow<Int>
}