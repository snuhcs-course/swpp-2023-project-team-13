package com.team13.fooriend.ui.screen.home

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("/reviews/friends/restaurants")
    suspend fun getFriendsRestaurants():RestaurantsResponse
}

data class RestaurantsResponse(
    val restaurantList: List<Restaurant>
)
data class Restaurant(
    val id: Int,
    val googleMapPlaceId: String,
    val longitude: Double,
    val latitude: Double,
    val name: String
)