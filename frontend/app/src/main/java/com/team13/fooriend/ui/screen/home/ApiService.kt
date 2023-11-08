package com.team13.fooriend.ui.screen.home

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/reviews/friends/restaurants")
    suspend fun getFriendsRestaurants():RestaurantsResponse

    @GET("/reviews/restaurants/{restaurantId}")
    suspend fun getRestaurantDetail(
        @Path("restaurantId") restaurantPlaceId: String
    ):RestaurantDetailResponse
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

data class RestaurantDetailResponse(
    val reviewList: List<Review>
)

data class Review(
    val id: Int,
    val content: String,
    val images: List<Image>,
    val receiptImage: Image?,
    val issuedAt: String,
    val restaurant: Restaurant,
    val user: User
)

data class Image(
    val id: Int,
    val url: String,
    val isReceipt: Boolean,
    val isReceiptVerified: Boolean
)

data class User(
    val id: Int,
    val name: String
)
