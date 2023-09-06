package com.example.clubbers.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.clubbers.R
import com.example.clubbers.data.entities.Admin
import com.example.clubbers.data.entities.User
import com.example.clubbers.viewModel.AdminsViewModel
import com.example.clubbers.viewModel.UsersViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ClubSearchScreen(
    modifier: Modifier,
    onClickAction: () -> Unit,
    adminsViewModel: AdminsViewModel
){

    val admins = adminsViewModel.admins.collectAsState(initial = listOf()).value

    Scaffold(modifier = modifier) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            content = {
                stickyHeader {
                    Divider(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                        thickness = 1.dp,
                        modifier = modifier
                            .shadow(5.dp, RoundedCornerShape(1.dp))
                    )
                }
                items(admins.size) { index ->

                    AdminItem(admins[index], onClickAction = onClickAction, adminsViewModel = adminsViewModel)
                }
            }
        )
    }

}







@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminAutocompleteSearchbar(
    users: List<Admin>,
    adminsViewModel: AdminsViewModel,
    onSearchAction: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val usernames = users.map { it.adminUsername }.distinct()
    val context = LocalContext.current

    OutlinedTextField(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                textFieldSize = coordinates.size.toSize()
            },
        label = { Text(text = "Search by username") },
        value = searchText,
        onValueChange = {
            searchText = it
            expanded = true
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
            cursorColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        textStyle = TextStyle(
            color = Color.Black,
            fontSize = MaterialTheme.typography.bodySmall.fontSize
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        trailingIcon = {
            Row {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowDropDown,
                        contentDescription = "Dropdown menu")
                }
                IconButton(onClick = {
                    if (searchText.isNotEmpty() && searchText == usernames.find { it == searchText }) {
                        adminsViewModel.getAdminByUsername(searchText)
                        onSearchAction()
                    } else {
                        Toast.makeText(context, "User not Found", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_check_24),
                        contentDescription = "Search")
                }
            }
        }
    )

    AnimatedVisibility(visible = expanded) {
        Card(
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .width(textFieldSize.width.dp),
        ) {
            LazyColumn(
                modifier = Modifier
                    .heightIn(max = 150.dp)
            ) {
                if (searchText.isNotEmpty()) {
                    items(
                        usernames.filter {
                            it.lowercase()
                                .contains(searchText.lowercase())
                        }
                            .sorted()
                    ) {
                        UserNames(title = it) { title ->
                            searchText = title
                            expanded = false
                        }
                    }
                } else {
                    items(
                        usernames.sorted()
                    ) {
                        UserNames(title = it) { title ->
                            searchText = title
                            expanded = false
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminItem(
    admin: Admin,
    onClickAction: () -> Unit,
    adminsViewModel: AdminsViewModel
){
    var proPicUri = admin.adminImage
    val propicChanged = LocalContext.current.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE).getString("${admin.adminEmail}_ACTUAL_PROPIC", "NONE").orEmpty()

    if(proPicUri == ""){
        proPicUri = propicChanged
    }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
        onClick ={
            adminsViewModel.selectAdmin(admin)
            onClickAction()
        }) {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(
                        LocalContext.current
                    ).data(data = proPicUri).apply(block = fun ImageRequest.Builder.() {
                        crossfade(true)
                        placeholder(R.drawable.ic_launcher_foreground)
                        error(R.drawable.ic_launcher_foreground)
                    }).build()
                ),
                contentDescription = "Profile Avatar",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )
            Text(admin.adminUsername)
        }
    }
}

