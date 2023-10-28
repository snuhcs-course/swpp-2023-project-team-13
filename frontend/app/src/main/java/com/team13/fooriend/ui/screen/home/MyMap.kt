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
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PointOfInterest
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.team13.fooriend.R
import com.team13.fooriend.ui.util.LineType
import com.team13.fooriend.ui.util.getMarkerIconFromDrawable
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
    val coroutineScope = rememberCoroutineScope()
    var placeId by remember { mutableStateOf<String?>(null)}

    val googleMap = remember { mutableStateOf<GoogleMap?>(null) }
    val markers = listOf(
        MyItem(LatLng(37.4773666, 126.9531265), "Taco Bueno"),
        MyItem(LatLng(37.4784436, 126.9565545), "Tendong Yotsuya"),
        MyItem(LatLng(37.4790055, 126.9537665), "순이네밥상"),
        MyItem(LatLng(37.4790948, 126.95556), "멘쇼우라멘"),
        MyItem(LatLng(37.4777708, 126.9573454), "누들하우스"),
        MyItem(LatLng(37.4791105, 126.9537991), "Jeongsugseong"),
        MyItem(LatLng(37.477904, 126.9524293), "Frank Burger"),
        MyItem(LatLng(37.480378, 126.9534373), "Samcha"),
        MyItem(LatLng(37.4788032, 126.9545337), "모다 모다"),
        MyItem(LatLng(37.480606, 126.9515086), "Hanam Pig House Seoul National University Station"),
        MyItem(LatLng(37.4806412, 126.9529295), "七里香刀削面"),
        MyItem(LatLng(37.4798268, 126.9539257), "부엌우동집"),
        MyItem(LatLng(37.4781838, 126.9530122), "Chamjag"),
        MyItem(LatLng(37.47861054481183, 127.00040582567452), "Marker 1"),
        MyItem(LatLng(37.47921478418604, 127.00007289648055), "Marker 2"),
        MyItem(LatLng(37.477953218751, 127.0015639782424), "Marker 3"),
        MyItem(LatLng(37.4801124123123, 127.002785610199), "Marker 4"),
        MyItem(LatLng(37.4789341238123, 127.003456782312), "Marker 5"),
        MyItem(LatLng(37.477123912312, 127.002312315678), "Marker 6")
    )


    val lastAddedMarker = remember { mutableStateOf<Marker?>(null) }
    // var initialMarkersAdded by remember { mutableStateOf(false) } // 초기 마커들이 추가되었는지 여부

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { innerContext ->
                MapView(innerContext).apply {
                    onCreate(Bundle())
                    getMapAsync { map ->
                        Log.d("factory", "Map loaded")
                        googleMap.value = map
                        map.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(latLng, 15f)))

                        map.setOnPoiClickListener { poi ->
                            lastAddedMarker.value?.remove() // 마지막에 추가된 마커 삭제
                            val marker = map.addMarker(MarkerOptions().position(poi.latLng).title("POI"))
                            lastAddedMarker.value = marker
                            Log.d("MyMap", "POI Name: ${poi.name}")
                        }
                        val clusterManager = ClusterManager<MyItem>(context, map)
                        clusterManager.renderer = CustomMarkerRenderer(context, map, clusterManager)
                        // Point the map's listeners at the listeners implemented by the cluster
                        // manager.
                        map.setOnCameraIdleListener(clusterManager)
                        map.setOnMarkerClickListener(clusterManager)
                        markers.forEach { myItem ->
                            clusterManager.addItem(myItem)
                        }
                        clusterManager.cluster()

                        map.setOnMapClickListener { clickedLatLng ->
                            coroutineScope.launch {
                                lastAddedMarker.value?.remove() // 마지막에 추가된 마커 삭제
                                val marker = map.addMarker(MarkerOptions().position(clickedLatLng).title("Random"))
                                lastAddedMarker.value = marker
                                placeId = handleMapClick(context, clickedLatLng, placesApi, apiKey)
                                Log.d("MyMap", "placeId: $placeId")
                            }
                        }
                    }
                }
            },
            update = { mapView ->
                googleMap.value?.let { map ->
                    Log.d("update", "update")
                }
            }
        )
    }
    Log.d("MyMap", "Reload")
}

// 친구 식당 마커

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
        markerOptions.icon(getMarkerIconFromDrawable(context, R.drawable.chick, 100, 100))
    }

    // 필요하면 여기에 추가적인 로직을 추가할 수 있습니다.
}



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
