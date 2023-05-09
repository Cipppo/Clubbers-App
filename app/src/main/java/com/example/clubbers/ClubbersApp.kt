package com.example.clubbers

import android.app.Application
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.clubbers.data.ClubbersDatabase
import com.example.clubbers.ui.DiscoverScreen
import com.example.clubbers.ui.HomeScreen
import com.example.clubbers.ui.NewPostScreen
import com.example.clubbers.ui.PersonalProfileScreen
import com.example.clubbers.ui.TodayScreen
import com.example.clubbers.viewModel.UsersViewModel
import dagger.hilt.android.HiltAndroidApp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.clubbers.data.entities.Admin
import com.example.clubbers.ui.ClubRegistrationScreen
import com.example.clubbers.ui.LoginScreen
import com.example.clubbers.ui.RegistrationScreen
import com.example.clubbers.viewModel.AdminsViewModel

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

    //user registration Screens
    object Login : AppScreen("Login")
    object Registration : AppScreen("Registration Page")

    object AdminRegistration : AppScreen("Admin Registration Page")
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
    onHomeButtonClicked: () -> Unit,
    onTodayButtonClicked: () -> Unit,
    onNewPostButtonClicked: () -> Unit,
    onDiscoverButtonClicked: () -> Unit,
    onProfileButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    BottomAppBar (
        actions = {
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                IconButton(
                    onClick = onHomeButtonClicked
                ) {
                    Icon(
                        Icons.Filled.Home,
                        contentDescription = "Go to Home Screen",
                        tint = if (currentScreen == AppScreen.Home.name)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondary
                    )
                }
                IconButton(
                    onClick = onTodayButtonClicked
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_calendar_today_24),
                        contentDescription = "Go to Today's Events",
                        tint = if (currentScreen == AppScreen.Today.name)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondary
                    )
                }
                FloatingActionButton(
                    onClick = onNewPostButtonClicked,
                    containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Add Post",
                        tint = if (currentScreen == AppScreen.NewPost.name)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondary
                    )
                }
                IconButton(onClick = onDiscoverButtonClicked) {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = "Discover",
                        tint = if (currentScreen == AppScreen.Discover.name)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondary
                    )
                }
                IconButton(onClick = onProfileButtonClicked) {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = "Personal Profile",
                        tint = if (currentScreen == AppScreen.Profile.name)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondary
                    )
                }
            }
        },
        modifier = modifier
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
            /**
             * TODO: When the login will work surround bottomAppBar with this if statement:
             * if (currentScreen != AppScreen.Login.name)
             */
                BottomAppBarFunction(
                    currentScreen = currentScreen,
                    onHomeButtonClicked = {
                        navController.backQueue.clear()
                        navController.navigate(AppScreen.Home.name)
                    },
                    onTodayButtonClicked = {
                        if (currentScreen == AppScreen.Today.name) {
                            navController.popBackStack()
                            navController.navigate(AppScreen.Today.name)
                        } else
                            navController.navigate(AppScreen.Today.name)
                    },
                    onNewPostButtonClicked = {
                        if (currentScreen == AppScreen.NewPost.name) {
                            navController.popBackStack()
                            navController.navigate(AppScreen.NewPost.name)
                        } else
                            navController.navigate(AppScreen.NewPost.name)
                    },
                    onDiscoverButtonClicked = {
                        if (currentScreen == AppScreen.Discover.name) {
                            navController.popBackStack()
                            navController.navigate(AppScreen.Discover.name)
                        } else
                            navController.navigate(AppScreen.Discover.name)
                    },
                    onProfileButtonClicked = {
                        if (currentScreen == AppScreen.Profile.name) {
                            navController.popBackStack()
                            navController.navigate(AppScreen.Profile.name)
                        } else
                            navController.navigate(AppScreen.Profile.name)
                    }
                )
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
        startDestination = AppScreen.Login.name,
        modifier = modifier.padding(innerPadding)
    ) {
        // Home Screen
        composable(route = AppScreen.Home.name) {
            HomeScreen()
        }

        // Today's Events Screen
        composable(route = AppScreen.Today.name) {
            TodayScreen()
        }

        // New Post Screen
        composable(route = AppScreen.NewPost.name) {
            NewPostScreen(
                onPost = { navController.navigate(AppScreen.Home.name) }
            )
        }

        // Discover Screen
        composable(route = AppScreen.Discover.name) {
            val usersViewModel = hiltViewModel<UsersViewModel>()
            DiscoverScreen(
                usersViewModel = usersViewModel
            )
        }

        // Personal Profile Screen
        composable(route = AppScreen.Profile.name) {
            PersonalProfileScreen()
        }

        composable(route = AppScreen.AdminRegistration.name){
            val adminsViewModel = hiltViewModel<AdminsViewModel>()
            ClubRegistrationScreen(adminsViewModel)
        }

        composable(route = AppScreen.Login.name){
            val usersViewModel = hiltViewModel<UsersViewModel>()
            LoginScreen(
                switchToRegister = {navController.navigate(AppScreen.Registration.name)},
                switchToAdminRegister = {navController.navigate((AppScreen.AdminRegistration.name))},
                usersViewModel = usersViewModel,
                onLogin = {navController.navigate(AppScreen.Home.name)},
            )
        }

        composable(route = AppScreen.Registration.name){
            val usersViewModel = hiltViewModel<UsersViewModel>()
            RegistrationScreen(
                usersViewModel = usersViewModel,
                onRegister = {navController.navigate(AppScreen.Home.name)},
            )
        }


    }
}