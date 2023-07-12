package com.example.clubbers.ui

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
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

    val userEmail = LocalContext.current.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE).getString("USER_LOGGED", "None").orEmpty()
    usersViewModel.getUserByEmail(userEmail)
    var user = usersViewModel.userByMail.collectAsState().value
    notificationsViewModel.getAllUserNotifications(user?.userId.toString().toInt())
    var notifications = notificationsViewModel.allUserNotifications.collectAsState().value

    Scaffold(modifier = modifier) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            content = {
                stickyHeader {
                }
                items(notifications.size) { index ->
                    notificationItem(modifier = modifier, notification = notifications[index])
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun notificationItem(
    modifier: Modifier,
    notification: Notification
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
        ) {
            Text("notifica")

    }
}