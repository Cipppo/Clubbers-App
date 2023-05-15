package com.example.clubbers.utilities

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.clubbers.R
import com.example.clubbers.data.details.LocationDetails
import com.example.clubbers.data.entities.Event
import com.example.clubbers.viewModel.AdminsViewModel
import com.example.clubbers.viewModel.EventHasTagsViewModel
import com.example.clubbers.viewModel.EventsViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CreateSearchTimeLine(
    modifier: Modifier,
    onClickAction: () -> Unit,
    eventsViewModel: EventsViewModel,
    adminsViewModel: AdminsViewModel,
    eventHasTagsViewModel: EventHasTagsViewModel
) {
    val events = eventsViewModel.events.collectAsState(initial = listOf()).value
    Scaffold(modifier = modifier) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            content = {
                stickyHeader {
                    SearchBar(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .fillMaxWidth()
                    )
                    Divider(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                        thickness = 1.dp,
                        modifier = modifier
                            .shadow(5.dp, RoundedCornerShape(1.dp))
                    )
                }
                items(events.size) { index ->
                    EventItem(
                        eventsViewModel = eventsViewModel,
                        adminsViewModel = adminsViewModel,
                        eventHasTagsViewModel = eventHasTagsViewModel,
                        event = events[index],
                        onClickAction = onClickAction
                    )
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
    eventHasTagsViewModel: EventHasTagsViewModel
) {
    val events = eventsViewModel.events.collectAsState(initial = listOf()).value
    Scaffold(modifier = modifier) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            content = {
                items(events.size) { index ->
                    EventItem(
                        eventsViewModel = eventsViewModel,
                        adminsViewModel = adminsViewModel,
                        eventHasTagsViewModel = eventHasTagsViewModel,
                        event = events[index],
                        onClickAction = onClickAction
                    )
                }
            }
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    var searchText by remember { mutableStateOf("") }

    OutlinedTextField (
        value = searchText,
        onValueChange = { searchText = it },
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        label = { Text(text = "Search") },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
            cursorColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        singleLine = true,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_search_24),
                contentDescription = "Search icon"
            )
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventItem(
    eventsViewModel: EventsViewModel,
    adminsViewModel: AdminsViewModel,
    eventHasTagsViewModel: EventHasTagsViewModel,
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
            Row (
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
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                        shape = MaterialTheme.shapes.medium
                    )
                    .heightIn(min = 180.dp)
            )
            Text(
                text = caption,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.padding(top = 8.dp))
            Text(
                text = "Tags: ${ tagsList?.joinToString(separator = ", ") }",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.padding(top = 8.dp))
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
                Text(
                    text = place.name.take(10) + if (place.name.length > 10) "..." else "" + "",
                    style = MaterialTheme.typography.bodySmall
                        .copy(textDecoration = TextDecoration.Underline),
                    modifier = Modifier
                        .clickable(onClick = { mapSheetState.show() })
                        .widthIn(max = 100.dp)
                )
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