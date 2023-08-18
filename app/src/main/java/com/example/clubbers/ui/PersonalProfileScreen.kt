package com.example.clubbers.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.clubbers.R
import com.example.clubbers.data.entities.Notification
import com.example.clubbers.data.entities.User
import com.example.clubbers.data.entities.UserFollowsUser
import com.example.clubbers.utilities.PostFeed
import com.example.clubbers.utilities.UserBookedEvents
import com.example.clubbers.viewModel.EventsViewModel
import com.example.clubbers.viewModel.NotificationsViewModel
import com.example.clubbers.viewModel.ParticipatesViewModel
import com.example.clubbers.viewModel.PostsViewModel
import com.example.clubbers.viewModel.UserFollowsAdminsViewModel
import com.example.clubbers.viewModel.UserFollowsUsersViewModel
import com.example.clubbers.viewModel.UsersViewModel


@Composable

fun PersonalProfileScreen(
    onOption: () -> Unit,
    onNotify: () -> Unit,
    usersViewModel: UsersViewModel,
    postsViewModel: PostsViewModel,
    participatesViewModel: ParticipatesViewModel,
    eventsViewModel: EventsViewModel,
    userFollowsUsersViewModel: UserFollowsUsersViewModel,
    userFollowsAdminsViewModel: UserFollowsAdminsViewModel,
    notificationsViewModel: NotificationsViewModel
){


    val selectedMenu = remember{ mutableStateOf("Possts")}
    val user = usersViewModel.userSelected.collectAsState().value
    Log.d("USERUSER", user?.userName.toString())

    val userEmail = LocalContext.current.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE).getString("USER_LOGGED", "None").orEmpty()
    usersViewModel.getUserByEmail(userEmail = userEmail)
    val currentUser = usersViewModel.userByMail.collectAsState().value

    var followers = remember { mutableStateOf(0) }
    var followed = 0

    followers.value = countFollowers(userId = user?.userId.toString().toInt(), userFollowsUsersViewModel = userFollowsUsersViewModel)
    followed = countFollowed(userId = user?.userId.toString().toInt(), userFollowsUsersViewModel = userFollowsUsersViewModel)
    var personalProfile = true

    var amIFollowingResult = remember {
        mutableStateOf(false)
    }

    if ( user?.userEmail.orEmpty() != userEmail ){
        personalProfile = false
        amIFollowingResult.value = amIFollowing(
            currentUserId = currentUser?.userId.toString().toInt(),
            destUserId = user?.userId.toString().toInt(),
            destUserType = "USER",
            userFollowsAdminsViewModel = userFollowsAdminsViewModel,
            userFollowsUsersViewModel = userFollowsUsersViewModel
        )
        Log.d("RESULTF", amIFollowingResult.toString())
    }


    val userName = user?.userName.orEmpty()
    val userBio = user?.userBio.orEmpty()
    val userId = user?.userId.toString().toInt()

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ){
        Column(
            Modifier
                .padding(30.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)) {
                    Image(
                        painter = painterResource(id = R.drawable.default_avatar),
                        contentDescription = "Profile Avatar",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(text = userName, fontWeight = FontWeight.Bold)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Text(text = "Followers", style = TextStyle(fontWeight = FontWeight.Bold))
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Text(text = followers.value.toString(), style = TextStyle(fontWeight = FontWeight.Bold))
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Text(text = "Followed", style = TextStyle(fontWeight = FontWeight.Bold))
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Text(text = followed.toString(), style = TextStyle(fontWeight = FontWeight.Bold))
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Text(text = "Events", style = TextStyle(fontWeight = FontWeight.Bold))
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Text(text = "8", style = TextStyle(fontWeight = FontWeight.Bold))
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(userBio)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                if (!personalProfile) {
                    if (!amIFollowingResult.value) {
                        Button(onClick = { handleFollowPression(currentUser?.userId.toString().toInt(), userId, userFollowsUsersViewModel, notificationsViewModel = notificationsViewModel)
                                         amIFollowingResult.value = true}, modifier = Modifier.fillMaxWidth()) {
                            Text("Follow")
                        }
                    }else{
                        Button(onClick = { handleUnfollowPression(currentUser?.userId.toString().toInt(), userId, userFollowsUsersViewModel)
                                         amIFollowingResult.value = false}, modifier = Modifier.fillMaxWidth()) {
                            Text("Unfollow")
                        }
                    }
                }
            }
            Divider()
            Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween){
                IconButton(
                    onClick = {selectedMenu.value = "Posts"}
                ){
                    Icon(painter = painterResource(id = R.drawable.posts_icon),
                        contentDescription = "Posts Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(
                            if(selectedMenu.value == "Posts"){
                                45.dp
                            }else{
                                25.dp
                            }))

                }
                IconButton(
                    onClick = {selectedMenu.value = "Booked"}
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.booked_icon),
                        contentDescription = "Next Events Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(
                            if(selectedMenu.value == "Booked"){
                                45.dp
                            }else{
                                25.dp
                            }))
                }
                IconButton(
                    onClick = {selectedMenu.value = "Been"}
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.been_icon),
                        contentDescription = "Been icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(
                            if(selectedMenu.value == "Been"){
                                45.dp
                            }else{
                                25.dp
                            }
                        )
                    )
                }
            }
            if(selectedMenu.value == "Posts"){
                PostFeed(user, postsViewModel, eventsViewModel)
            }
            if(selectedMenu.value == "Booked"){
                UserBookedEvents(userId, participatesViewModel, eventsViewModel)
            }
            if(selectedMenu.value == "Been"){
                UserBookedEvents(userId, participatesViewModel, eventsViewModel)
            }
        }
    }

}


@Composable
fun amIFollowing(currentUserId: Int, destUserId: Int, destUserType: String, userFollowsAdminsViewModel: UserFollowsAdminsViewModel, userFollowsUsersViewModel: UserFollowsUsersViewModel): Boolean {
    if(destUserType == "USER"){
        userFollowsUsersViewModel.getFollowed(currentUserId)
        val currentUserFollowed = userFollowsUsersViewModel.followed.collectAsState().value
        for(user in currentUserFollowed){
            var res = user.userId == destUserId
            Log.d("USERD", res.toString())
            return user.userId == destUserId
        }
    }
    return false


}


fun handleFollowPression(from: Int, to: Int, userFollowsUsersViewModel: UserFollowsUsersViewModel, notificationsViewModel: NotificationsViewModel){

    val newFollow = UserFollowsUser(
        FollowedId = to,
        FollowerId = from,
    )
    userFollowsUsersViewModel.insert(newFollow)

    val notificationMessage = "ha iniziato a seguirti"
    val notification_type = "FOLLOW"


    val newNotification = Notification(
        senderId = from,
        receiverId = to,
        message = notificationMessage,
        notification_type = notification_type
    )

    notificationsViewModel.addNewNotification(newNotification)
}

fun handleUnfollowPression(from: Int, to: Int, userFollowsUsersViewModel: UserFollowsUsersViewModel){

    val newUnFollow = UserFollowsUser(
        FollowedId = to,
        FollowerId = from,
    )

    userFollowsUsersViewModel.delete(newUnFollow)
}

@Composable
fun countFollowers(userId: Int, userFollowsUsersViewModel: UserFollowsUsersViewModel): Int {

    userFollowsUsersViewModel.getFollowers(userId)

    return userFollowsUsersViewModel.followers.collectAsState().value.count()

}

//fun sendFollowNotification(from: Int, to: Int)

@Composable
fun countFollowed(userId: Int, userFollowsUsersViewModel: UserFollowsUsersViewModel): Int{

    userFollowsUsersViewModel.getFollowed(userId = userId)
    Log.d("FOLLOWED", userFollowsUsersViewModel.followed.collectAsState().value.count().toString())
    return userFollowsUsersViewModel.followed.collectAsState().value.count()

}