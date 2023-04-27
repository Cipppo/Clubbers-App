package com.example.clubbers.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "user_follows_admin", foreignKeys = [
    ForeignKey(entity = User::class,
        parentColumns = arrayOf("userId"),
        childColumns = arrayOf("user_id"),
        onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = Admin::class,
        parentColumns = arrayOf("adminId"),
        childColumns = arrayOf("admin_id"),
        onDelete = ForeignKey.CASCADE)
])
data class UserFollowsAdmin(
    @ColumnInfo(name = "user_id")
    var userId: Int,

    @ColumnInfo(name = "admin_id")
    var adminId: Int
)
