package com.example.clubbers.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// TODO: When other features are implemented, add them to the entity
@Entity(tableName = "users", indices = [Index(value = ["user_name"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,

    @ColumnInfo(name = "user_name")
    var userName: String,

    @ColumnInfo(name = "user_email")
    var userEmail: String,

    @ColumnInfo(name = "user_password")
    var userPassword: String,

    @ColumnInfo(name = "user_image")
    var userImage: String,

    @ColumnInfo(name = "user_bio")
    var userBio: String?,

    @ColumnInfo(name = "is_admin")
    var isAdmin: Boolean = false
)