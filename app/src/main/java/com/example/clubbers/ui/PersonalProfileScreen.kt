package com.example.clubbers.ui

import android.Manifest
import android.app.AppOpsManager.OnOpNotedCallback
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.clubbers.R
import com.example.clubbers.data.entities.User
import com.example.clubbers.utilities.postFeed
import com.example.clubbers.utilities.userBookedEvents
import com.example.clubbers.viewModel.UsersViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun PersonalProfileScreen(
    onOption: () -> Unit,
    onNotify: () -> Unit,
    usersViewModel: UsersViewModel,
){



    val selectedMenu = remember{ mutableStateOf("Posts")}
    val userEmail = LocalContext.current.getSharedPreferences("USER_LOGGED", Context.MODE_PRIVATE).getString("USER_LOGGED", "None").orEmpty()
    usersViewModel.getUserFirstNameByEmail(userEmail)
    usersViewModel.getUserBioByEmail(userEmail)


    val userName = usersViewModel.userName.collectAsState().value
    val userBio = usersViewModel.userBio.collectAsState().value

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ){
        Column(
            Modifier
                .padding(30.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)) {
                    Image(
                        painter = painterResource(id = R.drawable.default_avatar),
                        contentDescription = "Profile Avatar",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(text = userName, fontWeight = FontWeight.Bold)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Text(text = "Followers", style = TextStyle(fontWeight = FontWeight.Bold))
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Text(text = "8", style = TextStyle(fontWeight = FontWeight.Bold))
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Text(text = "Followed", style = TextStyle(fontWeight = FontWeight.Bold))
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Text(text = "8", style = TextStyle(fontWeight = FontWeight.Bold))
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Text(text = "Events", style = TextStyle(fontWeight = FontWeight.Bold))
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Text(text = "8", style = TextStyle(fontWeight = FontWeight.Bold))
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(userBio)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth()){
                Button(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) {
                    Text("Follow")
                }
            }
            Divider()
            Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween){
                IconButton(
                    onClick = {selectedMenu.value = "Posts"}
                ){
                    Icon(painter = painterResource(id = R.drawable.posts_icon),
                        contentDescription = "Posts Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(
                            if(selectedMenu.value == "Posts"){
                                45.dp
                            }else{
                                25.dp
                            }))

                }
                IconButton(
                    onClick = {selectedMenu.value = "Booked"}
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.booked_icon),
                        contentDescription = "Next Events Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(
                            if(selectedMenu.value == "Booked"){
                                45.dp
                            }else{
                                25.dp
                            }))
                }
                IconButton(
                    onClick = {selectedMenu.value = "Been"}
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.been_icon),
                        contentDescription = "Been icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(
                            if(selectedMenu.value == "Been"){
                                45.dp
                            }else{
                                25.dp
                            }
                        )
                    )
                }
            }
            if(selectedMenu.value == "Posts"){
                postFeed()  
            }
            if(selectedMenu.value == "Booked"){
                userBookedEvents()
            }
            if(selectedMenu.value == "Been"){
                userBookedEvents()
            }
        }
    }

}
