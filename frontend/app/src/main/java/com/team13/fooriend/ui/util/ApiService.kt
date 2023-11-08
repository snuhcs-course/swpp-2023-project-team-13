package com.team13.fooriend.ui.util

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/reviews/friends/restaurants")
    suspend fun getFriendsRestaurants(): RestaurantsResponse

    @GET("/reviews/restaurants/{restaurantId}")
    suspend fun getRestaurantDetail(
        @Path("restaurantId") restaurantPlaceId: String
    ): RestaurantDetailResponse

    @GET("/reviews/users/{userId}")
    suspend fun getUserDetail(
        @Path("userId") userId: Int
    ): UserDetailResponse
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

data class UserDetailResponse(
    val reviewList: List<Review>
)
