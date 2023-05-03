package com.example.clubbers.data.views

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

@DatabaseView("""
    SELECT
        users.user_id,
        users.user_name,
        users.user_email,
        users.user_password,
        users.user_image,
        users.user_bio,
        users.is_admin
    FROM users
    UNION
    SELECT
        admins.admin_id as user_id,
        admins.admin_name as user_name,
        admins.admin_email as user_email,
        admins.admin_password as user_password,
        admins.admin_image as user_image,
        admins.admin_bio as user_bio,
        admins.is_admin
    FROM admins
""")
data class UsersAndAdminsView(
    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "user_name")
    val userName: String,

    @ColumnInfo(name = "user_email")
    val userEmail: String,

    @ColumnInfo(name = "user_password")
    val userPassword: String,

    @ColumnInfo(name = "user_image")
    val userImage: String,

    @ColumnInfo(name = "user_bio")
    val userBio: String?,

    @ColumnInfo(name = "is_admin")
    val isAdmin: Boolean
)
