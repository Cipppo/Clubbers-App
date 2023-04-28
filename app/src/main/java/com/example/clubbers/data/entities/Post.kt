package com.example.clubbers.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "posts", foreignKeys = [
    ForeignKey(entity = User::class,
        parentColumns = arrayOf("userId"),
        childColumns = arrayOf("post_user_id"),
        onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = Event::class,
        parentColumns = arrayOf("eventId"),
        childColumns = arrayOf("post_event_id"),
        onDelete = ForeignKey.CASCADE)
])
data class Post(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "post_id")
    val postId: Int = 0,

    @ColumnInfo(name = "post_title")
    var postTitle: String,

    @ColumnInfo(name = "post_image")
    var postImage: String,

    @ColumnInfo(name = "post_caption")
    var postCaption: String,

    @ColumnInfo(name = "post_user_id")
    var postUserId: Int,

    @ColumnInfo(name = "post_event_id")
    var postEventId: Int
)
