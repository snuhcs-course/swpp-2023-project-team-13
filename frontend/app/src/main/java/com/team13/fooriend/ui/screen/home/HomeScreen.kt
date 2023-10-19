package com.team13.fooriend.ui.screen.home

import android.content.Context
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.model.LatLng
import com.team13.fooriend.ui.util.LineType
import com.team13.fooriend.ui.util.getCurrentLocation


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(nickname: String, context: Context) {
    var showMap by remember { mutableStateOf(false) }
    var location by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    var changeIcon by remember { mutableStateOf(false) }
    var lineType by remember {
        mutableStateOf<LineType?>(null)
    }

    getCurrentLocation(context) {
        location = it
        showMap = true
    }

    if (showMap) {
        MyMap(
            context = context,
            latLng = location,
            lineType = lineType,
            changeIcon = changeIcon,
            onChangeMarkerIcon = {
                changeIcon = !changeIcon
            }
        )
    } else {
        Text(text = "Loading Map...")
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeScreenPreview(){
    HomeScreen("nickname", context = TODO())
}