package com.example.clubbers.utilities

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.SystemClock
import android.provider.MediaStore
import java.io.File
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


// Debug function to get all files from the app directory
fun Context.getFilesFromAppDir(): List<String> {
    val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val files = mutableListOf<String>()
    storageDir?.walkTopDown()?.forEach { file ->
        if (file.isFile) {
            files.add(file.absolutePath)
        }
    }
    return files
}

/**
 * TODO: When the login is implemented, save the image to the user's directory
 */
fun saveImage(context: Context, contentResolver: ContentResolver, capturedImageUri: Uri, photoType: String) {
    val bitmap = getBitmap(capturedImageUri, contentResolver)

    // Save image to gallery
    val values = ContentValues()
    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    values.put(MediaStore.Images.Media.DISPLAY_NAME, "Image_" + SystemClock.currentThreadTimeMillis())

    val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

    val outputStream = imageUri?.let { contentResolver.openOutputStream(it) }
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    outputStream?.close()

    // Save image to app directory
    val directoryName = when(photoType) {
        "Event" -> "Events"
        "Post" -> "Posts"
        "ProPic" -> "ProPics"
        else -> throw IllegalArgumentException("Invalid photo type")
    }
    val appDir = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), directoryName)
    if (!appDir.exists()) {
        appDir.mkdir()
    }
    val file = File(appDir, "Image_" + SystemClock.currentThreadTimeMillis() + ".jpg")
    val fileOutputStream = file.outputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
    fileOutputStream.close()
}

fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
}

private fun getBitmap(selectedPhotoUri: Uri, contentResolver: ContentResolver): Bitmap {
    val bitmap = when {
        Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
            contentResolver,
            selectedPhotoUri
        )
        else -> {
            val source = ImageDecoder.createSource(contentResolver, selectedPhotoUri)
            ImageDecoder.decodeBitmap(source)
        }
    }
    return bitmap
}