package com.example.clubbers.ui

import android.content.Context
import android.widget.Space
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.clubbers.R
import com.example.clubbers.data.entities.Notification
import com.example.clubbers.viewModel.NotificationsViewModel
import com.example.clubbers.viewModel.UserFollowsUsersViewModel
import com.example.clubbers.viewModel.UsersViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NotificationScreen(
    modifier: Modifier,
    usersViewModel: UsersViewModel,
    notificationsViewModel: NotificationsViewModel){

    val userEmail = LocalContext.current.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE).getString("USER_LOGGED", "None")
    usersViewModel.getUserByEmail(userEmail.orEmpty())
    val user by usersViewModel.userByMail.collectAsState()
    val userId = user?.userId
    notificationsViewModel.getAllUserNotifications(user?.userId.toString().toInt())
    var notifications = notificationsViewModel.allUserNotifications.collectAsState().value.reversed()

    Scaffold(modifier = modifier) { innerPadding ->
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            content = {
                stickyHeader {
                }
                items(notifications.size) { index ->
                    notificationItem(modifier = modifier, notification = notifications[index], usersViewModel)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun notificationItem(
    modifier: Modifier,
    notification: Notification,
    usersViewModel: UsersViewModel
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
        ) {
            if(notification.notification_type == "SUBSCRIPTION"){
                Row(Modifier.fillMaxWidth()){
                    Text(text = "NUOVA ISCRIZIONE\n${notification.message}", fontWeight = FontWeight.Bold)
                }
            }else if(notification.notification_type == "FOLLOW"){
                usersViewModel.getUserById(notification.senderId)
                var from = usersViewModel.userById.collectAsState().value?.userUsername
                Row(Modifier.fillMaxWidth()){
                    Text(text = "NUOVO FOLLOWER!\n${from} ${notification.message}", fontWeight = FontWeight.Bold)
                }
            }

    }
    Spacer(modifier = Modifier.height(10.dp))
}