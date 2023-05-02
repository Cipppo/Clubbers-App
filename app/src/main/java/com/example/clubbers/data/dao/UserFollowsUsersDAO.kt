package com.example.clubbers.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.clubbers.data.entities.User
import com.example.clubbers.data.entities.UserFollowsUser
import kotlinx.coroutines.flow.Flow

@Dao
interface UserFollowsUsersDAO {
    // get all users a user follows
    @Query("SELECT * FROM user_follows_user " +
            "INNER JOIN users ON user_follows_user.followed_id = users.user_id " +
            "WHERE user_follows_user.follower_id = :userId")
    fun getFollowed(userId: Int): Flow<List<User>>

    // get all users that follow a user
    @Query("SELECT * FROM user_follows_user " +
            "INNER JOIN users ON user_follows_user.follower_id = users.user_id " +
            "WHERE user_follows_user.followed_id = :userId")
    fun getFollowers(userId: Int): Flow<List<User>>

    // insert a user following a user
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userFollowsUser: UserFollowsUser)

    // delete a user from following a user
    @Delete
    suspend fun delete(userFollowsUser: UserFollowsUser)
}