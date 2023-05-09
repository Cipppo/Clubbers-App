package com.example.clubbers.data.repos

import com.example.clubbers.data.dao.UsersAndAdminsViewsDAO
import com.example.clubbers.data.views.UsersAndAdminsView
import kotlinx.coroutines.flow.Flow

class UsersAndAdminsViewsRepository(private val usersAndAdminsViewsDAO: UsersAndAdminsViewsDAO) {

    val usersAndAdmins = usersAndAdminsViewsDAO.getUsers()

    fun getUserById(userId: Int): Flow<UsersAndAdminsView> {
        return usersAndAdminsViewsDAO.getUserById(userId)
    }

    fun getUserByUserName(userName: String): Flow<UsersAndAdminsView> {
        return usersAndAdminsViewsDAO.getUserByUserName(userName)
    }

    fun getUserByEmail(userEmail: String): Flow<UsersAndAdminsView> {
        return usersAndAdminsViewsDAO.getUserByEmail(userEmail)
    }

    fun isAdmin(userName: String): Flow<Boolean> {
        return usersAndAdminsViewsDAO.isAdmin(userName)
    }
}