package com.example.clubbers.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.clubbers.data.entities.Post
import com.example.clubbers.data.entities.User

import com.example.clubbers.viewModel.EventsViewModel
import com.example.clubbers.viewModel.PostsViewModel
import com.example.clubbers.viewModel.UserFollowsUsersViewModel
import com.example.clubbers.viewModel.UsersViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.clubbers.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(

    modifier: Modifier = Modifier,
    postsViewModel: PostsViewModel,
    eventsViewModel: EventsViewModel,
    usersViewModel: UsersViewModel,
    userFollowsUsersViewModel: UserFollowsUsersViewModel,

) {

    val sharedPreferences = LocalContext.current.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE)
    val userMail = sharedPreferences.getString("USER_LOGGED", "None")
    val userType = LocalContext.current.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE).getString("USER_TYPE", "NONE").orEmpty()


    var user = User(
        userId = 1,
        userName = "user1",
        userSurname = "user1",
        userUsername = "user1",
        userEmail = "user1",
        userPassword = "user1",
        userImage = "",
        userBio = "prova bio user1",
        isAdmin = false
    )

    if(userType == "USER"){
        if(userMail == "user1"){
            user = User(
                userId = 1,
                userName = "user1",
                userSurname = "user1",
                userUsername = "user1",
                userEmail = "user1",
                userPassword = "user1",
                userImage = "",
                userBio = "prova bio user1",
                isAdmin = false
            )
        }else if(userMail == "user2"){
            user = User(
                userId = 2,
                userName = "user2",
                userSurname = "user2",
                userUsername = "user2",
                userEmail = "user2",
                userPassword = "user2",
                userImage = "",
                userBio = "prova bio user2",
                isAdmin = false
            )
        }
    }


    userFollowsUsersViewModel.getFollowed(user.userId)

    val userFollowed = userFollowsUsersViewModel.followed.collectAsState().value

    val userIdList = userFollowed.map { user -> user.userId }


    postsViewModel.getAllPosts()
    val posts = postsViewModel.allPosts.collectAsState(initial = listOf()).value

    var postList = emptyList<Post>()

    for(post in posts){
        if(userIdList.contains(post.postUserId)){
            postList = postList + post
        }
    }

    if(postList == emptyList<Post>()){
        Text("No posts to be shown")
    }else{
        LazyColumn(modifier = Modifier.fillMaxWidth().fillMaxHeight()){
            items(postList){post ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),

                    ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp)) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    ImageRequest.Builder(
                                        LocalContext.current
                                    ).data(data = user?.userImage).apply(block = fun ImageRequest.Builder.() {
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





}