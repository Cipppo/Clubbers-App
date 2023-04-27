package com.example.clubbers.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.clubbers.data.dao.UsersDAO
import com.example.clubbers.data.entities.Admin
import com.example.clubbers.data.entities.Event
import com.example.clubbers.data.entities.Post
import com.example.clubbers.data.entities.User

/**
 * The Room database for this app
 * TODO: Add "followers" tables
 * TODO: If other tables are needed, create them, create the DAO and add them to the entities list
 */
@Database(entities = [User::class, Admin::class, Post::class, Event::class], version = 1, exportSchema = true)
abstract class ClubbersDatabase : RoomDatabase() {

    abstract fun usersDAO(): UsersDAO

    companion object {
        @Volatile
        private var INSTANCE: ClubbersDatabase ?= null

        fun getDatabase(context: Context): ClubbersDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClubbersDatabase::class.java,
                    "clubbers_database"
                ).build()
                INSTANCE = instance

                instance
            }
        }
    }
}