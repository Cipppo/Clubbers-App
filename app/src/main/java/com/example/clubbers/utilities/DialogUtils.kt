package com.example.clubbers.utilities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.clubbers.R
import com.example.clubbers.data.details.TagsListItem
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
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
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
fun TakenPhotoCarouselDialog(
    title: String,
    sheetState: SheetState,
    imageUriList: MutableList<Uri>,
) {
    val context = LocalContext.current

    val currentImageIndex: MutableState<Int> = remember { mutableStateOf(0) }

    var file by remember { mutableStateOf<File?>(null) }
    var uri by remember { mutableStateOf<Uri?>(null) }

    val picOrGalleryState = rememberSheetState()

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

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { newUri ->
        if (newUri != null) {
            imageUriList.add(newUri)
        } else {
            // Handle cancellation event
            Toast.makeText(context, "Gallery cancelled", Toast.LENGTH_SHORT).show()
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
                if (imageUriList.size == 10) {
                    Toast.makeText(context, "You can't add more than 10 photos", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    file = context.createImageFile()
                    uri = file?.let {
                        FileProvider.getUriForFile(
                            Objects.requireNonNull(context),
                            "${context.packageName}.provider",
                            it
                        )
                    }

                    picOrGalleryState.show()

//                    val permissionCheckResult = ContextCompat.checkSelfPermission(
//                        context,
//                        Manifest.permission.CAMERA
//                    )
//                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
//                        cameraLauncher.launch(uri)
//                    } else {
//                        permissionLauncher.launch(Manifest.permission.CAMERA)
//                    }
                }
                              },
            negativeButton = SelectionButton(
                text = "Delete",
            ),
            onNegativeClick = {
                val currentImageUri = imageUriList.getOrNull(currentImageIndex.value)
                currentImageUri?.let { deletedUri ->
                    val index = imageUriList.indexOf(deletedUri)
                    if (index != -1) {
                        imageUriList.removeAt(index)
                        if (currentImageIndex.value >= index) {
                            currentImageIndex.value = if (index == 0) 0 else index - 1
                        }
                    }
                }
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
                    capturedImageUris = imageUriList,
                    currentImageIndex = currentImageIndex,
                )
            }
        }
    )

    PicOrGalleyChoicePopup(
        context = context,
        uri = if (uri != null) uri!! else Uri.EMPTY,
        galleryLauncher = galleryLauncher,
        cameraLauncher = cameraLauncher,
        permissionLauncher = permissionLauncher,
        title = "Chose what to do",
        sheetState = picOrGalleryState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TakenPhotoDialog(
    title: String,
    sheetState: SheetState,
    capturedImageUri: MutableState<Uri>
) {
    CoreDialog(
        state = sheetState,
        selection = CoreSelection(
            withButtonView = true,
            positiveButton = SelectionButton(
                text = "Done",
            ),
            onPositiveClick = {
                sheetState.hide()
            },
            negativeButton = SelectionButton(
                text = "Delete",
            ),
            onNegativeClick = {
                sheetState.hide()
                capturedImageUri.value = Uri.EMPTY
            }
        ),
        header = Header.Default(
            title = title
        ),
        body = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(
                            LocalContext.current
                        ).data(data = capturedImageUri.value)
                            .apply(block = fun ImageRequest.Builder.() {
                                crossfade(true)
                                placeholder(R.drawable.ic_launcher_foreground)
                                error(R.drawable.ic_launcher_foreground)
                            }).build()
                    ),
                    contentDescription = "Captured Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PicOrGalleyChoicePopup(
    context: Context,
    uri: Uri,
    galleryLauncher: ActivityResultLauncher<String>,
    cameraLauncher: ActivityResultLauncher<Uri>,
    permissionLauncher: ActivityResultLauncher<String>,
    title: String,
    sheetState: SheetState
) {
    CoreDialog(
        state = sheetState,
        selection = CoreSelection(
            withButtonView = true,
            positiveButton = SelectionButton(
                text = "Take Picture",
            ),
            onPositiveClick = {
                val permissionCheckResult = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                )
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    cameraLauncher.launch(uri)
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
                sheetState.hide()
            },
            negativeButton = SelectionButton(
                text = "Select from Gallery",
            ),
            onNegativeClick = {
                val permissionCheckResult = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                )
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    galleryLauncher.launch("image/*")
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
                sheetState.hide()
            }
        ),
        header = Header.Default(
            title = title
        ),
        body = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagsListDialog(
    title: String,
    sheetState: SheetState,
    tagsList: MutableList<TagsListItem>
) {
    CoreDialog(
        state = sheetState,
        selection = CoreSelection(
            withButtonView = true,
            positiveButton = SelectionButton(
                text = "Done",
            ),
            onPositiveClick = {
                sheetState.hide()
            },
            negativeButton = SelectionButton(
                text = "Reset",
            ),
            onNegativeClick = {
                for (i in tagsList.indices) {
                    tagsList[i] = tagsList[i].copy(isSelected = false)
                }
            },
        ),
        header = Header.Default(
            title = title
        ),
        body = {
            LazyColumn(
                modifier = Modifier
                    .height(350.dp)
                    .fillMaxWidth(),
                content = {
                    items(tagsList.size) { index ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    tagsList[index] = tagsList.mapIndexed { i, item ->
                                        if (i == index) {
                                            item.copy(isSelected = !item.isSelected)
                                        } else {
                                            item
                                        }
                                    }[index]
                                }
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(text = tagsList[index].name)
                            if(tagsList[index].isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Divider()
                    }
                }
            )
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