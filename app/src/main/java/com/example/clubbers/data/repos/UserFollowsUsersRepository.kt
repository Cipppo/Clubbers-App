package com.example.clubbers.data.repos

import com.example.clubbers.data.dao.UserFollowsUsersDAO
import com.example.clubbers.data.entities.User
import kotlinx.coroutines.flow.Flow

class UserFollowsUsersRepository(private val userFollowsUsersDAO: UserFollowsUsersDAO) {

    fun getFollowers(userId: Int): Flow<List<User>> {
        return userFollowsUsersDAO.getFollowers(userId)
    }

    fun getFollowed(userId: Int): Flow<List<User>> {
        return userFollowsUsersDAO.getFollowed(userId)
    }

    suspend fun insert(followerId: Int, followedId: Int) {
        userFollowsUsersDAO.insert(followerId, followedId)
    }

    suspend fun delete(followerId: Int, followedId: Int) {
        userFollowsUsersDAO.delete(followerId, followedId)
    }
}