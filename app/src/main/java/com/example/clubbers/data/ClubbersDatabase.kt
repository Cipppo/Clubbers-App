package com.example.clubbers.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.clubbers.data.dao.*
import com.example.clubbers.data.entities.*
import com.example.clubbers.data.views.UsersAndAdminsView
import com.example.clubbers.utilities.DateConverter

/**
 * The Room database for this app
 */
@Database(entities = [
    User::class,
    Admin::class,
    Post::class,
    Event::class,
    Tag::class,
    UserFollowsUser::class,
    UserFollowsAdmin::class,
    Participates::class,
    EventHasTag::class
                     ],
    views = [
        UsersAndAdminsView::class
            ], version = 1, exportSchema = true)
@TypeConverters(DateConverter::class)
abstract class ClubbersDatabase : RoomDatabase() {

    // DAOs
    abstract fun usersDAO(): UsersDAO
    abstract fun adminsDAO(): AdminsDAO
    abstract fun postsDAO(): PostsDAO
    abstract fun eventsDAO(): EventsDAO
    abstract fun tagsDAO(): TagsDAO
    abstract fun participatesDAO(): ParticipatesDAO
    abstract fun eventHasTagsDAO(): EventHasTagsDAO
    abstract fun userFollowsUsersDAO(): UserFollowsUsersDAO
    abstract fun userFollowsAdminsDAO(): UserFollowsAdminsDAO
    abstract fun usersAndAdminsViewsDAO(): UsersAndAdminsViewsDAO

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