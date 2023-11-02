package com.team13.fooriend.ui.screen.home

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.team13.fooriend.R
import com.team13.fooriend.ui.util.LineType
import com.team13.fooriend.ui.util.bitmapDescriptor

@Composable
fun MyMap(
    context: Context,
    latLng: LatLng,
    lineType: LineType?,
    changeIcon: Boolean = false,
    onChangeMarkerIcon: () -> Unit,
) {
    val latlangList = remember {
        mutableStateListOf(latLng)
    }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 15f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = {
                if (lineType == null) {
                    latlangList.add(it)
                }
            }
        ) {
            latlangList.toList().forEach {
                Marker(
                    state = MarkerState(position = it),
                    title = "Location",
                    snippet = "Marker in current location",
                    icon = if (changeIcon) {
                        bitmapDescriptor(context, R.drawable.ic_shopping_cart_24)
                    } else null
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp)
        ) {
            Button(onClick = onChangeMarkerIcon) {
                Text(text = if (changeIcon) "Default Marker" else "Custom Marker")
            }
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}