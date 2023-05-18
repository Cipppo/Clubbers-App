package com.example.clubbers.utilities

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.example.clubbers.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
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
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

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
    capturedImageUri: Uri,
) {
    /**
     * TODO: Create a list of images and display them in a carousel
     *  example: <- image ->
     *      then implement scroll to change image
     */
    // split capturedImageUri on commas and store in a list
    val imageUriList = capturedImageUri.toString().split(",")

    CoreDialog(
        state = sheetState,
        selection = CoreSelection(
            withButtonView = true,
            positiveButton = SelectionButton(
                text = "Add a photo",
            ),
            onPositiveClick = {
                /* TODO */
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
                CarouselCard()
            }

            /*Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp),
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
            }*/
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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CarouselCard() {
    val pagerState = rememberPagerState(initialPage = 0)
    val slider = listOf(
        "https://picsum.photos/id/237/500/800",
        "https://picsum.photos/id/238/500/800",
        "https://picsum.photos/id/239/500/800",
        "https://picsum.photos/id/240/500/800",
        "https://picsum.photos/id/241/500/800"
    )
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        HorizontalPager(
            count = slider.size,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 60.dp),
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
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(slider[page])
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                        .crossfade(true)
                        .scale(Scale.FILL)
                        .build(),
                    contentDescription = "Image"
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
            repeat(slider.size) {
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .size(5.dp)
                        .background(
                            color = if (pagerState.currentPage == it) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
                            }
                        )
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                enabled = pagerState.currentPage < slider.size - 1,
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