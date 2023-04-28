package com.example.clubbers

import android.app.Application
import com.example.clubbers.data.ClubbersDatabase
import dagger.hilt.android.HiltAndroidApp

sealed class AppScreen(val name: String) {
    // Bottom Bar
    object Home : AppScreen("Home")
    object NewPost : AppScreen("Create Post")
    object Discover : AppScreen("Discover")
    object Profile : AppScreen("Personal Profile")
    object Today : AppScreen("Today's Events")

    // Other Screens
    object Event : AppScreen("Event Details")
    object Settings : AppScreen("Settings Screen")
    object User : AppScreen("User Profile")

    // TODO: If there will be more screens, add them here
}

@HiltAndroidApp
class ClubbersApp : Application() {
    // lazy --> the database and the repository are only created when they're needed
    val database by lazy { ClubbersDatabase.getDatabase(this) }
}


