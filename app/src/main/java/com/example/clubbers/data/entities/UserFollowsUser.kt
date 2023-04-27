package com.example.clubbers.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "user_follows_user", foreignKeys = [
    ForeignKey(entity = User::class,
        parentColumns = arrayOf("userId"),
        childColumns = arrayOf("follower_id"),
        onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = User::class,
        parentColumns = arrayOf("userId"),
        childColumns = arrayOf("followed_id"),
        onDelete = ForeignKey.CASCADE)
])
data class UserFollowsUser(
    @ColumnInfo(name = "follower_id")
    var FollowerId: Int,

    @ColumnInfo(name = "followed_id")
    var FollowedId: Int
)
