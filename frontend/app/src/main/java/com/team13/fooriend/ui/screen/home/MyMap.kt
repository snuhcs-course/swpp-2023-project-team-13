package com.team13.fooriend.ui.screen.home

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PointOfInterest
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.team13.fooriend.R
import com.team13.fooriend.ui.util.LineType
import com.team13.fooriend.ui.util.bitmapDescriptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MyMap(
    context: Context,
    latLng: LatLng,
    placesApi: PlacesApiService,
    apiKey: String
) {
    Places.initialize(context, apiKey)
    var markerPosition by remember { mutableStateOf<LatLng?>(latLng) }
    val coroutineScope = rememberCoroutineScope()
    var placeId by remember { mutableStateOf<String?>(null)}

    val googleMap = remember { mutableStateOf<GoogleMap?>(null) }
    val markers = listOf(
        PlaceMarker(1, LatLng(37.47861054481183, 127.00040582567452), "Marker 1"),
        PlaceMarker(2, LatLng(37.47921478418604, 127.00007289648055), "Marker 2"),
        // ... 추가적인 마커들 ...
    )
    var clusterManager: ClusterManager<MyItem>
    Log.d("Map", "reComposed $markerPosition")
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { innerContext ->
                MapView(innerContext).apply {
                    onCreate(Bundle())
                    getMapAsync { map ->
                        googleMap.value = map
                        map.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(latLng, 15f)))
                        map.setOnPoiClickListener { poi ->
                            markerPosition = poi.latLng
                            Log.d("MyMap", "POI Name: ${poi.name}")
                        }
                        clusterManager = ClusterManager(context, map)

                        // Point the map's listeners at the listeners implemented by the cluster
                        // manager.
                        map.setOnCameraIdleListener(clusterManager)
                        map.setOnMarkerClickListener(clusterManager)
//                        map.setOnMapClickListener { clickedLatLng ->
//                            coroutineScope.launch {
//                                markerPosition = clickedLatLng
//                                placeId = handleMapClick(context, clickedLatLng, placesApi, apiKey)
//                                Log.d("MyMap", "placeId: $placeId")
//                            }
//                        }
                    }
                }
            },
            update = { mapView ->
                googleMap.value?.let { map ->
                    // 이미 추가된 마커는 제거합니다.
                    map.clear()
                    // 새로운 위치에 마커를 추가합니다.
                    Log.d("MyMap", "markerPosition: $markerPosition")
                    markerPosition?.let {
                        map.addMarker(MarkerOptions().position(it).title("Selected Location"))
                    }
                }
            }
        )
    }
}

data class PlaceMarker(val id: Int, val latLng: LatLng, val title: String)

class MyItem(private val position: LatLng, private val title: String) : ClusterItem {
    override fun getPosition(): LatLng {
        return position
    }

    override fun getTitle(): String? {
        return title
    }

    override fun getSnippet(): String? {
        return null
    }

    override fun getZIndex(): Float {
        return 1.0f  // 원하는 Z 인덱스 값을 반환합니다. 기본적으로 1.0f를 반환해도 됩니다.
    }
}



//private suspend fun handleMapClick (
//    context: Context,
//    clickedLatLng: LatLng,
//    placesApi: PlacesApiService,
//    apiKey: String
//): String{
//    // nearbysearch API 요청
//    val response = placesApi.searchNearbyPlaces(
//        location = "${clickedLatLng.latitude},${clickedLatLng.longitude}",
//        apiKey = apiKey
//    )
//    // 가장 가까운 장소 선택
//    val nearestPlace = response.results.minByOrNull { place ->
//        distanceBetween(clickedLatLng,  place)
//    }
//    // 로그
//    val nearestPlaceName = nearestPlace?.name
//    val placeId: String? = nearestPlace?.place_id
//    if (nearestPlaceName != null) {
//        Log.d("MyMap", "Nearest place: $nearestPlaceName")
//    } else {
//        Log.e("MyMap", "No nearby places found")
//    }
//    return placeId?:""
//}
//
//fun distanceBetween(latLng1: LatLng, placeResult: PlaceResult): Float {
//    val results = FloatArray(1)
//    val latLng2 = LatLng(placeResult.geometry.location["lat"]!!, placeResult.geometry.location["lng"]!!)
//    android.location.Location.distanceBetween(
//        latLng1.latitude, latLng1.longitude,
//        latLng2.latitude, latLng2.longitude,
//        results
//    )
//    Log.d("MyMap", "Distance between $latLng1 and ${placeResult.name} $latLng2: ${results[0]}")
//    return results[0]
//}
