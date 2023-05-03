package com.example.clubbers.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "user_follows_admin",
    primaryKeys = ["user_id", "admin_id"],
    foreignKeys = [
        ForeignKey(entity = User::class,
            parentColumns = arrayOf("user_id"),
            childColumns = arrayOf("user_id"),
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Admin::class,
            parentColumns = arrayOf("admin_id"),
            childColumns = arrayOf("admin_id"),
            onDelete = ForeignKey.CASCADE)
    ]
)
data class UserFollowsAdmin(
    @ColumnInfo(name = "user_id")
    var userId: Int,

    @ColumnInfo(name = "admin_id")
    var adminId: Int
)
