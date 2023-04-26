package com.example.clubbers.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, Post::class, Event::class], version = 1, exportSchema = true)
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