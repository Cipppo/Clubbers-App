package com.example.clubbers.utilities

import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.util.lerp
import androidx.core.app.NotificationCompat
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.clubbers.R
import com.example.clubbers.data.details.LocationDetails
import com.example.clubbers.data.entities.Event
import com.example.clubbers.data.entities.Participates
import com.example.clubbers.data.entities.Post
import com.example.clubbers.viewModel.AdminsViewModel
import com.example.clubbers.viewModel.EventHasTagsViewModel
import com.example.clubbers.viewModel.EventsViewModel
import com.example.clubbers.viewModel.ParticipatesViewModel
import com.example.clubbers.viewModel.PostsViewModel
import com.example.clubbers.viewModel.TagsViewModel
import com.example.clubbers.viewModel.UsersViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.absoluteValue


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CreateSearchTimeLine(
    modifier: Modifier,
    onClickAction: () -> Unit,
    onSearchAction: () -> Unit,
    eventsViewModel: EventsViewModel,
    adminsViewModel: AdminsViewModel,
    eventHasTagsViewModel: EventHasTagsViewModel,
    tagsViewModel: TagsViewModel,
    participatesViewModel: ParticipatesViewModel,
    usersViewModel: UsersViewModel,
    searchingTags: Boolean = false,
    isTodayEvents: Boolean
) {
    eventsViewModel.getFutureEvents()
    val events by eventsViewModel.events.collectAsState()
    val todayEvents = if (isTodayEvents) {
        events.filter { event ->
            val timeStart = event.timeStart
            val timeEnd = event.timeEnd
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val eventStartDate = sdf.format(timeStart)
            val eventEndDate = sdf.format(timeEnd)
            val currentDate = sdf.format(System.currentTimeMillis())
            currentDate in eventStartDate..eventEndDate
        }
    } else {
        emptyList()
    }
    Scaffold(modifier = modifier) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            content = {
                stickyHeader {
                    AutoCompleteSearchBar(
                        events = if (isTodayEvents) todayEvents else events,
                        eventsViewModel = eventsViewModel,
                        onSearchAction = onSearchAction,
                        searchingTags = searchingTags,
                        eventHasTagsViewModel = eventHasTagsViewModel,
                        tagsViewModel = tagsViewModel
                    )
                    Divider(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                        thickness = 1.dp,
                        modifier = modifier
                            .shadow(5.dp, RoundedCornerShape(1.dp))
                    )
                }
                if (isTodayEvents && todayEvents.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center,
                            content = {
                                Text(
                                    text = "No events today",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(16.dp),
                                )
                            }
                        )
                    }
                } else {
                    items(if(isTodayEvents) todayEvents.size else events.size) { index ->
                        EventItem(
                            eventsViewModel = eventsViewModel,
                            adminsViewModel = adminsViewModel,
                            eventHasTagsViewModel = eventHasTagsViewModel,
                            event = if (isTodayEvents) todayEvents[index] else events[index],
                            participatesViewModel = participatesViewModel,
                            usersViewModel = usersViewModel,
                            onClickAction = onClickAction
                        )
                    }
                }
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateParticipatedEventTimeLine(
    modifier: Modifier,
    onClickAction: () -> Unit,
    eventsViewModel: EventsViewModel,
    adminsViewModel: AdminsViewModel,
    eventHasTagsViewModel: EventHasTagsViewModel,
    participatesViewModel: ParticipatesViewModel,
    passedEvents: List<Event> = emptyList(),
    isSearchingTags: Boolean = false,
    usersViewModel: UsersViewModel,
) {
    Scaffold(modifier = modifier) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            content = {
                if (passedEvents.isNotEmpty()) {
                    items(passedEvents.size) { index ->
                        EventItem(
                            eventsViewModel = eventsViewModel,
                            adminsViewModel = adminsViewModel,
                            eventHasTagsViewModel = eventHasTagsViewModel,
                            event = passedEvents[index],
                            participatesViewModel = participatesViewModel,
                            usersViewModel = usersViewModel,
                            onClickAction = onClickAction
                        )
                    }
                } else {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center,
                            content = {
                                Text(
                                    text = if (isSearchingTags) "No events for this tag" else "No events participated right now",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(16.dp),
                                )
                            }
                        )
                    }
                }
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoCompleteSearchBar(
    events: List<Event>,
    searchingTags: Boolean,
    eventsViewModel: EventsViewModel,
    eventHasTagsViewModel: EventHasTagsViewModel,
    tagsViewModel: TagsViewModel,
    onSearchAction: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val eventNames = events.map { it.eventName }.distinct()
    tagsViewModel.getAllTags()
    val tags by tagsViewModel.tags.collectAsState()
    val tagNames = tags.map { it.tagName }.distinct()
    val context = LocalContext.current

    OutlinedTextField(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                textFieldSize = coordinates.size.toSize()
            },
        label = { Text(text = if(searchingTags) "Search by tags" else "Search by title") },
        value = searchText,
        onValueChange = {
            searchText = it
            expanded = true
                        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
            cursorColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        textStyle = TextStyle(
            color = Color.Black,
            fontSize = MaterialTheme.typography.bodySmall.fontSize
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        trailingIcon = {
            Row {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowDropDown,
                        contentDescription = "Dropdown menu")
                }
                IconButton(onClick = {
                    if (!searchingTags) {
                        if (searchText.isNotEmpty() && searchText == eventNames.find { it == searchText }) {
                            eventsViewModel.getEventsByName(searchText)
                            onSearchAction()
                        } else {
                            Toast.makeText(context, "Event not found", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        if (searchText.isNotEmpty() && searchText == tagNames.find { it == searchText }) {
                            eventHasTagsViewModel.getEventsByTagName(searchText)
                            onSearchAction()
                        } else {
                            Toast.makeText(context, "Event not found", Toast.LENGTH_SHORT).show()
                        }
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_check_24),
                        contentDescription = "Search")
                }
            }
        }
    )
    
    AnimatedVisibility(visible = expanded) {
        Card(
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .width(textFieldSize.width.dp),
        ) {
            LazyColumn(
                modifier = Modifier
                    .heightIn(max = 150.dp)
            ) {
                if (searchText.isNotEmpty()) {
                    if (searchingTags)
                        items(
                            tagNames.filter {
                                it.lowercase()
                                    .contains(searchText.lowercase())
                            }
                                .sorted()
                        ) {
                            EventNames(title = it) { title ->
                                searchText = title
                                expanded = false
                            }
                        }
                    else
                        items(
                            eventNames.filter {
                                it.lowercase()
                                    .contains(searchText.lowercase())
                            }
                                .sorted()
                        ) {
                            EventNames(title = it) { title ->
                                searchText = title
                                expanded = false
                            }
                        }
                } else {
                    if (searchingTags)
                        items(
                            tagNames.sorted()
                        ) {
                            EventNames(title = it) { title ->
                                searchText = title
                                expanded = false
                            }
                        }
                    else {
                        items(
                            eventNames.sorted()
                        ) {
                            EventNames(title = it) { title ->
                                searchText = title
                                expanded = false
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun EventNames(
    title: String,
    onSelect: (String) -> Unit
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = { onSelect(title) }
            )
            .padding(10.dp)
    ) {
        Text(
            text = title,
            fontSize = MaterialTheme.typography.bodySmall.fontSize
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventItem(
    eventsViewModel: EventsViewModel,
    adminsViewModel: AdminsViewModel,
    eventHasTagsViewModel: EventHasTagsViewModel,
    participatesViewModel: ParticipatesViewModel,
    usersViewModel: UsersViewModel,
    isSingleEvent: Boolean = false,
    event: Event,
    onClickAction: () -> Unit
) {
    val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    adminsViewModel.getAdminById(event.eventAdminId)
    val admin by adminsViewModel.admin.collectAsState()
    eventHasTagsViewModel.getTagsByEventId(event.eventId)
    val tags by eventHasTagsViewModel.tags.collectAsState()

    val mapSheetState = rememberSheetState()

    val eventTitle = event.eventName
    val adminName = admin?.adminUsername
    val proPicUri = admin?.adminImage
    val imageUri = event.eventImage
    val caption = event.eventDescription.orEmpty()
    val tagsList = tags?.map { it.tagName }
    val timeStart = format.format(event.timeStart)
    val timeEnd = format.format(event.timeEnd)
    val place = LocationDetails(
        name = event.eventLocation,
        latitude = event.eventLocationLat,
        longitude = event.eventLocationLon
    )

    val context = LocalContext.current

    ElevatedCard(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .border(
                1.dp,
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                shape = MaterialTheme.shapes.small
            ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        onClick = {
            eventsViewModel.selectEvent(event = event)
            onClickAction()
        }
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 8.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(
                            LocalContext.current
                        ).data(data = proPicUri).apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)
                            placeholder(R.drawable.ic_launcher_foreground)
                            error(R.drawable.ic_launcher_foreground)
                        }).build()
                    ),
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                            shape = CircleShape
                        )
                )
                Spacer(modifier = Modifier.padding(end = 8.dp))
                adminName?.let { Text(text = it, style = MaterialTheme.typography.bodySmall) }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = eventTitle,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(
                        LocalContext.current
                    ).data(data = imageUri).apply(block = fun ImageRequest.Builder.() {
                        crossfade(true)
                        placeholder(R.drawable.ic_launcher_foreground)
                        error(R.drawable.ic_launcher_foreground)
                    }).build()
                ),
                contentDescription = "Post/Event image",
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .fillMaxSize()
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                        shape = MaterialTheme.shapes.medium
                    )
                    .run {
                        if (isSingleEvent) {
                            heightIn(180.dp)
                        } else {
                            height(180.dp)
                        }
                    },
                contentScale = if (isSingleEvent) ContentScale.Crop else ContentScale.Fit
            )
            Text(
                text = caption,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.padding(top = 8.dp))
            if(isSingleEvent) {
                Text(
                    text = "Tags: ${tagsList?.joinToString(separator = ", ")}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.padding(top = 16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 8.dp)
            ) {
                Column {
                    Text(text = "Starts: $timeStart", style = MaterialTheme.typography.bodySmall)
                    Text(text = "Ends: $timeEnd", style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.weight(1f))
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    val textColor = if (event.participants == event.maxParticipants) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    Text(
                        text = AnnotatedString(
                            text = "Participants: "
                        ) + AnnotatedString(
                            text = "${event.participants}",
                            spanStyle = SpanStyle(color = textColor)
                        ) + AnnotatedString(
                            text = " / ${event.maxParticipants}"
                        ),

                        style = MaterialTheme.typography.bodySmall,
                    )
                    Text(
                        text = place.name.take(15) + if (place.name.length > 15) "..." else "",
                        style = MaterialTheme.typography.bodySmall
                            .copy(textDecoration = TextDecoration.Underline),
                        modifier = Modifier
                            .clickable(onClick = { mapSheetState.show() })
                            .widthIn(max = 100.dp)
                    )
                }
            }
            if (isSingleEvent) {

                participatesViewModel.getParticipants(event.eventId)
                val participants by participatesViewModel.participants.collectAsState()

                val userName = LocalContext.current.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE)
                    .getString("USER_LOGGED", "None")
                usersViewModel.getUserByEmail(userName.orEmpty())
                val user by usersViewModel.userByMail.collectAsState()

                var isUserParticipating by rememberSaveable { mutableStateOf(false) }
                isUserParticipating = participants.contains(user)

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    ElevatedButton(
                        onClick = {
                            val participant = user?.let {
                                Participates(
                                    eventId = event.eventId,
                                    userId = it.userId
                                )
                            }
                            if (isUserParticipating) participant?.let {
                                participatesViewModel.deleteParticipant(it)
                                event.participants--
                                eventsViewModel.updateEvent(event)
                            } else participant?.let {
                                participatesViewModel.addNewParticipant(it)
                                event.participants++
                                sendEventParticipationNotification(
                                    context = context,
                                    eventTitle = eventTitle,
                                    date = timeStart,
                                    place = place.name)
                                eventsViewModel.updateEvent(event)
                            }
                        },
                        enabled = event.participants < event.maxParticipants!!
                    ) {
                        if (isUserParticipating) {
                            Text(
                                text = "Leave",
                                color = MaterialTheme.colorScheme.error
                            )
                        } else {
                            Text(text = "Join")
                        }
                    }
                }
            }
        }
    }

    val locationLatLng = LatLng(place.latitude, place.longitude)
    val initialCameraPosition = CameraPosition.fromLatLngZoom(locationLatLng, 10f)
    MapDialog(
        title = "Location Map",
        sheetState = mapSheetState,
        placeName = place.name,
        initialCameraPosition = initialCameraPosition,
        locationLatLng = locationLatLng
    )
}

fun sendEventParticipationNotification(context: Context, eventTitle: String, date: String, place: String){
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val notification = NotificationCompat.Builder(context, "1")
        .setContentTitle("Stai partecipando all'evento $eventTitle !")
        .setContentText("L'evento prenderà parte il $date a $place")
        .setSmallIcon(R.drawable.baseline_notifications_24)
        .build()
    notificationManager.notify(1, notification)
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostItem(
    usersViewModel: UsersViewModel,
    postsViewModel: PostsViewModel,
    post: Post,
    onClickAction: () -> Unit
) {
    usersViewModel.getUserById(post.postUserId)
    val user by usersViewModel.userSelected.collectAsState()

    val proPicUri = user?.userImage
    val userName = user?.userName
    val imageUriList: List<Uri> = post.postImage.split(",").map { Uri.parse(it) }

    ElevatedCard(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .border(
                1.dp,
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                shape = MaterialTheme.shapes.small
            ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        onClick = {
            postsViewModel.selectPost(post)
            onClickAction()
        }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(
                            LocalContext.current
                        ).data(data = proPicUri).apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)
                            placeholder(R.drawable.ic_launcher_foreground)
                            error(R.drawable.ic_launcher_foreground)
                        }).build()
                    ),
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                            shape = CircleShape
                        )
                )
                Spacer(modifier = Modifier.padding(end = 8.dp))
                userName?.let { Text(text = it, style = MaterialTheme.typography.bodySmall) }
            }
            CarouselCard(
                capturedImageUris = imageUriList
            )
            Text(
                text = post.postCaption,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CarouselCard(
    capturedImageUris: List<Uri>,
    currentImageIndex: MutableState<Int> = remember { mutableStateOf(0) }
) {
    val pagerState = rememberPagerState(initialPage = 0)
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        HorizontalPager(
            count = capturedImageUris.size,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 20.dp),
            modifier = Modifier
                .height(350.dp)
        ) { page ->
            Card(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .graphicsLayer {
                        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                        lerp(
                            start = 0.50f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                            .also { scale ->
                                scaleX = scale
                                scaleY = scale
                            }
                        alpha = lerp(
                            start = 0.50f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }
            ) {
                val currentImageUri = capturedImageUris[page]
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current).data(data = currentImageUri)
                            .apply(block = fun ImageRequest.Builder.() {
                                crossfade(true)
                                placeholder(R.drawable.ic_launcher_foreground)
                                error(R.drawable.ic_launcher_foreground)
                            }).build()
                    ),
                    contentDescription = "Captured Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(300.dp)
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.small)
                )
            }
        }
        Row(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            IconButton(
                enabled = pagerState.currentPage > 0,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                }
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.weight(1f))
            repeat(capturedImageUris.size) {
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .size(5.dp)
                        .background(
                            color = if (pagerState.currentPage == it) {
                                currentImageIndex.value = it
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
                            }
                        )
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                enabled = pagerState.currentPage < capturedImageUris.size - 1,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            ) {
                Icon(Icons.Filled.ArrowForward, contentDescription = "Forward")
            }
        }
    }
}