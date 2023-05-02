package com.example.clubbers

import android.app.Application
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.clubbers.data.ClubbersDatabase
import com.example.clubbers.ui.HomeScreen
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

@Composable
fun BottomAppBarFunction (
    currentScreen: String,
    modifier: Modifier = Modifier
) {
    BottomAppBar (
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.Home, contentDescription = "Add Post")
            }
        },
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Post")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationApp (
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination?.route ?: AppScreen.Home.name

    Scaffold(
        bottomBar = {
            BottomAppBarFunction(currentScreen = currentScreen)
        }
    ) { innerPadding ->
        NavigationGraph(navController, innerPadding)
    }
}

@Composable
private fun NavigationGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.Home.name,
        modifier = modifier.padding(innerPadding)
    ) {
        // Home Screen
        composable(route = AppScreen.Home.name) {
            HomeScreen()
        }
    }
}