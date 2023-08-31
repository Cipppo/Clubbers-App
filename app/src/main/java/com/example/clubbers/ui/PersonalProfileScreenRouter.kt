package com.example.clubbers.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.clubbers.data.entities.Admin
import com.example.clubbers.data.entities.User
import com.example.clubbers.viewModel.AdminsViewModel
import com.example.clubbers.viewModel.EventsViewModel
import com.example.clubbers.viewModel.NotificationsViewModel
import com.example.clubbers.viewModel.ParticipatesViewModel
import com.example.clubbers.viewModel.PostsViewModel
import com.example.clubbers.viewModel.UserFollowsAdminsViewModel
import com.example.clubbers.viewModel.UserFollowsUsersViewModel
import com.example.clubbers.viewModel.UsersViewModel

@Composable
fun personalProfileScreenRouter(
    modifier: Modifier,
    postsViewModel: PostsViewModel,
    participatesViewModel: ParticipatesViewModel,
    eventsViewModel: EventsViewModel,
    usersFollowsUsersViewModel: UserFollowsUsersViewModel,
    usersFollowsAdminsViewModel: UserFollowsAdminsViewModel,
    usersViewModel: UsersViewModel,
    notificationsViewModel: NotificationsViewModel,
    adminsViewModel: AdminsViewModel,
){

    val userType = LocalContext.current.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE).getString("USER_TYPE", "NONE").orEmpty()
    val userEmail = LocalContext.current.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE).getString("USER_LOGGED", "None").orEmpty()

    if(userType == "USER"){
        usersViewModel.getUserByEmail(userEmail = userEmail)
        var user = usersViewModel.userByMail.collectAsState().value
        val userProfile = User(
            userId = user?.userId.toString().toInt(),
            userName = user?.userName.toString(),
            userSurname = user?.userSurname.toString(),
            userUsername = user?.userUsername.toString(),
            userEmail = user?.userEmail.toString(),
            userPassword = user?.userPassword.toString(),
            userImage = user?.userImage.toString(),
            userBio = user?.userBio.toString(),
            isAdmin = false
        )
        usersViewModel.selectUser(userProfile)
        PersonalProfileScreen(
            onOption = { /*TODO*/ },
            onNotify = { /*TODO*/ },
            usersViewModel = usersViewModel,
            postsViewModel = postsViewModel,
            participatesViewModel = participatesViewModel,
            eventsViewModel = eventsViewModel,
            userFollowsUsersViewModel = usersFollowsUsersViewModel,
            userFollowsAdminsViewModel = usersFollowsAdminsViewModel,
            notificationsViewModel = notificationsViewModel
        ) {

        }
    }else if (userType == "CLUB"){
        adminsViewModel.getAdminByMail(adminMail = userEmail)
        var admin = adminsViewModel.adminByMail.collectAsState().value

        val adminProfile = Admin(
            adminId = admin?.adminId.toString().toInt(),
            adminUsername = admin?.adminUsername.toString(),
            adminEmail = admin?.adminEmail.toString(),
            adminPassword = admin?.adminPassword.toString(),
            adminImage = admin?.adminImage.toString(),
            adminBio = admin?.adminBio.toString(),
            adminAddress = admin?.adminAddress.toString(),
            isAdmin = true
        )

        adminsViewModel.selectAdmin(adminProfile)
        AdminProfileScreen(
            modifier = modifier,
            adminsViewModel = adminsViewModel,
            eventsViewModel = eventsViewModel,
            notificationsViewModel = notificationsViewModel
        )
    }


}