package com.example.clubbers.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// TODO: When other features are implemented, add them to the entity
@Entity(tableName = "admins", indices = [Index(value = ["admin_username", "admin_email"], unique = true)])
data class Admin(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "admin_id")
    val adminId: Int = 0,

    @ColumnInfo(name = "admin_username")
    var adminUsername: String,

    @ColumnInfo(name = "admin_email")
    var adminEmail: String,

    @ColumnInfo(name = "admin_password")
    var adminPassword: String,

    @ColumnInfo(name = "admin_image")
    var adminImage: String,

    @ColumnInfo(name = "admin_bio")
    var adminBio: String?,

    @ColumnInfo(name = "admin_address")
    var adminAddress: String,

    @ColumnInfo(name = "is_admin")
    var isAdmin: Boolean = true
)
