package com.example.clubbers.data.repos

import com.example.clubbers.data.dao.UserFollowsAdminsDAO
import com.example.clubbers.data.entities.Admin
import com.example.clubbers.data.entities.User
import kotlinx.coroutines.flow.Flow

class UserFollowsAdminsRepository(private val userFollowsAdminsDAO: UserFollowsAdminsDAO) {

    fun getUsers(adminId: Int): Flow<List<User>> {
        return userFollowsAdminsDAO.getUsers(adminId)
    }

    fun getAdmins(userId: Int): Flow<List<Admin>> {
        return userFollowsAdminsDAO.getAdmins(userId)
    }

    suspend fun insert(userId: Int, adminId: Int) {
        userFollowsAdminsDAO.insert(userId, adminId)
    }

    suspend fun delete(userId: Int, adminId: Int) {
        userFollowsAdminsDAO.delete(userId, adminId)
    }
}