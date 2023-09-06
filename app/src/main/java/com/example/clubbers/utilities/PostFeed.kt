package com.example.clubbers.utilities

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.clubbers.R
import com.example.clubbers.data.entities.User
import com.example.clubbers.viewModel.EventsViewModel
import com.example.clubbers.viewModel.PostsViewModel

@Composable
fun PostFeed(
    user: User?,
    postsViewModel: PostsViewModel,
    eventsViewModel: EventsViewModel,
){


    postsViewModel.getPostsByUserId(userId = user?.userId.toString().toInt())
    val posts = postsViewModel.posts.collectAsState().value.reversed()

    val userMail = LocalContext.current.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE).getString("USER_LOGGED", "None")
    val propicChanged = LocalContext.current.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE).getString("${user?.userEmail}_ACTUAL_PROPIC", "NONE").orEmpty()


    var propicUri = ""

    if(user?.userImage == ""){
        propicUri = propicChanged
    }else{
        propicUri = user?.userImage!!
    }




    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ){
        items(posts){ post ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),

            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment =Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                ImageRequest.Builder(
                                    LocalContext.current
                                ).data(data = propicUri).apply(block = fun ImageRequest.Builder.() {
                                    crossfade(true)
                                    placeholder(R.drawable.ic_launcher_foreground)
                                    error(R.drawable.ic_launcher_foreground)
                                }).build()),
                            contentDescription = "PostPic",
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
                        eventsViewModel.getEventById(post.postEventId)
                        var eventName = eventsViewModel.eventSelected?.eventName
                        Text(text= "${user?.userUsername} is been at $eventName", style = MaterialTheme.typography.bodySmall)
                    }
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(
                                LocalContext.current
                            ).data(data = post.postImage).apply(block = fun ImageRequest.Builder.() {
                                crossfade(true)
                                placeholder(R.drawable.ic_launcher_foreground)
                                error(R.drawable.ic_launcher_foreground)
                            }).build()
                        ),
                        contentDescription = "Description",
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                                shape = MaterialTheme.shapes.medium
                            )
                            .heightIn(min = 180.dp)
                    )
                    Text(post.postCaption)
                }
            }
        }
    }
}