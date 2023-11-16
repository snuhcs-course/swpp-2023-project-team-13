package com.team13.fooriend.ui.screen.mypage

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ChangePwdScreen(
    context : Context,
    onConfirmClick : () -> Unit = {}
){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Current pwd :", modifier = Modifier.align(Alignment.Start))
        Text(text = "New pwd :", modifier = Modifier.align(Alignment.Start))
        Text(text = "Confirm new pwd :", modifier = Modifier.align(Alignment.Start))
        Button(onClick = {
            onConfirmClick()
        }) {
            Text("Confirm")
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun ChangePwdScreenPreview(){
    ChangePwdScreen(
        context = TODO(),
        onConfirmClick = {}
    )
}