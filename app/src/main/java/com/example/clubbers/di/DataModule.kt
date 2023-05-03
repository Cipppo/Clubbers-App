package com.example.clubbers.di

import android.content.Context
import com.example.clubbers.ClubbersApp
import com.example.clubbers.data.repos.AdminsRepository
import com.example.clubbers.data.repos.EventHasTagsRepository
import com.example.clubbers.data.repos.EventsRepository
import com.example.clubbers.data.repos.ParticipatesRepository
import com.example.clubbers.data.repos.PostsRepository
import com.example.clubbers.data.repos.TagsRepository
import com.example.clubbers.data.repos.UserFollowsAdminsRepository
import com.example.clubbers.data.repos.UserFollowsUsersRepository
import com.example.clubbers.data.repos.UsersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Singleton
    @Provides
    fun provideAdminsRepository(@ApplicationContext context: Context) =
        AdminsRepository((context.applicationContext as ClubbersApp).database.adminsDAO())

    @Singleton
    @Provides
    fun provideEventsRepository(@ApplicationContext context: Context) =
        EventsRepository((context.applicationContext as ClubbersApp).database.eventsDAO())

    @Singleton
    @Provides
    fun provideUsersRepository(@ApplicationContext context: Context) =
        UsersRepository((context.applicationContext as ClubbersApp).database.usersDAO())

    @Singleton
    @Provides
    fun providePostsRepository(@ApplicationContext context: Context) =
        PostsRepository((context.applicationContext as ClubbersApp).database.postsDAO())

    @Singleton
    @Provides
    fun provideTagsRepository(@ApplicationContext context: Context) =
        TagsRepository((context.applicationContext as ClubbersApp).database.tagsDAO())

    @Singleton
    @Provides
    fun provideEventHasTagsRepository(@ApplicationContext context: Context) =
        EventHasTagsRepository((context.applicationContext as ClubbersApp).database.eventHasTagsDAO())

    @Singleton
    @Provides
    fun provideParticipatesRepository(@ApplicationContext context: Context) =
        ParticipatesRepository((context.applicationContext as ClubbersApp).database.participatesDAO())

    @Singleton
    @Provides
    fun provideUserFollowsUsersRepository(@ApplicationContext context: Context) =
        UserFollowsUsersRepository((context.applicationContext as ClubbersApp).database.userFollowsUsersDAO())

    @Singleton
    @Provides
    fun provideUserFollowsAdminsRepository(@ApplicationContext context: Context) =
        UserFollowsAdminsRepository((context.applicationContext as ClubbersApp).database.userFollowsAdminsDAO())
}