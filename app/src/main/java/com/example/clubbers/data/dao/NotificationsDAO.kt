package com.example.clubbers.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.clubbers.data.entities.Notification
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationsDAO {

    // Get All Notifications
    @Query("SELECT * FROM notifications")
    fun getNotifications(): Flow<List<Notification>>

    // Get all receiver notifications
    @Query("SELECT * FROM notifications WHERE receiver = :receiverId")
    fun getAllUserNotifications(receiverId: Int): Flow<List<Notification>>

    @Query("UPDATE notifications SET isRead = 1 WHERE notification_id = :notificationId")
    fun setNotificationAsRead(notificationId: Int): Unit

    @Query("UPDATE notifications SET isRead = 1 WHERE receiver = :receiverId")
    fun readAllNotifications(receiverId: Int): Unit
}