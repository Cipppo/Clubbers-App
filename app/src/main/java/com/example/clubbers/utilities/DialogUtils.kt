package com.example.clubbers.utilities

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clubbers.R
import com.maxkeppeker.sheets.core.CoreDialog
import com.maxkeppeker.sheets.core.models.CoreSelection
import com.maxkeppeker.sheets.core.models.base.Header
import com.maxkeppeker.sheets.core.models.base.SheetState

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