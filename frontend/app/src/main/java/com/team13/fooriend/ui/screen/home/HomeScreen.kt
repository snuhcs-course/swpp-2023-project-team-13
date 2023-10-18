package com.team13.fooriend.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.widget.ConstraintLayout
import com.team13.fooriend.ui.screen.login.LogInScreen




@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(nickname: String) {
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeScreenPreview(){
    HomeScreen("nickname")
}