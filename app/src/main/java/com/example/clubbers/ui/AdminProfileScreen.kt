package com.example.clubbers.ui

import android.content.Context
import android.inputmethodservice.Keyboard
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.clubbers.R
import com.example.clubbers.viewModel.AdminsViewModel
import com.example.clubbers.viewModel.EventsViewModel
import com.example.clubbers.viewModel.NotificationsViewModel
import com.example.clubbers.viewModel.PostsViewModel


@Composable
fun AdminProfileScreen(
    modifier: Modifier,
    adminsViewModel: AdminsViewModel,
    eventsViewModel: EventsViewModel,
    notificationsViewModel: NotificationsViewModel,
){
    val admin = adminsViewModel.adminSelected.collectAsState().value

    val userEmail = LocalContext.current.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE).getString("USER_LOGGED", "None").orEmpty()
    adminsViewModel.getAdminByMail(adminMail = userEmail)
    val currentAdmin = adminsViewModel.adminByMail.collectAsState().value

    var followers = remember { mutableStateOf(0) }
    var personalProfile = true

    val username = admin?.adminUsername.orEmpty()
    val adminBio = admin?.adminBio.orEmpty()
    val adminId = admin?.adminId.toString().toInt()
    val propicUri = admin?.adminImage

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    )
        Column(modifier = Modifier
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
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(
                                LocalContext.current
                            ).data(data = propicUri).apply(block = fun ImageRequest.Builder.() {
                                crossfade(true)
                                placeholder(R.drawable.ic_launcher_foreground)
                                error(R.drawable.ic_launcher_foreground)
                            }).build()),
                        contentDescription = "Profile Avatar",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(text = username, fontWeight = FontWeight.Bold)
                }
            }
        }

}