package com.example.clubbers.data.repos

import com.example.clubbers.data.dao.NotificationsDAO
import com.example.clubbers.data.entities.Notification
import dagger.Provides
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class NotificationsRepository(private val notificationsDAO: NotificationsDAO) {

    val notifications = notificationsDAO.getNotifications()

    suspend fun getNotifications(): Flow<List<Notification>>{
        return notificationsDAO.getNotifications()
    }

    suspend fun getAllUserNotifications(userId: Int): Flow<List<Notification>>{
        return notificationsDAO.getAllUserNotifications(userId)
    }

    suspend fun setNotificationAsRead(notificationId: Int): Unit{
        notificationsDAO.setNotificationAsRead(notificationId)
    }

    suspend fun readAllNotifications(receiverId: Int): Unit{
        notificationsDAO.readAllNotifications(receiverId)
    }

    suspend fun insert(notification: Notification){
        notificationsDAO.insert(notification)
    }
}