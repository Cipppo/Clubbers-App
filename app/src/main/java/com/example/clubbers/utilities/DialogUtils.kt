package com.example.clubbers.utilities

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.clubbers.R
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.maxkeppeker.sheets.core.CoreDialog
import com.maxkeppeker.sheets.core.models.CoreSelection
import com.maxkeppeker.sheets.core.models.base.Header
import com.maxkeppeker.sheets.core.models.base.SelectionButton
import com.maxkeppeker.sheets.core.models.base.SheetState
import java.io.File
import java.util.Objects

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun NumbersDialog(
    title: String,
    sheetState: SheetState,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    var newValue by rememberSaveable { mutableStateOf(value) }

    CoreDialog(
        state = sheetState,
        selection = CoreSelection(
            withButtonView = false
        ),
        header = Header.Default(
            title = title
        ),
        body = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = newValue.toString(),
                    style = TextStyle(fontSize = 48.sp),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(25.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ) {
                    items(9) { i ->
                        Box(
                            modifier = Modifier
                                .padding(4.dp, 20.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable {
                                    newValue = (newValue * 10) + (i + 1)
                                }
                                .aspectRatio(1f)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = (i + 1).toString(),
                                style = TextStyle(fontSize = 24.sp),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    item {
                        Box(
                            modifier = Modifier
                                .padding(4.dp, 20.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable { newValue /= 10 }
                                .aspectRatio(1f)
                                .background(MaterialTheme.colorScheme.secondaryContainer),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_backspace_24),
                                contentDescription = "Backspace"
                            )
                        }
                    }

                    item {
                        Box(
                            modifier = Modifier
                                .padding(4.dp, 20.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable { newValue *= 10 }
                                .aspectRatio(1f)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "0",
                                style = TextStyle(fontSize = 24.sp),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    item {
                        Box(
                            modifier = Modifier
                                .padding(4.dp, 20.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable {
                                    sheetState.hide()
                                    onValueChange(newValue)
                                }
                                .aspectRatio(1f)
                                .background(MaterialTheme.colorScheme.secondaryContainer),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_keyboard_return_24),
                                contentDescription = "Accept number"
                            )
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapDialog(
    title: String,
    sheetState: SheetState,
    placeName: String,
    initialCameraPosition: CameraPosition,
    locationLatLng: LatLng
) {
    CoreDialog(
        state = sheetState,
        selection = CoreSelection(
            withButtonView = false
        ),
        header = Header.Default(
            title = title
        ),
        body = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                MapView(
                    placeName = placeName,
                    initialCameraPosition = initialCameraPosition,
                    markerPosition = locationLatLng
                )
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TakenPhotoDialog(
    title: String,
    sheetState: SheetState,
    imageUriList: MutableList<Uri>,
) {
    val context = LocalContext.current

    var file by remember { mutableStateOf<File?>(null) }
    var uri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            if ((file != null) && (uri != null)) imageUriList.add(uri!!)
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

    CoreDialog(
        state = sheetState,
        selection = CoreSelection(
            withButtonView = true,
            positiveButton = SelectionButton(
                text = "Add a photo",
            ),
            onPositiveClick = {
                file = context.createImageFile()
                uri = file?.let {
                    FileProvider.getUriForFile(
                        Objects.requireNonNull(context),
                        "${context.packageName}.provider",
                        it
                    )
                }

                val permissionCheckResult = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                )
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    cameraLauncher.launch(uri)
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
                              },
            negativeButton = SelectionButton(
                text = "Delete",
            ),
            onNegativeClick = {
                /* TODO */
            },
            extraButton = SelectionButton(
                text = "Done",
            ),
            onExtraButtonClick = {
                sheetState.hide()
            }
        ),
        header = Header.Default(
            title = title
        ),
        body = {
            Column() {
                CarouselCard(
                    imageUriList
                )
            }
        }
    )
}

@Composable
fun MapView(
    placeName: String,
    initialCameraPosition: CameraPosition,
    markerPosition: LatLng
) {
    val cameraPositionState = rememberCameraPositionState { position = initialCameraPosition }
    val markerState = MarkerState(markerPosition)

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(500.dp)
        .fillMaxWidth()
        .clip(shape = MaterialTheme.shapes.small)
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = markerState,
                title = placeName,
            )
        }
    }
}