package com.example.clubbers.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.clubbers.R
import com.example.clubbers.viewModel.UsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    usersViewModel: UsersViewModel,
    modifier: Modifier = Modifier
) {
    Scaffold { innerPadding ->
        Column(
            modifier
                .padding(innerPadding)
                .fillMaxSize()) {
            SearchBar(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .clip(RoundedCornerShape(16.dp))
            )
            Divider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                thickness = 1.dp,
                modifier = modifier.
                        shadow(5.dp, RoundedCornerShape(1.dp))
            )
            LazyColumn(
                modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                items(20) {
                    PostItem(username = "User $it")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    var searchText by remember { mutableStateOf("") }

    TextField (
        value = searchText,
        onValueChange = { searchText = it },
        modifier = modifier,
        label = { Text(text = "Search") },
        singleLine = true,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_search_24),
                contentDescription = "Search icon"
            )
        }
    )
}

@Composable
fun PostItem(username: String) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .border(
                1.dp,
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                shape = MaterialTheme.shapes.medium
            ),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Description",
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
                Text(text = username, style = MaterialTheme.typography.bodySmall)
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
            Text(
                text = "Description",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.padding(top = 8.dp))
            Text(text = "Tags: tag1, tag2, tag3", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.padding(top = 8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 8.dp)
            ) {
                Text(text = "Date", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "Place", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun PostList(usersViewModel: UsersViewModel) {
    for (i in 0..20) {
//        val user = usersViewModel.getUserById(post.postUserId).collectAsState(initial = null).value
//        val username = user?.userName ?: ""
        val username = "username $i"
        /**
         * TODO: Get the profile picture from the user
         * val context = LocalContext.current
         * val filesDir = context.filesDir
         * val proPic = user?.userImage ?: ""
         * */

        PostItem(username = username)
    }
}