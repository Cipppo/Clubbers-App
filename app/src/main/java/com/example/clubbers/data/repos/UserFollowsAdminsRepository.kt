package com.example.clubbers.data.repos

import com.example.clubbers.data.dao.UserFollowsAdminsDAO
import com.example.clubbers.data.entities.Admin
import com.example.clubbers.data.entities.User
import com.example.clubbers.data.entities.UserFollowsAdmin
import kotlinx.coroutines.flow.Flow

class UserFollowsAdminsRepository(private val userFollowsAdminsDAO: UserFollowsAdminsDAO) {

    fun getUsers(adminId: Int): Flow<List<User>> {
        return userFollowsAdminsDAO.getUsers(adminId)
    }

    fun getAdmins(userId: Int): Flow<List<Admin>> {
        return userFollowsAdminsDAO.getAdmins(userId)
    }

    suspend fun insert(userFollowsAdmin: UserFollowsAdmin) {
        userFollowsAdminsDAO.insert(userFollowsAdmin)
    }

    suspend fun delete(userFollowsAdmin: UserFollowsAdmin) {
        userFollowsAdminsDAO.delete(userFollowsAdmin)
    }
}