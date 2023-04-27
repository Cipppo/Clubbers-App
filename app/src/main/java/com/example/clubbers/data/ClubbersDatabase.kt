package com.example.clubbers.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.clubbers.data.dao.AdminDAO
import com.example.clubbers.data.dao.UsersDAO
import com.example.clubbers.data.entities.Admin
import com.example.clubbers.data.entities.Event
import com.example.clubbers.data.entities.EventHasTag
import com.example.clubbers.data.entities.Participates
import com.example.clubbers.data.entities.Post
import com.example.clubbers.data.entities.Tag
import com.example.clubbers.data.entities.User
import com.example.clubbers.data.entities.UserFollowsAdmin
import com.example.clubbers.data.entities.UserFollowsUser

/**
 * The Room database for this app
 */
@Database(entities = [
    User::class,
    Admin::class,
    Post::class,
    Event::class,
    Post::class,
    Tag::class,
    UserFollowsUser::class,
    UserFollowsAdmin::class,
    Participates::class,
    EventHasTag::class
                     ], version = 1, exportSchema = true)
abstract class ClubbersDatabase : RoomDatabase() {

    // DAOs
    abstract fun usersDAO(): UsersDAO
    abstract fun adminsDAO(): AdminDAO
    // TODO: insert other DAOs here

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