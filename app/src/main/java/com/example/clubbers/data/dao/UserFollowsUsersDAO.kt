package com.example.clubbers.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.clubbers.data.entities.UserFollowsUser
import kotlinx.coroutines.flow.Flow

@Dao
interface UserFollowsUsersDAO {
    // get all users a user follows
    @Query("SELECT * FROM user_follows_user WHERE follower_id = :userId")
    fun getFollowed(userId: Int): Flow<List<UserFollowsUser>>

    // get all users that follow a user
    @Query("SELECT * FROM user_follows_user WHERE followed_id = :userId")
    fun getFollowers(userId: Int): Flow<List<UserFollowsUser>>

    // insert a user following a user
    @Query("INSERT INTO user_follows_user (follower_id, followed_id) VALUES (:followerId, :followedId)")
    suspend fun insert(followerId: Int, followedId: Int)

    // delete a user from following a user
    @Query("DELETE FROM user_follows_user WHERE follower_id = :followerId AND followed_id = :followedId")
    suspend fun delete(followerId: Int, followedId: Int)
}