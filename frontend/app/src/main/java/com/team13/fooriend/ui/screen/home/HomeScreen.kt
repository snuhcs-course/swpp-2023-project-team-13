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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(nickname: String, context: Context, onReviewClick : (Int) -> Unit) {
    var showMap by remember { mutableStateOf(false) }
    var location by remember { mutableStateOf(LatLng(0.0, 0.0)) }

    getCurrentLocation(context) {
        location = it
        showMap = true
    }

    val retrofit = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val placesApiService = retrofit.create(PlacesApiService::class.java)


    if (showMap) {
        MyMap(
            context = context,
            latLng = location,
            placesApi = placesApiService,
            apiKey = "AIzaSyDV4YwwZmJp1PHNO4DSp_BdgY4qCDQzKH0",
            onReviewClick = onReviewClick,
        )
    } else {
        Text(text = "Loading Map...")
    }
}



@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeScreenPreview(){
    HomeScreen("nickname", context = TODO(), onReviewClick = {TODO()})
}