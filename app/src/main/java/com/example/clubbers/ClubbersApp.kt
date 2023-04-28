package com.example.clubbers

sealed class ClubbersApp(val name: String) {
    // Bottom Bar
    object Home : ClubbersApp("Home")
    object NewPost : ClubbersApp("Create Post")
    object Discover : ClubbersApp("Discover")
    object Profile : ClubbersApp("Personal Profile")
    object Today : ClubbersApp("Today's Events")

    // Other Screens
    object Event : ClubbersApp("Event Details")
    object Settings : ClubbersApp("Settings Screen")
    object User : ClubbersApp("User Profile")

    //user registration Screens
    object Login : ClubbersApp("Login")
    object Registration : ClubbersApp("Registration Page")

    // TODO: If there will be more screens, add them here
}

