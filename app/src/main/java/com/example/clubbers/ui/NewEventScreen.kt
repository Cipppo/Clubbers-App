package com.example.clubbers.ui

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.clubbers.R
import com.example.clubbers.data.details.TagsListItem
import com.example.clubbers.utilities.NumbersDialog
import com.example.clubbers.utilities.TagsListDialog
import com.example.clubbers.utilities.createImageFile
import com.example.clubbers.viewModel.EventLocationViewModel
import com.example.clubbers.viewModel.EventsViewModel
import com.example.clubbers.viewModel.TagsViewModel
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Objects

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEventScreen(
    modifier: Modifier = Modifier,
    onNext: () -> Unit,
    eventsViewModel: EventsViewModel,
    eventLocationViewModel: EventLocationViewModel,
    tagsViewModel: TagsViewModel,
) {
    val context = LocalContext.current

    tagsViewModel.getAllTags()
    val tags by tagsViewModel.tags.collectAsState()
    val tagItems = remember { mutableStateListOf<TagsListItem>() }
    if (tagItems.isEmpty()) tagItems.addAll(tags.map { TagsListItem(it.tagId, it.tagName, false) })

    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file)

    var showPopup by rememberSaveable { mutableStateOf(false) }

    var capturedImageUri by rememberSaveable {
        mutableStateOf<Uri>(Uri.EMPTY)
    }


    var eventCaption by rememberSaveable { mutableStateOf("") }
    var eventTitle by rememberSaveable { mutableStateOf("") }

    val captionMaxChar = 255
    val titleMaxChar = 30

    var selectedStartDate by rememberSaveable { mutableStateOf<LocalDate>(LocalDate.now()) }
    var selectedStartTime by rememberSaveable { mutableStateOf(LocalTime.of(0,0,0)) }
    val startCalendarState = rememberSheetState()
    val startClockState = rememberSheetState()

    var selectedEndDate by rememberSaveable { mutableStateOf<LocalDate>(LocalDate.now().plusDays(1)) }
    var selectedEndTime by rememberSaveable { mutableStateOf(LocalTime.of(12,0,0)) }
    val endCalendarState = rememberSheetState()
    val endClockState = rememberSheetState()

    var maxParticipants by rememberSaveable { mutableStateOf(0) }
    val maxParticipantsState = rememberSheetState()
    val tagsState = rememberSheetState()

    eventsViewModel.getAllEvents()

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            capturedImageUri = uri
        } else {
            // Handle cancellation event
            Toast.makeText(context, "Camera cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier
                    .width(80.dp)
                    .aspectRatio(1f)
                    .background(Color.Gray, MaterialTheme.shapes.small)
                    .clickable(
                        onClick = {
                            if (capturedImageUri.path?.isNotEmpty() == true) {
                                showPopup = true
                            } else {
                                val permissionCheckResult = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                )
                                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                    cameraLauncher.launch(uri)
                                } else {
                                    permissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            }
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (showPopup) {
                    AlertDialog(
                        onDismissRequest = {
                            showPopup = false
                        },
                        title = { Text("Post Preview") },
                        text = {
                            Box(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(
                                        ImageRequest.Builder(
                                            LocalContext.current
                                        ).data(data = capturedImageUri)
                                            .apply(block = fun ImageRequest.Builder.() {
                                                crossfade(true)
                                                placeholder(R.drawable.ic_launcher_foreground)
                                                error(R.drawable.ic_launcher_foreground)
                                            }).build()
                                    ),
                                    contentDescription = "Captured Image",
                                    contentScale = ContentScale.Crop,
                                    modifier = modifier.fillMaxSize()
                                )
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showPopup = false
                                    capturedImageUri = Uri.EMPTY
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            ) {
                                Text("Take new photo")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    showPopup = false
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            ) {
                                Text("Cancel")
                            }
                        }
                    )
                }

                if (capturedImageUri.path?.isNotEmpty() == true) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current).data(data = capturedImageUri)
                                .apply(block = fun ImageRequest.Builder.() {
                                    crossfade(true)
                                    placeholder(R.drawable.ic_launcher_foreground)
                                    error(R.drawable.ic_launcher_foreground)
                                }).build()
                        ),
                        contentDescription = "Captured Image",
                        contentScale = ContentScale.FillWidth,
                        modifier = modifier
                            .fillMaxSize()
                            .clip(MaterialTheme.shapes.small)
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                        contentDescription = "Take Photo",
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
            Spacer(modifier = modifier.width(16.dp))
            OutlinedTextField(
                value = eventTitle,
                onValueChange = {
                    if (it.length <= titleMaxChar) eventTitle = it
                                },
                label = { Text(text = "Event Title") },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colorScheme.onBackground,
                    containerColor = MaterialTheme.colorScheme.background,
                ),
                shape = MaterialTheme.shapes.small,
                modifier = modifier
                    .fillMaxWidth()
                    .height(80.dp),
                maxLines = 1,
                supportingText = {
                    Text(
                        text = "${eventTitle.length} / $titleMaxChar",
                        modifier = modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.End,
                    )
                }
            )
        }
        Divider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            thickness = 1.dp,
            modifier = modifier
                .shadow(5.dp, RoundedCornerShape(1.dp))
        )

        // Start Dialogs
        CalendarDialog(
            state = startCalendarState,
            config = CalendarConfig(
                yearSelection = true,
                monthSelection = true,
                style = CalendarStyle.MONTH,
            ),
            selection = CalendarSelection.Dates { newDates ->
                selectedStartDate = newDates.first()
                selectedEndDate = newDates.first()
            }
        )
        ClockDialog(
            state = startClockState,
            selection = ClockSelection.HoursMinutes { hours, minute ->
                selectedStartTime = LocalTime.of(hours, minute, 0)
                selectedEndTime = LocalTime.of(hours, minute, 0)
            },
            config = ClockConfig(
                defaultTime = selectedStartTime,
                is24HourFormat = true
            )
        )

        // End Dialogs
        CalendarDialog(
            state = endCalendarState,
            config = CalendarConfig(
                yearSelection = true,
                monthSelection = true,
                style = CalendarStyle.MONTH,
            ),
            selection = CalendarSelection.Dates { newDates ->
                selectedEndDate = newDates.first()
            }
        )
        ClockDialog(
            state = endClockState,
            selection = ClockSelection.HoursMinutes { hours, minute ->
                selectedEndTime = LocalTime.of(hours, minute, 0)
            },
            config = ClockConfig(
                defaultTime = selectedEndTime,
                is24HourFormat = true
            )
        )

        // Participants Dialog
        NumbersDialog("Max Participants", maxParticipantsState, maxParticipants) { maxParticipants = it }
        // Tags Dialog
        TagsListDialog(
            title = "Tags",
            sheetState = tagsState,
            tagsList = tagItems
        )

        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Start Event:")
            Spacer(modifier = modifier.weight(1f))
            ExtendedFloatingActionButton(
                onClick = { startCalendarState.show() },
                modifier.
                    height(25.dp)
            ) {
                Text(text = selectedStartDate
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                )
            }
            Spacer(modifier = modifier.width(8.dp))
            ExtendedFloatingActionButton(
                onClick = { startClockState.show() },
                modifier.
                    height(25.dp)
            ) {
                Text(text = selectedStartTime
                    .format(DateTimeFormatter.ofPattern("HH:mm"))
                )
            }
        }
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "End Event:")
            Spacer(modifier = modifier.weight(1f))
            ExtendedFloatingActionButton(
                onClick = { endCalendarState.show() },
                modifier.
                    height(25.dp)
            ) {
                Text(text = selectedEndDate
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                )
            }
            Spacer(modifier = modifier.width(8.dp))
            ExtendedFloatingActionButton(
                onClick = { endClockState.show() },
                modifier.
                    height(25.dp)
            ){
                Text(text = selectedEndTime
                    .format(DateTimeFormatter.ofPattern("HH:mm"))
                )
            }
        }
        Spacer(modifier = modifier.height(8.dp))
        Row(
            modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Max Participants:")
            Spacer(modifier = modifier.weight(1f))
            ExtendedFloatingActionButton(
                onClick = { maxParticipantsState.show() },
                modifier = modifier
                    .height(25.dp)
            ) {
                Text(text = maxParticipants.toString())
            }
        }
        Row (
            modifier
                .fillMaxWidth()
        ){
            val tagSelected = tagItems.count { it.isSelected }
            Text(text = "Tags:")
            Spacer(modifier = modifier.weight(1f))
            ExtendedFloatingActionButton(
                onClick = { tagsState.show() },
                modifier = modifier
                    .height(25.dp)
            ) {
                Text(text = if (tagSelected == 0) "Select" else "$tagSelected selected")
            }
        }
        Spacer(modifier = modifier.height(8.dp))
        OutlinedTextField(
            value = eventCaption,
            onValueChange = {
                if (it.length <= captionMaxChar) eventCaption = it
                            },
            label = { Text(text = "Write a caption") },
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colorScheme.onBackground,
                containerColor = MaterialTheme.colorScheme.background,
            ),
            shape = MaterialTheme.shapes.small,
            modifier = modifier
                .fillMaxWidth()
                .height(180.dp),
            maxLines = 5,
            supportingText = {
                Text(
                    text = "${eventCaption.length} / $captionMaxChar",
                    modifier = modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.End,
                )
            }
        )

        Spacer(modifier = modifier.weight(1f))

        // Post button
        Button(
            onClick = {
                if (eventTitle.isEmpty()) {
                    Toast.makeText(context, "Please enter an event title", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                eventLocationViewModel.setTitle(eventTitle)

                val startEventDate = LocalDateTime.of(selectedStartDate, selectedStartTime)
                if (startEventDate.isBefore(LocalDateTime.now())) {
                    Toast.makeText(context, "Start date must be in the future", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                eventLocationViewModel.setStartEventDate(startEventDate)

                val endEventDate = LocalDateTime.of(selectedEndDate, selectedEndTime)
                if (startEventDate.isAfter(endEventDate)) {
                    Toast.makeText(context, "End date must be after start date", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                eventLocationViewModel.setEndEventDate(endEventDate)

                if (maxParticipants <= 0) {
                    Toast.makeText(context, "Max participants must be greater than 0", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                eventLocationViewModel.setMaxParticipants(maxParticipants)

                if (capturedImageUri.path?.isNotEmpty() == true) {

                    eventLocationViewModel.setCapturedImageUri(capturedImageUri)
                    eventLocationViewModel.setDescription(eventCaption)

                    val tagListItem = tagItems.filter { it.isSelected }
                    eventLocationViewModel.setTags(tagListItem)

                    onNext()
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
            Text(text = "Next: Select Location")
        }
    }
}