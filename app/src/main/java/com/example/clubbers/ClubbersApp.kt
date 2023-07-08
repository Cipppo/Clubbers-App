package com.example.clubbers

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.clubbers.data.ClubbersDatabase
import com.example.clubbers.ui.AdminProfileScreen
import com.example.clubbers.ui.ClubRegistrationScreen
import com.example.clubbers.ui.ConnectivitySnackBarComposable
import com.example.clubbers.ui.DiscoverScreen
import com.example.clubbers.viewModel.EventLocationViewModel
import com.example.clubbers.ui.EventScreen
import com.example.clubbers.ui.FoundEventsScreen
import com.example.clubbers.ui.FoundTagsScreen
import com.example.clubbers.ui.HomeScreen
import com.example.clubbers.ui.LoginScreen
import com.example.clubbers.ui.NewEventLocationScreen
import com.example.clubbers.ui.NewEventScreen
import com.example.clubbers.ui.NewPostScreen
import com.example.clubbers.ui.PersonalProfileScreen
import com.example.clubbers.ui.PostScreen
import com.example.clubbers.ui.RegistrationScreen
import com.example.clubbers.ui.SearchTagScreen
import com.example.clubbers.ui.SelectEventForPostScreen
import com.example.clubbers.ui.TodayScreen
import com.example.clubbers.ui.UserOptionScreen
import com.example.clubbers.ui.UserSearchPage
import com.example.clubbers.ui.notificationsScreen
import com.example.clubbers.viewModel.AdminsViewModel
import com.example.clubbers.viewModel.EventHasTagsViewModel
import com.example.clubbers.viewModel.EventsViewModel
import com.example.clubbers.viewModel.LocationsViewModel
import com.example.clubbers.viewModel.ParticipatesViewModel
import com.example.clubbers.viewModel.PostsViewModel
import com.example.clubbers.viewModel.TagsViewModel
import com.example.clubbers.viewModel.UsersAndAdminsViewsViewModel
import com.example.clubbers.viewModel.UsersViewModel
import com.example.clubbers.viewModel.WarningViewModel
import dagger.hilt.android.HiltAndroidApp


sealed class AppScreen(val name: String) {
    // Bottom Bar
    object Home : AppScreen("Home")
    object NewPost : AppScreen("Create Post")
    object NewEvent : AppScreen("Create Event")
    object NewEventLocation : AppScreen("Select Event Location")
    object Discover : AppScreen("Discover")
    object Profile : AppScreen("Personal Profile")
    object Today : AppScreen("Today's Events")

    // Other Screens
    object Event : AppScreen("Event Details")
    object Post : AppScreen("Post Details")
    object EventSelection : AppScreen("Select Event")
    object FoundEvents : AppScreen("Found Events")
    object FoundTags : AppScreen("Found Tags")
    object User : AppScreen("User Profile")
    object SearchTag : AppScreen("Search Tag")

    //user registration Screens
    object Login : AppScreen("Login")
    object Registration : AppScreen("Registration Page")
    object AdminRegistration : AppScreen("Admin Registration Page")
    object UserOption : AppScreen("User Option Page")
    object Notifications : AppScreen("User Notifications Page")
    object UserSearch : AppScreen("User search Page")
    // TODO: If there will be more screens, add them here
}

@HiltAndroidApp
class ClubbersApp : Application() {
    // lazy --> the database and the repository are only created when they're needed
    val database by lazy { ClubbersDatabase.getDatabase(this) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarFunction (
    currentScreen: String,
    canNavigateBack: Boolean,
    onSettingsPressed: () -> Unit,
    onTagPressed: () -> Unit,
    onSearchPressed: () -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar (
        title = {
            Text (
                text = currentScreen,
                fontWeight = FontWeight.Medium
            )
        },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back Button"
                    )
                }
            }
        },
        actions = {
            if (currentScreen == AppScreen.Profile.name) {
                IconButton(onClick = onSettingsPressed) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings Button")
                }
                IconButton(onClick =  onSearchPressed ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "User Search"
                    )
                }
            } else if (currentScreen == AppScreen.Discover.name) {
                IconButton(onClick = onTagPressed) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_tag_24),
                        contentDescription = "Search Tag Button")
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
fun BottomAppBarFunction (
    isAdmin: Boolean,
    currentScreen: String,
    onHomeButtonClicked: () -> Unit,
    onTodayButtonClicked: () -> Unit,
    onNewPostButtonClicked: () -> Unit,
    onNewEventButtonClicked: () -> Unit,
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
                if (!isAdmin) {
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
                        containerColor = FloatingActionButtonDefaults.containerColor,
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
                } else {
                    FloatingActionButton(
                        onClick = onNewEventButtonClicked,
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                            contentDescription = "Add Event",
                            tint = if (currentScreen == AppScreen.NewPost.name)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.secondary
                        )
                    }
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
        modifier = modifier,
        containerColor = BottomAppBarDefaults.containerColor
    )

}

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationApp (
    startRequestingData: () -> Unit,
    startLocationUpdates: () -> Unit,
    warningViewModel: WarningViewModel,
    navController: NavHostController = rememberNavController()
) {
    val userName = LocalContext.current.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE)
        .getString("USER_LOGGED", "None")
    var isAdmin by rememberSaveable { mutableStateOf(false) }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination?.route ?: AppScreen.Login.name

    if (!userName.isNullOrEmpty()) {
        val usersAndAdminsViewModel = hiltViewModel<UsersAndAdminsViewsViewModel>()
        isAdmin = usersAndAdminsViewModel.isAdmin(userName)
            .collectAsState(initial = false).value
    }

    val snackBarHostState = remember { SnackbarHostState() }

    val alpha = remember { Animatable(0f) }
    val shadowAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 3000)
        )
        shadowAlpha.animateTo(
            targetValue = 10f,
            animationSpec = tween(durationMillis = 1000)
        )
    }

    Scaffold(
        topBar = {
            if (
                currentScreen != AppScreen.Registration.name &&
                currentScreen != AppScreen.Login.name &&
                currentScreen != AppScreen.AdminRegistration.name
            ) {
                TopAppBarFunction(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() },
                    onSettingsPressed = { navController.navigate(AppScreen.UserOption.name) },
                    onSearchPressed = { navController.navigate(AppScreen.UserSearch.name) },
                    onTagPressed = { navController.navigate(AppScreen.SearchTag.name) },
                    modifier = Modifier
                        .shadow(shadowAlpha.value.dp, RoundedCornerShape(1.dp))
                        .alpha(alpha.value)
                )
            }
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        bottomBar = {
            if (
                currentScreen != AppScreen.Registration.name &&
                currentScreen != AppScreen.Login.name &&
                currentScreen != AppScreen.AdminRegistration.name
            ) {
                BottomAppBarFunction(
                    isAdmin = isAdmin,
                    modifier = Modifier
                        .shadow(shadowAlpha.value.dp, RoundedCornerShape(1.dp))
                        .alpha(alpha.value),
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
                        when (currentScreen) {
                            AppScreen.EventSelection.name -> {
                                navController.popBackStack()
                                navController.navigate(AppScreen.EventSelection.name)
                            }

                            AppScreen.NewPost.name -> {
                                navController.popBackStack(
                                    route = AppScreen.EventSelection.name,
                                    inclusive = true
                                )
                                navController.navigate(AppScreen.EventSelection.name)
                            }

                            else -> navController.navigate(AppScreen.EventSelection.name)
                        }
                    },
                    onNewEventButtonClicked = {
                        if (currentScreen == AppScreen.NewEvent.name) {
                            navController.popBackStack()
                            navController.navigate(AppScreen.NewEvent.name)
                        } else
                            navController.navigate(AppScreen.NewEvent.name)
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
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .alpha(alpha.value)
        ) {
            NavigationGraph(
                startRequestingData,
                startLocationUpdates,
                navController,
                innerPadding,
                warningViewModel
            )
            val context = LocalContext.current
            if (warningViewModel.showConnectivitySnackBar.value) {
                ConnectivitySnackBarComposable(
                    snackBarHostState = snackBarHostState,
                    applicationContext = context,
                    warningViewModel = warningViewModel
                )
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
private fun NavigationGraph(
    startRequestingData: () -> Unit,
    startLocationUpdates: () -> Unit,
    navController: NavHostController,
    innerPadding: PaddingValues,
    warningViewModel: WarningViewModel,
    modifier: Modifier = Modifier
) {
    val isLoggedIn =
        LocalContext.current.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE)
            .contains("USER_LOGGED")

    val eventsViewModel = hiltViewModel<EventsViewModel>()
    val locationsViewModel = hiltViewModel<LocationsViewModel>()
    val postsViewModel = hiltViewModel<PostsViewModel>()
    val eventLocationViewModel = hiltViewModel<EventLocationViewModel>()
    val sharedEventHasTagsViewModel = hiltViewModel<EventHasTagsViewModel>()



    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) AppScreen.Home.name else AppScreen.Login.name,
        modifier = modifier.padding(innerPadding)
    ) {
        // Home Screen
        composable(route = AppScreen.Home.name) {
            HomeScreen()
        }

        // Today's Events Screen
        composable(route = AppScreen.Today.name) {
            val adminsViewModel = hiltViewModel<AdminsViewModel>()
            val tagsViewModel = hiltViewModel<TagsViewModel>()
            val eventHasTagsViewModel = hiltViewModel<EventHasTagsViewModel>()
            val usersViewModel = hiltViewModel<UsersViewModel>()
            val participatesViewModel = hiltViewModel<ParticipatesViewModel>()

            TodayScreen(
                onEventClicked = { navController.navigate(AppScreen.Event.name) },
                onSearchAction = { navController.navigate(AppScreen.FoundEvents.name) },
                eventsViewModel = eventsViewModel,
                adminsViewModel = adminsViewModel,
                eventHasTagsViewModel = eventHasTagsViewModel,
                participatesViewModel = participatesViewModel,
                usersViewModel = usersViewModel,
                tagsViewModel = tagsViewModel
            )
        }

        // Event Screen
        composable(route = AppScreen.Event.name) {
            val adminsViewModel = hiltViewModel<AdminsViewModel>()
            val eventHasTagsViewModel = hiltViewModel<EventHasTagsViewModel>()
            val usersViewModel = hiltViewModel<UsersViewModel>()
            val participatesViewModel = hiltViewModel<ParticipatesViewModel>()

            EventScreen(
                eventsViewModel = eventsViewModel,
                adminsViewModel = adminsViewModel,
                eventHasTagsViewModel = eventHasTagsViewModel,
                participatesViewModel = participatesViewModel,
                usersViewModel = usersViewModel,
                postsViewModel = postsViewModel,
                onClickAction = { navController.navigate(AppScreen.Post.name) }
            )
        }

        // Post Screen
        composable(route = AppScreen.Post.name) {
            val usersViewModel = hiltViewModel<UsersViewModel>()

            PostScreen(
                postsViewModel = postsViewModel,
                usersViewModel = usersViewModel
            )
        }

        // Found Events Screen
        composable(route = AppScreen.FoundEvents.name) {
            val adminsViewModel = hiltViewModel<AdminsViewModel>()
            val eventHasTagsViewModel = hiltViewModel<EventHasTagsViewModel>()
            val usersViewModel = hiltViewModel<UsersViewModel>()
            val participatesViewModel = hiltViewModel<ParticipatesViewModel>()

            FoundEventsScreen(
                onEventSelected = { navController.navigate(AppScreen.Event.name) },
                eventsViewModel = eventsViewModel,
                adminsViewModel = adminsViewModel,
                eventHasTagsViewModel = eventHasTagsViewModel,
                participatesViewModel = participatesViewModel,
                usersViewModel = usersViewModel
            )
        }

        // New Post Screen
        composable(route = AppScreen.NewPost.name) {
            val usersViewModel = hiltViewModel<UsersViewModel>()
            val userMail = LocalContext.current.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE)
                .getString("USER_LOGGED", "None")!!
            usersViewModel.getUserIdByEmail(userMail)
            val userId by usersViewModel.userId.collectAsState()

            NewPostScreen(
                postsViewModel = postsViewModel,
                eventsViewModel = eventsViewModel,
                userId = userId,
                onPost = { navController.navigate(AppScreen.Home.name) }
            )
        }

        // New Event Screen
        composable(route = AppScreen.NewEvent.name) {
            val adminsViewModel = hiltViewModel<AdminsViewModel>()
            val tagsViewModel = hiltViewModel<TagsViewModel>()

            val adminMail = LocalContext.current.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE)
                .getString("USER_LOGGED", "None")!!
            adminsViewModel.getAdminIdByEmail(adminMail)

            NewEventScreen(
                onNext = { navController.navigate(AppScreen.NewEventLocation.name) },
                eventsViewModel = eventsViewModel,
                eventLocationViewModel = eventLocationViewModel,
                tagsViewModel = tagsViewModel,
            )
        }

        // New Event Location Screen
        composable(route = AppScreen.NewEventLocation.name) {
            val adminsViewModel = hiltViewModel<AdminsViewModel>()
            val eventHasTagsViewModel = hiltViewModel<EventHasTagsViewModel>()

            val adminMail = LocalContext.current.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE)
                .getString("USER_LOGGED", "None")!!
            adminsViewModel.getAdminIdByEmail(adminMail)
            val adminId by adminsViewModel.adminId.collectAsState()

            NewEventLocationScreen(
                onEvent = { navController.navigate(AppScreen.Home.name) },
                eventsViewModel = eventsViewModel,
                locationsViewModel = locationsViewModel,
                eventLocationViewModel = eventLocationViewModel,
                adminId = adminId,
                startRequestingData = startRequestingData,
                startLocationUpdates = startLocationUpdates,
                warningViewModel = warningViewModel,
                eventHasTagsViewModel = eventHasTagsViewModel
            )
        }

        // Event Selection Screen
        composable(route = AppScreen.EventSelection.name) {
            val adminsViewModel = hiltViewModel<AdminsViewModel>()
            val eventHasTagsViewModel = hiltViewModel<EventHasTagsViewModel>()
            val usersViewModel = hiltViewModel<UsersViewModel>()

            val userMail = LocalContext.current.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE)
                .getString("USER_LOGGED", "None")!!
            usersViewModel.getUserIdByEmail(userMail)
            val userId by usersViewModel.userId.collectAsState()

            val participatesViewModel = hiltViewModel<ParticipatesViewModel>()
            SelectEventForPostScreen(
                onEventSelected = { navController.navigate(AppScreen.NewPost.name) },
                userId = userId,
                eventsViewModel = eventsViewModel,
                adminsViewModel = adminsViewModel,
                eventHasTagsViewModel = eventHasTagsViewModel,
                participatesViewModel = participatesViewModel,
                usersViewModel = usersViewModel
            )
        }

        // Discover Screen
        composable(route = AppScreen.Discover.name) {
            val adminsViewModel = hiltViewModel<AdminsViewModel>()
            val tagsViewModel = hiltViewModel<TagsViewModel>()
            val eventHasTagsViewModel = hiltViewModel<EventHasTagsViewModel>()
            val usersViewModel = hiltViewModel<UsersViewModel>()
            val participatesViewModel = hiltViewModel<ParticipatesViewModel>()
            DiscoverScreen(
                onEventClicked = { navController.navigate(AppScreen.Event.name) },
                onSearchAction = { navController.navigate(AppScreen.FoundEvents.name) },
                eventsViewModel = eventsViewModel,
                adminsViewModel = adminsViewModel,
                eventHasTagsViewModel = eventHasTagsViewModel,
                participatesViewModel = participatesViewModel,
                usersViewModel = usersViewModel,
                tagsViewModel = tagsViewModel
            )
        }

        // Search Tags Screen
        composable(route = AppScreen.SearchTag.name) {
            val adminsViewModel = hiltViewModel<AdminsViewModel>()
            val tagsViewModel = hiltViewModel<TagsViewModel>()
            val usersViewModel = hiltViewModel<UsersViewModel>()
            val participatesViewModel = hiltViewModel<ParticipatesViewModel>()
            SearchTagScreen(
                eventHasTagsViewModel = sharedEventHasTagsViewModel,
                participatesViewModel = participatesViewModel,
                usersViewModel = usersViewModel,
                eventsViewModel = eventsViewModel,
                onEventClicked = { navController.navigate(AppScreen.Event.name) },
                onSearchAction = { navController.navigate(AppScreen.FoundTags.name) },
                adminsViewModel = adminsViewModel,
                tagsViewModel = tagsViewModel
            )
        }

        // Found Tags Screen
        composable(route = AppScreen.FoundTags.name) {
            val adminsViewModel = hiltViewModel<AdminsViewModel>()
            val usersViewModel = hiltViewModel<UsersViewModel>()
            val participatesViewModel = hiltViewModel<ParticipatesViewModel>()
            FoundTagsScreen(
                eventHasTagsViewModel = sharedEventHasTagsViewModel,
                participatesViewModel = participatesViewModel,
                usersViewModel = usersViewModel,
                eventsViewModel = eventsViewModel,
                adminsViewModel = adminsViewModel,
                onEventSelected = { navController.navigate(AppScreen.Event.name) }
            )
        }

        // Personal Profile Screen
        composable(route = AppScreen.Profile.name) {
            val usersViewModel = hiltViewModel<UsersViewModel>()
            val postViewModel = hiltViewModel<PostsViewModel>()
            val participatesViewModel = hiltViewModel<ParticipatesViewModel>()
            val eventsViewModel = hiltViewModel<EventsViewModel>()
            val userType = LocalContext.current.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE).getString("USER_TYPE", "NONE").orEmpty()

            if(userType == "USER"){
                PersonalProfileScreen(
                    onOption = {navController.navigate(AppScreen.UserOption.name)},
                    onNotify = {navController.navigate(AppScreen.Notifications.name)},
                    usersViewModel = usersViewModel,
                    postsViewModel = postViewModel,
                    participatesViewModel = participatesViewModel,
                    eventsViewModel = eventsViewModel,
                )
            }else{
                AdminProfileScreen()
            }


        }

        // Admin Registration Screen
        composable(route = AppScreen.AdminRegistration.name){
            val adminsViewModel = hiltViewModel<AdminsViewModel>()
            ClubRegistrationScreen(
                adminsViewModel,
                onRegister = {
                    navController.backQueue.clear()
                    navController.navigate(AppScreen.Home.name)
                }
            )
        }

        // Login Screen
        composable(route = AppScreen.Login.name){
            val usersViewModel = hiltViewModel<UsersViewModel>()
            val adminsViewModel = hiltViewModel<AdminsViewModel>()
            LoginScreen(
                switchToRegister = {navController.navigate(AppScreen.Registration.name)},
                switchToAdminRegister = {navController.navigate((AppScreen.AdminRegistration.name))},
                usersViewModel = usersViewModel,
                onLogin = {
                    navController.backQueue.clear()
                    navController.navigate(AppScreen.Home.name)
                },
                adminsViewModel = adminsViewModel
            )
        }

        // User Registration Screen
        composable(route = AppScreen.Registration.name){
            val usersViewModel = hiltViewModel<UsersViewModel>()
            RegistrationScreen(
                usersViewModel = usersViewModel,
                onRegister = {
                    navController.backQueue.clear()
                    navController.navigate(AppScreen.Home.name)
                },
            )
        }

        // User Option Screen
        composable(route = AppScreen.UserOption.name){
            UserOptionScreen(
                onLogout = {
                    navController.backQueue.clear()
                    navController.navigate(AppScreen.Login.name)
                }
            )
        }

        // Notifications Screen
        composable(route = AppScreen.Notifications.name){
            notificationsScreen()
        }

        composable(route = AppScreen.UserSearch.name){
            val usersViewModel = hiltViewModel<UsersViewModel>()
            UserSearchPage(modifier = Modifier.fillMaxSize(),
                onClickAction = {navController.navigate(AppScreen.Profile.name)},
                usersViewModel = usersViewModel)
        }

    }
}