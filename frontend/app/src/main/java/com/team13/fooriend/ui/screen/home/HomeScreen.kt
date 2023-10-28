package com.team13.fooriend.ui.screen.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.team13.fooriend.R
import com.team13.fooriend.ui.util.LineType
import com.team13.fooriend.ui.util.getCurrentLocation
import com.team13.fooriend.ui.util.getMarkerIconFromDrawable
import com.team13.fooriend.ui.util.restaurants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@SuppressLint("PotentialBehaviorOverride")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(nickname: String, context: Context, onReviewClick : (Int) -> Unit) {
    var showMap by remember { mutableStateOf(false) }
    var myLocation by remember { mutableStateOf(LatLng(0.0, 0.0)) }

    getCurrentLocation(context) {
        myLocation = it
        showMap = true
    }
    Log.d("MyMap", "myLocation: $myLocation")

    val retrofit = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val placesApiService = retrofit.create(PlacesApiService::class.java)
    val coroutineScope = rememberCoroutineScope()
    var placeId by remember { mutableStateOf<String?>(null)}
    val googleMap = remember { mutableStateOf<GoogleMap?>(null) }
    var cameraTarget by rememberSaveable { mutableStateOf(LatLng(0.0,0.0)) }
    var cameraZoom by rememberSaveable { mutableStateOf(15f) }
    var cameraTilt by rememberSaveable { mutableStateOf(0f) }
    var cameraBearing by rememberSaveable { mutableStateOf(0f) }
    var selectedMarkerTitle by remember { mutableStateOf<String?>(null) }

    Log.d("MyMap", "cameraPosition: $cameraTarget")

    Places.initialize(context, "AIzaSyD-9tSrke72PouQMnMX-a7eZSW0jkFMBWY")

    val markers = restaurants


    // val lastAddedMarker = remember { mutableStateOf<Marker?>(null) }
    // var initialMarkersAdded by remember { mutableStateOf(false) } // 초기 마커들이 추가되었는지 여부

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { innerContext ->
                MapView(innerContext).apply {
                    onCreate(Bundle())
                    getMapAsync { map ->
                        Log.d("factory", "Map loaded")
                        googleMap.value = map

//                        map.setOnPoiClickListener { poi ->
//                            lastAddedMarker.value?.remove() // 마지막에 추가된 마커 삭제
//                            val marker = map.addMarker(MarkerOptions().position(poi.latLng).title(poi.name))
//                            lastAddedMarker.value = marker
//                        }
//
//                        map.setOnMapClickListener { clickedLatLng ->
//                            coroutineScope.launch {
//                                lastAddedMarker.value?.remove() // 마지막에 추가된 마커 삭제
//                                val marker = map.addMarker(MarkerOptions().position(clickedLatLng).title("Random"))
//                                lastAddedMarker.value = marker
//                                placeId = handleMapClick(context, clickedLatLng, placesApi, apiKey)
//                                Log.d("MyMap", "placeId: $placeId")
//                            }
//                        }

                        val clusterManager = ClusterManager<MyItem>(context, map)
                        clusterManager.renderer = CustomMarkerRenderer(context, map, clusterManager)
                        val defaultClusterRenderer = clusterManager.renderer as DefaultClusterRenderer
                        defaultClusterRenderer.minClusterSize = 6
                        clusterManager.setOnClusterItemInfoWindowClickListener { item ->
                            cameraTarget = map.cameraPosition.target
                            cameraZoom = map.cameraPosition.zoom
                            cameraTilt = map.cameraPosition.tilt
                            cameraBearing = map.cameraPosition.bearing
                            onReviewClick(1) // onReviewClick(item.place_id)
                        }
                        map.setOnCameraIdleListener(clusterManager)
                        markers.forEach { myItem ->
                            clusterManager.addItem(myItem)
                        }
                        clusterManager.cluster()

                    }
                }
            },
            update = { mapView ->
                googleMap.value?.let { map ->
                    Log.d("update", "update")
                    if(cameraTarget != LatLng(0.0,0.0))
                        map.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition(cameraTarget, cameraZoom, cameraTilt, cameraBearing)))
                    else
                        map.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(myLocation, 15f)))

                    map.addMarker(
                        MarkerOptions().position(myLocation).title("Current Location").icon(
                        getMarkerIconFromDrawable(context, R.drawable.mypin, 100, 100)
                    ))
                }
            }
        )

    }
    Log.d("MyMap", "Reload")
}


//enum class RestaurantType {
//    KOREAN, JAPANESE, CHINESE, WESTERN, FUSION, SEAFOOD, SALAD, DRINK, BRUNCH, MEET, WINE// ... 추가적인 종류들 ...
//}
// 친구 식당 마커

// need to add private val place_id : String,
// maybe private val restaurantType: RestaurantType
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
        return 1.0f // 마커의 z-index
    }

}


class CustomMarkerRenderer(
    private val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<MyItem>
) : DefaultClusterRenderer<MyItem>(context, map, clusterManager) {

    override fun onBeforeClusterItemRendered(item: MyItem, markerOptions: MarkerOptions) {
        val iconsResId = R.drawable.pin
        markerOptions.icon(getMarkerIconFromDrawable(context, iconsResId, 100, 100))
    }

    // 필요하면 여기에 추가적인 로직을 추가할 수 있습니다.
}

data class SavedCameraPosition(
    val target: LatLng,
    val zoom: Float,
    val tilt: Float,
    val bearing: Float
)

private suspend fun handleMapClick (
    context: Context,
    clickedLatLng: LatLng,
    placesApi: PlacesApiService,
    apiKey: String
): String{
    // nearbysearch API 요청
    val response = placesApi.searchNearbyPlaces(
        location = "${clickedLatLng.latitude},${clickedLatLng.longitude}",
        apiKey = apiKey
    )
    // 가장 가까운 장소 선택
    val nearestPlace = response.results.minByOrNull { place ->
        distanceBetween(clickedLatLng,  place)
    }
    // 로그
    val nearestPlaceName = nearestPlace?.name
    val placeId: String? = nearestPlace?.place_id
    if (nearestPlaceName != null) {
        Log.d("MyMap", "Nearest place: $nearestPlaceName")
    } else {
        Log.e("MyMap", "No nearby places found")
    }
    return placeId?:""
}
//
fun distanceBetween(latLng1: LatLng, placeResult: PlaceResult): Float {
    val results = FloatArray(1)
    val latLng2 = LatLng(placeResult.geometry.location["lat"]!!, placeResult.geometry.location["lng"]!!)
    android.location.Location.distanceBetween(
        latLng1.latitude, latLng1.longitude,
        latLng2.latitude, latLng2.longitude,
        results
    )
    Log.d("MyMap", "Distance between $latLng1 and $latLng2 ${placeResult.name}: ${results[0]}")
    return results[0]
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeScreenPreview(){
    HomeScreen("nickname", context = TODO(), onReviewClick = {TODO()})
}