package com.example.clubbers.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.clubbers.R
import com.example.clubbers.data.details.LocationDetails
import com.example.clubbers.data.entities.Event
import com.example.clubbers.data.entities.EventHasTag
import com.example.clubbers.utilities.MapView
import com.example.clubbers.utilities.getFilesFromAppDir
import com.example.clubbers.utilities.saveImage
import com.example.clubbers.viewModel.EventHasTagsViewModel
import com.example.clubbers.viewModel.EventsViewModel
import com.example.clubbers.viewModel.LocationsViewModel
import com.example.clubbers.viewModel.WarningViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEventLocationScreen(
    modifier: Modifier = Modifier,
    onEvent: () -> Unit,
    startRequestingData: () -> Unit,
    startLocationUpdates: () -> Unit,
    eventsViewModel: EventsViewModel,
    eventLocationViewModel: EventLocationViewModel,
    eventHasTagsViewModel: EventHasTagsViewModel,
    locationsViewModel: LocationsViewModel,
    warningViewModel: WarningViewModel,
    adminId: Int
) {
    val context = LocalContext.current

    val eventTitle by eventLocationViewModel.title
    val startEventDate by eventLocationViewModel.startEventDate
    val endEventDate by eventLocationViewModel.endEventDate
    val maxParticipants by eventLocationViewModel.maxParticipants
    val capturedImageUri by eventLocationViewModel.capturedImageUri
    val description by eventLocationViewModel.description
    val tags by eventLocationViewModel.tags

    eventsViewModel.getAllEvents()
    val eventsTemp = eventsViewModel.events.collectAsState()
    val eventId = eventsTemp.value.size.plus(1)

    var localImageDir by rememberSaveable { mutableStateOf("") }

    var eventLocation by rememberSaveable { mutableStateOf("") }
    var eventLocationLat by rememberSaveable { mutableStateOf(0.0)}
    var eventLocationLon by rememberSaveable { mutableStateOf(0.0)}

    val captionMaxChar = 255

    var isButtonClicked by rememberSaveable { mutableStateOf(false) }
    var isRequestDone by rememberSaveable { mutableStateOf(false) }

    var locationLatLng by rememberSaveable { mutableStateOf(LatLng(0.0, 0.0)) }

    var initialCameraPosition = CameraPosition.fromLatLngZoom(locationLatLng, 10f)

    var mapVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row (
            modifier = modifier
                .fillMaxWidth()
                .height(120.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = eventLocation,
                onValueChange = {
                    if (it.length <= captionMaxChar) eventLocation = it
                },
                label = { Text(text = "Event Location") },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colorScheme.onBackground,
                    containerColor = MaterialTheme.colorScheme.background,
                ),
                shape = MaterialTheme.shapes.small,
                modifier = modifier
                    .width(250.dp)
                    .height(120.dp),
                maxLines = 2,
                supportingText = {
                    if (isButtonClicked && eventLocation.isEmpty() && isRequestDone) {
                        Text(
                            text = "Location not found, Retry",
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Start,
                        )
                    }
                    Text(
                        text = "${eventLocation.length} / $captionMaxChar",
                        modifier = modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.End,
                    )
                }
            )
            Spacer(modifier = modifier.weight(1f))
            Column {
                ExtendedFloatingActionButton(
                    onClick = {
                        if (eventLocation.isNotEmpty()) {
                            isRequestDone = false
                            context.getSharedPreferences("EventLocation", Context.MODE_PRIVATE)
                                .edit()
                                .putString("EventLocation", eventLocation)
                                .apply()
                            locationsViewModel.setEventLocation(LocationDetails("", 0.0, 0.0))
                            startRequestingData()
                            if (!warningViewModel.showConnectivitySnackBar.value) {
                                isButtonClicked = true
                                val coroutineScope = CoroutineScope(Dispatchers.Main)
                                coroutineScope.launch {
                                    locationsViewModel.location.collect {
                                        eventLocation = it.name
                                        isRequestDone = true
                                        eventLocationLat = it.latitude
                                        eventLocationLon = it.longitude
                                    }
                                }
                                Toast.makeText(context, "Checking location", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Location is empty", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = modifier
                        .height(25.dp)
                ) {
                    Text(
                        text = "Check",
                        textAlign = TextAlign.Center,
                    )
                }
                Spacer(modifier = modifier.height(16.dp))
                ExtendedFloatingActionButton(
                    onClick = {
                        isRequestDone = false
                        context.deleteSharedPreferences("EventLocation")
                        startLocationUpdates()
                        if (!warningViewModel.showGPSAlertDialog.value) {
                            isButtonClicked = true
                            val coroutineScope = CoroutineScope(Dispatchers.Main)
                            coroutineScope.launch {
                                locationsViewModel.location.collect {
                                    eventLocation = it.name
                                    eventLocationLat = it.latitude
                                    eventLocationLon = it.longitude
                                }
                            }
                            Toast.makeText(context, "Getting position", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "GPS is disabled", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = modifier
                        .height(25.dp)
                ) {
                    Text(
                        text = "GPS",
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
        ExtendedFloatingActionButton(
            onClick = {
                locationLatLng = LatLng(eventLocationLat, eventLocationLon)
                initialCameraPosition = CameraPosition.fromLatLngZoom(locationLatLng, 10f)
                mapVisible = !mapVisible
//                mapSheetState.show()
            },
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Update map",
                textAlign = TextAlign.Center,
            )
        }
        ElevatedCard(
            modifier = modifier
                .fillMaxWidth()
                .height(300.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            when {
                mapVisible -> {
                    MapView(
                        placeName = "Prova",
                        initialCameraPosition = initialCameraPosition,
                        markerPosition = locationLatLng)
                }
                !mapVisible -> {
                    MapView(
                        placeName = "Prova",
                        initialCameraPosition = initialCameraPosition,
                        markerPosition = locationLatLng)
                }
            }
        }
        Button(
            onClick = {
                val photoType = "Event"

                if (eventLocation.isEmpty()) {
                    Toast.makeText(context, "Please enter an event location", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (capturedImageUri!!.path?.isNotEmpty() == true) {
                    saveImage(context, context.applicationContext.contentResolver, capturedImageUri!!, photoType)

                    context.getFilesFromAppDir(photoType).lastOrNull().let { lastFile ->
                        lastFile?.let {
                            localImageDir = it
                        }
                    }

                    eventsViewModel.insertNewEvent(
                        Event(
                            eventName = eventTitle,
                            eventImage = localImageDir,
                            eventLocation = eventLocation,
                            eventLocationLat = eventLocationLat,
                            eventLocationLon = eventLocationLon,
                            eventDescription = description,
                            timeStart = Date.from(startEventDate!!.atZone(ZoneId.systemDefault()).toInstant()),
                            timeEnd = Date.from(endEventDate!!.atZone(ZoneId.systemDefault()).toInstant()),
                            maxParticipants = maxParticipants,
                            participants = 0,
                            eventAdminId = adminId
                        )
                    )
                    for (tag in tags) {
                        eventHasTagsViewModel.addNewTagToEvent(
                            eventHasTag = EventHasTag(
                                eventId = eventId,
                                tagId = tag.id
                            )
                        )
                    }
                    onEvent()
                } else {
                    Toast.makeText(context, "Please take a photo", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_send_24),
                contentDescription = "Post event",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = modifier.width(8.dp))
            Text(text = "Next: Post event")
        }
    }
}