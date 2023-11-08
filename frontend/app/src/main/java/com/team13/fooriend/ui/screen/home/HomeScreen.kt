package com.team13.fooriend.ui.screen.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import com.team13.fooriend.R
import com.team13.fooriend.ui.util.getCurrentLocation
import com.team13.fooriend.ui.util.getMarkerIconFromDrawable
//import com.team13.fooriend.ui.util.restaurants
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@SuppressLint("PotentialBehaviorOverride", "MissingPermission")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(
    nickname: String,
    context: Context,
    onReviewClick : (Int) -> Unit
) {
    var showMap by remember { mutableStateOf(false) }
    var myLocation by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    var myLocationMarker by remember { mutableStateOf<Marker?>(null) }

    getCurrentLocation(context) {
        myLocation = it
        showMap = true
    }
    Log.d("MyMap", "myLocation: $myLocation")

    val retrofit = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val placesApi = retrofit.create(PlacesApiService::class.java)

    val retrofit2 = Retrofit.Builder()
        .baseUrl("http://ec2-54-180-101-207.ap-northeast-2.compute.amazonaws.com") // 기본 URL 설정
        .addConverterFactory(GsonConverterFactory.create()) // Gson 변환기 사용
        .build()

    val apiService = retrofit2.create(ApiService::class.java)

    val coroutineScope = rememberCoroutineScope()
    var placeId by remember { mutableStateOf<String?>(null)}
    val googleMap = remember { mutableStateOf<GoogleMap?>(null) }
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(myLocation, 15f)
    }
    var isReturn by remember { mutableStateOf(false) }
    var selectedMarkerTitle by remember { mutableStateOf<String?>(null) }

    Log.d("MyMap", "cameraPosition: ${cameraPositionState.position}")
    Places.initialize(context, "AIzaSyDV4YwwZmJp1PHNO4DSp_BdgY4qCDQzKH0")
//    var isMarkerClicked by remember { mutableStateOf(false) }
    var markerName by remember { mutableStateOf("") }

    val locationRequest = LocationRequest.Builder(10000).build()

    val lastAddedMarker = remember { mutableStateOf<Marker?>(null) }
    var lastMarkerUpdated by remember { mutableStateOf(false) } // 초기 마커들이 추가되었는지 여부
    val markers by remember { mutableStateOf<List<MyItem>>(emptyList()) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { innerContext ->
                MapView(innerContext).apply {
                    onCreate(Bundle())
                    getMapAsync { map ->
                        Log.d("factory", "Map loaded")
                        googleMap.value = map
                        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPositionState.position))
                        val locationProviderClient = LocationServices.getFusedLocationProviderClient(context)
                        val locationRequest = LocationRequest.Builder(10000).build()
                        val locationCallback = object : LocationCallback() {
                            override fun onLocationResult(p0: LocationResult) {
                                p0 ?: return
                                for (location in p0.locations) {
                                    // 새 위치로 미커 이동
//                                    Log.d("MyMap", "Location updated: ${location.latitude}, ${location.longitude}")
                                    myLocationMarker?.remove()
                                    myLocation = LatLng(location.latitude, location.longitude)
                                    myLocationMarker = map.addMarker(
                                        MarkerOptions().position(myLocation).title("Current Location").icon(
                                            getMarkerIconFromDrawable(context, R.drawable.mypin, 100, 100)
                                        )
                                    )
                                }
                            }
                        }
                        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

                        map.setOnPoiClickListener { poi ->
                            val matchingItem = markers.find { it.placeId == poi.placeId }

                            // 일치하는 MyItem이 있으면 로그를 찍거나 원하는 다른 작업을 수행
                            matchingItem?.let {
                                // MyItem을 처리하는 코드, 예를 들어 로그 출력
                                Log.d("MyMap", "Matching MyItem found: ${it.title}")
                                lastAddedMarker.value?.remove() // 마지막에 추가된 마커 삭제
                                val marker = map.addMarker(
                                    MarkerOptions().position(it.position).title(it.title).icon(getMarkerIconFromDrawable(context, R.drawable.transparent, 100, 100)
                                    ).zIndex(2.0f)
                                )
                                lastAddedMarker.value = marker
                                cameraPositionState.position = CameraPosition(it.position, map.cameraPosition.zoom, map.cameraPosition.tilt, map.cameraPosition.bearing)
                                map.setOnInfoWindowClickListener { clickedMarker ->
                                    onReviewClick(1) // onReviewClick(clickedMarker.placeId)
                                }
                                lastMarkerUpdated = true
                                return@setOnPoiClickListener // MyItem을 찾았으므로 여기서 리스너 작업을 종료
                            }
                            coroutineScope.launch {
                                Log.d("MyMap", "Poi clicked: ${poi.placeId}")
                                val response = placesApi.getPlaceDetails(
                                    placeId = poi.placeId,
                                    apiKey = "AIzaSyDV4YwwZmJp1PHNO4DSp_BdgY4qCDQzKH0"
                                )

                                if(response.result == null) {
                                    Log.e("MyMap", "No place details found")
                                    return@launch
                                }
                                Log.d("MyMap", "Place details: ${response.result.name}")
                                // move map camera preserving zoom level

                                val latLng = LatLng(
                                    response.result.geometry.location["lat"]!!,
                                    response.result.geometry.location["lng"]!!
                                )
                                cameraPositionState.position = CameraPosition(latLng, map.cameraPosition.zoom, map.cameraPosition.tilt, map.cameraPosition.bearing)
                                val marker = map.addMarker(
                                    MarkerOptions().position(latLng).title(poi.name)
                                )
                                map.setOnInfoWindowClickListener { clickedMarker ->
                                    if (clickedMarker.id == marker?.id) {
                                        onReviewClick(1) // onReviewClick(poi.placeId)
                                    }
                                    true
                                }
                                lastAddedMarker.value?.remove() // 마지막에 추가된 마커 삭제
                                lastAddedMarker.value = marker
                                lastMarkerUpdated = true
                            }
                        }
                        map.setOnMapClickListener { clickedLatLng ->
//                            coroutineScope.launch {
//                                lastAddedMarker.value?.remove() // 마지막에 추가된 마커 삭제
//                                val marker = map.addMarker(MarkerOptions().position(clickedLatLng).title("Random"))
//                                lastAddedMarker.value = marker
//                                placeId = handleMapClick(context, clickedLatLng, placesApi, "AIzaSyDV4YwwZmJp1PHNO4DSp_BdgY4qCDQzKH0")
//                                Log.d("MyMap", "placeId: $placeId")
//                            }
                            lastAddedMarker.value?.remove()
                        }
                        val clusterManager = ClusterManager<MyItem>(context, map)
                        coroutineScope.launch {
                            val markers = apiService.getFriendsRestaurants().restaurantList.map { restaurant ->
                                MyItem(
                                    position = LatLng(restaurant.latitude, restaurant.longitude),
                                    title = restaurant.name,
                                    placeId = restaurant.googleMapPlaceId,
                                    id = restaurant.id
                                )
                            }
                            markers.forEach { myItem ->
                                clusterManager.addItem(myItem)
                            }
                            clusterManager.renderer = CustomMarkerRenderer(context, map, clusterManager)
                            val defaultClusterRenderer = clusterManager.renderer as DefaultClusterRenderer
                            defaultClusterRenderer.minClusterSize = 10
                            clusterManager.cluster()
                        }

                        Log.d("MyMap", "marker collection count: ${clusterManager.clusterMarkerCollection.markers}")
                        clusterManager.setOnClusterItemClickListener {
                            lastAddedMarker.value?.remove()
                            map.setOnInfoWindowClickListener(clusterManager)
                            false
                        }
                        clusterManager.setOnClusterItemInfoWindowClickListener { item ->
                            map.setOnInfoWindowClickListener(clusterManager)
                            cameraPositionState.position = map.cameraPosition
                            onReviewClick(item.id) // onReviewClick(item.placeId)
                        }
                        map.setOnCameraIdleListener(clusterManager)
                        map.isMyLocationEnabled = true
                        map.uiSettings.isMyLocationButtonEnabled = true
                        map.setOnMyLocationButtonClickListener {
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f))
                            true
                        }
                    }
                }
            },
            update = { mapView ->
                googleMap.value?.let { map ->
                    Log.d("update", "Map updated")
                    if(map.cameraPosition.target == LatLng(0.0, 0.0))
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f))

                    if(lastMarkerUpdated)
                        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPositionState.position))
                        lastAddedMarker.value?.showInfoWindow()
                        lastMarkerUpdated = false

                }
            }
        )
    }
    Log.d("MyMap", "Reload")
}


class MyItem(private val position: LatLng, private val title: String, val placeId: String, val id: Int) : ClusterItem {
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
    Log.d("MyMap", "Distance between $latLng1 and $latLng2 ${placeResult.name}: ${placeResult.place_id}")
    return results[0]
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeScreenPreview(){
    HomeScreen("nickname", context = TODO(), onReviewClick = { })
}