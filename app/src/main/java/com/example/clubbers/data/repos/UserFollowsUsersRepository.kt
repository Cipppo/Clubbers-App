package com.example.clubbers.data.repos

import com.example.clubbers.data.dao.UserFollowsUsersDAO
import com.example.clubbers.data.entities.UserFollowsUser
import kotlinx.coroutines.flow.Flow

class UserFollowsUsersRepository(private val userFollowsUsersDAO: UserFollowsUsersDAO) {

    fun getFollowers(userId: Int): Flow<List<UserFollowsUser>> {
        return userFollowsUsersDAO.getFollowers(userId)
    }

    fun getFollowed(userId: Int): Flow<List<UserFollowsUser>> {
        return userFollowsUsersDAO.getFollowed(userId)
    }

    suspend fun insert(followerId: Int, followingId: Int) {
        userFollowsUsersDAO.insert(followerId, followingId)
    }

    suspend fun delete(followerId: Int, followingId: Int) {
        userFollowsUsersDAO.delete(followerId, followingId)
    }
}