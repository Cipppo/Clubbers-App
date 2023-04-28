package com.example.clubbers.data.repos

import com.example.clubbers.data.dao.UserFollowsAdminsDAO
import com.example.clubbers.data.entities.UserFollowsAdmin
import kotlinx.coroutines.flow.Flow

class UserFollowsAdminsRepository(private val userFollowsAdminsDAO: UserFollowsAdminsDAO) {

    fun getUsers(adminId: Int): Flow<List<UserFollowsAdmin>> {
        return userFollowsAdminsDAO.getUsers(adminId)
    }

    fun getFollowed(userId: Int): Flow<List<UserFollowsAdmin>> {
        return userFollowsAdminsDAO.getAdmins(userId)
    }

    suspend fun insert(userId: Int, adminId: Int) {
        userFollowsAdminsDAO.insert(userId, adminId)
    }

    suspend fun delete(userId: Int, adminId: Int) {
        userFollowsAdminsDAO.delete(userId, adminId)
    }
}