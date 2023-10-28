package com.team13.fooriend.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FooriendScreen(
    onBackClick : () -> Unit = {},
    onFollowClick : () -> Unit = {},
){
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        IconButton(onClick = onBackClick) {
            Icon(Icons.Default.ArrowBack, contentDescription = "back")
        }
        Text("FooriendScreen")
        Button(onClick = onFollowClick){
            Text("Follow")
        }
    }
}

