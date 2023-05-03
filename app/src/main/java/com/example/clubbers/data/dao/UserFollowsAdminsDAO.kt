package com.example.clubbers.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.clubbers.data.entities.Admin
import com.example.clubbers.data.entities.User
import com.example.clubbers.data.entities.UserFollowsAdmin
import kotlinx.coroutines.flow.Flow

@Dao
interface UserFollowsAdminsDAO {
    // get all admins a user follows
    @Query("SELECT * FROM user_follows_admin " +
            "INNER JOIN admins ON user_follows_admin.admin_id = admins.admin_id " +
            "WHERE user_follows_admin.user_id = :userId")
    fun getAdmins(userId: Int): Flow<List<Admin>>

    // get all users that follow an admin
    @Query("SELECT * FROM user_follows_admin " +
            "INNER JOIN users ON user_follows_admin.user_id = users.user_id " +
            "WHERE user_follows_admin.admin_id = :adminId")
    fun getUsers(adminId: Int): Flow<List<User>>

    // insert a user following an admin
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userFollowsAdmin: UserFollowsAdmin)

    // delete a user from following an admin
    @Delete
    suspend fun delete(userFollowsAdmin: UserFollowsAdmin)
}