package com.example.clubbers.data.repos

import com.example.clubbers.data.dao.UserFollowsUsersDAO
import com.example.clubbers.data.entities.User
import com.example.clubbers.data.entities.UserFollowsUser
import kotlinx.coroutines.flow.Flow

class UserFollowsUsersRepository(private val userFollowsUsersDAO: UserFollowsUsersDAO) {

    fun getFollowers(userId: Int): Flow<List<User>> {
        return userFollowsUsersDAO.getFollowers(userId)
    }

    fun getFollowed(userId: Int): Flow<List<User>> {
        return userFollowsUsersDAO.getFollowed(userId)
    }

    suspend fun insert(userFollowsUser: UserFollowsUser) {
        userFollowsUsersDAO.insert(userFollowsUser)
    }

    suspend fun delete(userFollowsUser: UserFollowsUser) {
        userFollowsUsersDAO.delete(userFollowsUser)
    }
}