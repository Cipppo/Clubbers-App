package com.example.clubbers.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "notifications", indices = [Index(value = ["notification_id"], unique = true)])
data class Notification(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "notification_id")
    val notificationId: Int = 0,

    @ColumnInfo(name = "sender")
    var senderId: Int,

    @ColumnInfo(name = "receiver")
    var receiverId: Int,

    @ColumnInfo(name = "message")
    var message: String,

    @ColumnInfo(name = "isRead")
    var read: Int = 0
)