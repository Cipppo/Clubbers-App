package com.example.clubbers.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey
    val postId: String = UUID.randomUUID().toString(),
)
