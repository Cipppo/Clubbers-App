package com.example.clubbers.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "users", indices = [Index(value = ["user_name"], unique = true)])
data class User(
    @PrimaryKey
    val userId: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "user_name")
    var userName: String,

    @ColumnInfo(name = "user_email")
    var userEmail: String,

    @ColumnInfo(name = "user_password")
    var userPassword: String,

    @ColumnInfo(name = "user_image")
    var userImage: String,

    @ColumnInfo(name = "user_bio")
    var userBio: String,
)
