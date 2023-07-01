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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.clubbers.data.entities.Post
import com.example.clubbers.utilities.TakenPhotoDialog
import com.example.clubbers.utilities.createImageFile
import com.example.clubbers.utilities.getFilesFromAppDir
import com.example.clubbers.utilities.saveImage
import com.example.clubbers.viewModel.EventsViewModel
import com.example.clubbers.viewModel.PostsViewModel
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import java.util.Objects

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPostScreen(
    modifier: Modifier = Modifier,
    postsViewModel: PostsViewModel,
    eventsViewModel: EventsViewModel,
    userId: Int,
    onPost: () -> Unit
) {
    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file)

    val takenPhotoSheetState = rememberSheetState()
    
    val imageUriList = remember { mutableStateListOf<Uri>() }

    var localImageDirList by rememberSaveable { mutableStateOf("") }
    var postCaption by rememberSaveable { mutableStateOf("") }
    val maxChar = 255

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUriList.add(uri)
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
        Box(
            modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(Color.Gray, MaterialTheme.shapes.small)
                .clickable(
                    onClick = {
                        if (imageUriList.isNotEmpty()) {
                            takenPhotoSheetState.show()
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
            if (imageUriList.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current).data(data = imageUriList.first())
                            .apply(block = fun ImageRequest.Builder.() {
                                crossfade(true)
                                placeholder(R.drawable.ic_launcher_foreground)
                                error(R.drawable.ic_launcher_foreground)
                            }).build()
                    ),
                    contentDescription = "Captured Image",
                    contentScale = ContentScale.Crop,
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
        OutlinedTextField(
            value = postCaption,
            onValueChange = {
                if (it.length <= maxChar) postCaption = it
                            },
            label = { Text(text = "Write a caption") },
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colorScheme.onBackground,
                containerColor = MaterialTheme.colorScheme.background,
            ),
            shape = MaterialTheme.shapes.small,
            modifier = modifier
                .fillMaxWidth()
                .height(110.dp),
            maxLines = 2,
            supportingText = {
                Text(
                    text = "${postCaption.length} / $maxChar",
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
                val photoType = "Post"
                val eventId = eventsViewModel.eventSelected!!.eventId

                if (imageUriList.isNotEmpty()) {
                    imageUriList.forEach {
                        saveImage(context, context.applicationContext.contentResolver, it, photoType)
                    }

                    val lastNFiles = context.getFilesFromAppDir().takeLast(imageUriList.size)
                    localImageDirList = lastNFiles.joinToString(separator = ",") { it }

                    postsViewModel.addNewPost(
                        Post(
                            postImage = localImageDirList,
                            postCaption = postCaption,
                            postUserId = userId,
                            postEventId = eventId
                        )
                    )

                    onPost()
                } else {
                    Toast.makeText(context, "Please take a photo", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_send_24),
                contentDescription = "Post photo",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = modifier.width(8.dp))
            Text(text = "Post Photo")
        }
    }

    TakenPhotoDialog(
        title = "Taken Photo",
        sheetState = takenPhotoSheetState,
        imageUriList = imageUriList
    )
}