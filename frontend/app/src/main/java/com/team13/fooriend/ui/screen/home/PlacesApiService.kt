package com.team13.fooriend.ui.screen.home

import com.google.android.gms.maps.model.LatLng
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApiService {
    @GET("maps/api/place/nearbysearch/json")
    suspend fun searchNearbyPlaces(
        @Query("location") location: String,
        @Query("radius") radius: Int = 300, // 100미터 반경
        @Query("keyword") keyword: String = "restaurant", // 음식점
        @Query("key") apiKey: String
    ): NearbySearchResponse

    @GET("maps/api/place/details/json")
    suspend fun getPlaceDetails(
        @Query("place_id") placeId: String,
        @Query("key") apiKey: String
    ): PlaceResults
}

data class NearbySearchResponse(
    val results: List<PlaceResult>
)

data class PlaceResults(
    val result: PlaceResult
)

data class PlaceResult(
    val place_id: String,
    val geometry: Geometry,
    val name: String,
    val types: List<String>,
)

data class Geometry(
    val location: Map<String, Double>
)

//data class PlaceDetailsResponse(
//    val result: PlaceDetailsResult
//)
//
//data class PlaceDetailsResult(
//    val name: String  // 추가로 필요한 다른 정보들도 여기에 정의할 수 있습니다.
//    val geometry: Geometry
//)
//
//data class Geometry(
//    val location: LatLng
//)

