package com.example.clubbers.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "posts", foreignKeys = [
    ForeignKey(entity = User::class,
        parentColumns = arrayOf("user_id"),
        childColumns = arrayOf("user_id"),
        onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = Event::class,
        parentColumns = arrayOf("event_id"),
        childColumns = arrayOf("event_id"),
        onDelete = ForeignKey.CASCADE)
])
data class Post(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "post_id")
    val postId: Int = 0,

    @ColumnInfo(name = "post_image")
    var postImage: String,

    @ColumnInfo(name = "post_caption")
    var postCaption: String,

    @ColumnInfo(name = "user_id")
    var postUserId: Int,

    @ColumnInfo(name = "event_id")
    var postEventId: Int
)
