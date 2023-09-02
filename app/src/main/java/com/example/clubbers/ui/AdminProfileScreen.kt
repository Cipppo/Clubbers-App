@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.clubbers.ui

import android.content.Context
import android.inputmethodservice.Keyboard
import android.util.Log
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotApplyConflictException
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.clubbers.R
import com.example.clubbers.data.details.LocationDetails
import com.example.clubbers.data.entities.Admin
import com.example.clubbers.data.entities.Notification
import com.example.clubbers.data.entities.User
import com.example.clubbers.data.entities.UserFollowsAdmin
import com.example.clubbers.utilities.MapView
import com.example.clubbers.utilities.PostFeed
import com.example.clubbers.utilities.UserBookedEvents
import com.example.clubbers.viewModel.AdminsViewModel
import com.example.clubbers.viewModel.EventsViewModel
import com.example.clubbers.viewModel.LocationsViewModel
import com.example.clubbers.viewModel.NotificationsViewModel
import com.example.clubbers.viewModel.PostsViewModel
import com.example.clubbers.viewModel.UserFollowsAdminsViewModel
import com.example.clubbers.viewModel.UserFollowsUsersViewModel
import com.example.clubbers.viewModel.UsersViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.Date


@Composable
fun AdminProfileScreen(
    modifier: Modifier,
    adminsViewModel: AdminsViewModel,
    eventsViewModel: EventsViewModel,
    notificationsViewModel: NotificationsViewModel,
    userFollowsAdminsViewModel: UserFollowsAdminsViewModel,
    usersViewModel: UsersViewModel,
    startRequestingData: () -> Unit,
    locationsViewModel: LocationsViewModel
) {


    val userEmail = LocalContext.current.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE)
        .getString("USER_LOGGED", "None").orEmpty()
    val userType = LocalContext.current.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE)
        .getString("USER_TYPE", "NONE").orEmpty()
    var selectedMenu = remember { mutableStateOf("Upcoming") }

    var locationLatLng by rememberSaveable { mutableStateOf(LatLng(    44.17798799874249,12.4224273951512)) }



    var initialCameraPosition = CameraPosition.fromLatLngZoom(locationLatLng, 10f)


    val admin = Admin(
        adminId = 1,
        adminUsername = "admin1",
        adminEmail = "admin1",
        adminPassword = "admin1",
        adminImage = "",
        adminBio = "prova bio admin1",
        adminAddress = "prova address admin1",
        isAdmin = true
    )


    userFollowsAdminsViewModel.getUsers(admin.adminId)
    var followersCount = userFollowsAdminsViewModel.users.collectAsState().value

    var followers = remember { mutableStateOf(followersCount) }
    var personalProfile = userEmail == admin.adminUsername

    var amIFollowing = remember { mutableStateOf(false) }

    if(admin.adminEmail != userEmail && userType == "USER"){
        usersViewModel.getUserByEmail(userEmail)
        val user = usersViewModel.userByMail.collectAsState().value
        amIFollowing.value = amIFollowingClub(
            userId = user?.userId.toString().toInt(),
            adminUserId = admin.adminId,
            userFollowsAdminsViewModel = userFollowsAdminsViewModel
        )
    }


    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        Column(
            Modifier
                .padding(30.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(
                                LocalContext.current
                            ).data(data = admin.adminImage)
                                .apply(block = fun ImageRequest.Builder.() {
                                    crossfade(true)
                                    placeholder(R.drawable.ic_launcher_foreground)
                                    error(R.drawable.ic_launcher_foreground)
                                }).build()
                        ),
                        contentDescription = "Profile Avatar",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(text = admin.adminUsername, fontWeight = FontWeight.Bold)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(text = "Followers", style = TextStyle(fontWeight = FontWeight.Bold))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = followers.value.toString(),
                            style = TextStyle(fontWeight = FontWeight.Bold)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(text = "Events", style = TextStyle(fontWeight = FontWeight.Bold))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(text = "8", style = TextStyle(fontWeight = FontWeight.Bold))
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(admin.adminBio.toString())
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                if(!personalProfile){
                    usersViewModel.getUserByEmail(userEmail)
                    val user = usersViewModel.userByMail.collectAsState().value
                    if(!amIFollowing.value) {
                        Button(onClick = { handleFollowAdminPression(user, admin.adminId, userFollowsAdminsViewModel, notificationsViewModel) }, modifier = Modifier.fillMaxWidth()) {
                            Text("Follow")
                        }
                    }else{
                        Button(onClick = { handleAdminUnfollowPression(user, admin.adminId, userFollowsAdminsViewModel) }, modifier = Modifier.fillMaxWidth()) {
                            Text("Unfollow")
                        }
                    }
                }


            }
            Divider()
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween){
                IconButton(
                    onClick = {selectedMenu.value = "Upcoming"}
                ){
                    Icon(painter = painterResource(id = R.drawable.upcoming_icon),
                        contentDescription = "Upcoming Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(
                            if(selectedMenu.value == "Upcoming"){
                                45.dp
                            }else{
                                25.dp
                            }))

                }
                IconButton(
                    onClick = {selectedMenu.value = "Past"}
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.booked_icon),
                        contentDescription = "Past Events Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(
                            if(selectedMenu.value == "Past"){
                                45.dp
                            }else{
                                25.dp
                            }))
                }
                IconButton(
                    onClick = {selectedMenu.value = "Location"}
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.location_icon),
                        contentDescription = "Past Events Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(
                            if(selectedMenu.value == "Location"){
                                45.dp
                            }else{
                                25.dp
                            }
                        )
                    )
                }
            }
            //TODO aggiornare gli eventClick
            if(selectedMenu.value == "Upcoming"){
                upcomingSection(admin_id = admin.adminId, eventsViewModel = eventsViewModel, onEventClick = {})
            }else if(selectedMenu.value == "Past"){
                PastSection(admin_id = admin.adminId, eventsViewModel = eventsViewModel, onEventClick = {})
            }else if(selectedMenu.value == "Location"){
                LocationSection(
                    admin_address = admin.adminAddress,
                    initialCameraPosition = initialCameraPosition,
                    startRequestingData = startRequestingData,
                    context = LocalContext.current,
                    locationsViewModel = locationsViewModel,
                    markerLocation = locationLatLng
                )
            }
        }
    }
}


@Composable
fun upcomingSection(admin_id: Int, eventsViewModel: EventsViewModel, onEventClick: () -> Unit){

    eventsViewModel.getEventsByAdminId(admin_id)

    val events = eventsViewModel.eventsByAdId.collectAsState().value

    //TODO() Far vedere solo quelli che devono arrivare

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ){
        items(events){event ->
            eventsViewModel.selectEvent(event)
            val selectedEvent = eventsViewModel.eventSelected
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                onClick = {eventsViewModel.selectEvent(event)
                            onEventClick()}
            ){
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ){
                        Spacer(modifier = Modifier.height(2.dp))
                        if(selectedEvent != null){
                            Image(
                                painter = rememberAsyncImagePainter(
                                    ImageRequest.Builder(
                                        LocalContext.current
                                    ).data(data = selectedEvent.eventImage).apply(block = fun ImageRequest.Builder.() {
                                        crossfade(true)
                                        placeholder(R.drawable.ic_launcher_foreground)
                                        error(R.drawable.ic_launcher_foreground)
                                    }).build()
                                ),
                                contentDescription = "Event Banner",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                                        shape = MaterialTheme.shapes.small
                                    )
                                    .heightIn(min = 100.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}




@Composable
fun LocationSection(admin_address: String, initialCameraPosition: CameraPosition, startRequestingData: () -> Unit, context: Context, locationsViewModel: LocationsViewModel, markerLocation: LatLng){

    var eventLocation by rememberSaveable { mutableStateOf("") }
    var eventLocationLat by rememberSaveable { mutableStateOf(0.0)}
    var eventLocationLon by rememberSaveable { mutableStateOf(0.0)}

        /*
    var latLong = LatLng(
        44.17798799874249,
        12.4224273951512
    )

         */

    var latLong = LatLng(0.0, 0.0)


    MapView(placeName = admin_address, initialCameraPosition = initialCameraPosition, markerPosition = markerLocation)
}


@Composable
fun PastSection(admin_id: Int, eventsViewModel: EventsViewModel, onEventClick: () -> Unit){

    eventsViewModel.getEventsByAdminId(admin_id)

    val events = eventsViewModel.eventsByAdId.collectAsState().value

    //TODO() Far vedere solo quelli che ci sono gia stati

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ){
        items(events){event ->
            eventsViewModel.selectEvent(event)
            val selectedEvent = eventsViewModel.eventSelected
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                onClick = {eventsViewModel.selectEvent(event)
                    onEventClick()}
            ){
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ){
                        Spacer(modifier = Modifier.height(2.dp))
                        if(selectedEvent != null){
                            Image(
                                painter = rememberAsyncImagePainter(
                                    ImageRequest.Builder(
                                        LocalContext.current
                                    ).data(data = selectedEvent.eventImage).apply(block = fun ImageRequest.Builder.() {
                                        crossfade(true)
                                        placeholder(R.drawable.ic_launcher_foreground)
                                        error(R.drawable.ic_launcher_foreground)
                                    }).build()
                                ),
                                contentDescription = "Event Banner",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                                        shape = MaterialTheme.shapes.small
                                    )
                                    .heightIn(min = 100.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun amIFollowingClub(userId: Int, adminUserId: Int, userFollowsAdminsViewModel: UserFollowsAdminsViewModel): Boolean{
    userFollowsAdminsViewModel.getAdmins(userId)
    val admins = userFollowsAdminsViewModel.admins.collectAsState().value

    for(admin in admins){
        if(admin.adminId == adminUserId){
            return true
        }
    }

    return false
}

fun handleFollowAdminPression(user: User?, admin_id: Int, usersFollowsAdminsViewModel: UserFollowsAdminsViewModel, notificationsViewModel: NotificationsViewModel){

    val newfollow = UserFollowsAdmin(
        userId = user?.userId.toString().toInt(),
        adminId = admin_id,
    )

    usersFollowsAdminsViewModel.insert(newfollow)

    val notificationMessage = "${user?.userName.toString()} ha iniziato a seguirti"
    val notification_type = "NEW_ADMIN_FOLLOWER"

    val newNotification = Notification(
        senderId = user?.userId.toString().toInt(),
        receiverId = admin_id,
        notification_type = notification_type,
        message = notificationMessage
    )

    notificationsViewModel.addNewNotification(newNotification)

}

fun handleAdminUnfollowPression(user: User?, admin_id: Int, usersFollowsAdminsViewModel: UserFollowsAdminsViewModel){

    val newUnfollow = UserFollowsAdmin(
        userId = user?.userId.toString().toInt(),
        adminId = admin_id
    )

    usersFollowsAdminsViewModel.delete(newUnfollow)
}