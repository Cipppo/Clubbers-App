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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.example.clubbers.utilities.createImageFile
import com.example.clubbers.utilities.saveImage
import java.util.Objects

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPostScreen(
    modifier: Modifier = Modifier,
    onPost: () -> Unit
) {
    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file)

    var showPopup by rememberSaveable { mutableStateOf(false) }

    var capturedImageUri by rememberSaveable {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    var postCaption by rememberSaveable { mutableStateOf("") }
    val maxChar = 255

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
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(Color.Gray, RoundedCornerShape(4.dp))
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    ImageRequest.Builder(
                                        LocalContext.current
                                    ).data(data = capturedImageUri).apply(block = fun ImageRequest.Builder.() {
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
                    },
                    confirmButton = {
                        Button(onClick = {
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
                        Button(onClick = {
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
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(4.dp))
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
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp),
            maxLines = 2,
//                .heightIn(min = 80.dp, max = 80.dp),
            supportingText = {
                Text(
                    text = "${postCaption.length} / $maxChar",
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.End,
                )
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        // Debug
//        Text(text = "Debug: Show saved images in app dir")
//        context.getFilesFromAppDir()?.count().let {
//            Text(text = "Number of images: $it")
//        }

        // Post button
        Button(
            onClick = {
                if (capturedImageUri.path?.isNotEmpty() == true) {
                    saveImage(context, context.applicationContext.contentResolver, capturedImageUri)
                    onPost()
                } else {
                    Toast.makeText(context, "Please take a photo", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_send_24),
                contentDescription = "Post photo",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Post Photo")
        }
    }
}