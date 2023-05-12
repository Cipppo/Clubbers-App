package com.example.clubbers.ui

import android.Manifest
import android.app.AppOpsManager.OnOpNotedCallback
import android.content.pm.PackageManager
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.clubbers.R
import com.example.clubbers.utilities.postFeed
import com.example.clubbers.utilities.userBookedEvents


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun PersonalProfileScreen(
    onOption: () -> Unit,
    onNotify: () -> Unit
){



    val selectedMenu = remember{ mutableStateOf("Posts")}
    val scrollState = remember { ScrollState }

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
            Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "username",
                    style = TextStyle(textAlign = TextAlign.Center, fontSize = 25.sp),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Left
                )
                IconButton(onClick = {onNotify()}){
                    Icon(
                        painter = painterResource(id = R.drawable.notifications_icon),
                        contentDescription = "Notifications",
                        modifier = Modifier.size(30.dp)
                    )
                }
                IconButton(onClick = { onOption() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.setting_icon),
                        contentDescription = "Settings",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(R.drawable.default_avatar),
                contentDescription = "DefaultAvatar",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Followers", style = TextStyle(fontWeight = FontWeight.Bold))
                Text(text = "Followed", style = TextStyle(fontWeight = FontWeight.Bold))
                Text(text = "Events", style = TextStyle(fontWeight = FontWeight.Bold))
            }
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "10", style = TextStyle(fontWeight = FontWeight.Bold))
            Text(text = "20", style = TextStyle(fontWeight = FontWeight.Bold))
            Text(text = "30", style = TextStyle(fontWeight = FontWeight.Bold))
        }
            Spacer(modifier = Modifier.height(30.dp))
            Button(onClick = { /*TODO*/ }, shape = CutCornerShape(10), modifier = Modifier.background(MaterialTheme.colorScheme.secondary)) {
                Text("Follow")
            }
            Spacer(modifier = Modifier.height(30.dp))
            Text("UserBio")
            Spacer(modifier = Modifier.height(10.dp))
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
