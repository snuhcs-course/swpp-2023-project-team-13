package com.team13.fooriend.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun FooriendScreen(
    onFollowClick : () -> Unit = {},
){
    Column() {
        Text("FooriendScreen")
        Button(onClick = onFollowClick){
            Text("Follow")
        }
    }
}

