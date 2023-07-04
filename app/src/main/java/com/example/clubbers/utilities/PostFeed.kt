package com.example.clubbers.utilities

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.clubbers.R
import com.example.clubbers.viewModel.PostsViewModel

@Composable
fun postFeed(
    userId: Int,
    postsViewModel: PostsViewModel
){


    postsViewModel.getPostsByUserId(userId = userId)
    val posts = postsViewModel.posts.collectAsState().value





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
                            painter = painterResource(id = R.drawable.default_avatar),
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
                        Text(text= "username is been at NOME EVENTO", style = MaterialTheme.typography.bodySmall)
                    }
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
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
                    Text("PostCaption")
                }
            }
        }
    }
}