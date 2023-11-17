package com.team13.fooriend.ui.util

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @GET("/reviews/friends/restaurants")
    suspend fun getFriendsRestaurants(): RestaurantsResponse

    @GET("/reviews/restaurants/{restaurantId}")
    suspend fun getRestaurantDetail(
        @Path("restaurantId") restaurantPlaceId: String
    ): RestaurantDetailResponse

    @GET("/reviews/users/{userId}")
    suspend fun getUserReviews(
        @Path("userId") userId: Int
    ): UserDetailResponse

    @GET("/user/{userId}")
    suspend fun getUserDetail(
        @Path("userId") userId: Int
    ): User

    @GET("/reviews/{reviewId}")
    suspend fun getReviewDetail(
        @Path("reviewId") reviewId: Int
    ): Review

    @GET("/reviews/random")
    suspend fun getRandomReviews(): RandomReviews

    @GET("/reviews/my")
    suspend fun getMyReviews(): MyReviews

    @GET("/user/me")
    suspend fun getMyInfo(): User

    @POST("/reviews")
    suspend fun postReview(@Body reviewPostBody: ReviewPostBody): Response<ResponseBody>

    @Multipart
    @POST("/reviews/images")
    suspend fun uploadImage(@Part file: MultipartBody.Part): ImageResponse


    @POST("/auth/login")
    suspend fun login(@Body loginBody: LoginBody): LoginResponse

    @POST("/user")
    suspend fun register(@Body registerBody: RegisterBody): Response<ResponseBody>

    @DELETE("/reviews/{reviewId}")
    suspend fun deleteReview(
        @Path("reviewId") reviewId: Int
    ): Response<ResponseBody>

    @GET("/user/search/{name}")
    suspend fun searchUser(
        @Path("name") name: String
    ): SearchResponse

    @GET("/user/follow/{userId}")
    suspend fun getFollows(
        @Path("userId") userId: Int
    ): FollowResponse

    @PUT("/user/follow/{userId}")
    suspend fun follow(
        @Path("userId") userId: Int
    ): Response<ResponseBody>
}
data class FollowResponse(
    val followers: List<AbstractUser>,
    val followings: List<AbstractUser>
)
data class SearchResponse(
    val userList: List<AbstractUser>
)
data class RegisterBody(
    val name: String,
    val username: String,
    val password: String
)
data class LoginBody(
    val username: String,
    val password: String
)

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String
)

data class ImageResponse(
    val id: Int,
    val url: String,
    val isReceiptVerified: Boolean
)

data class ReviewPostBody(
    val content: String,
    val imageIds: List<Int>,
    val receiptImageId: Int,
    val restaurant: RestaurantInfo
)

data class RestaurantInfo(
    val googleMapPlaceId: String,
    val name: String,
    val latitude: Double,
    val longitude: Double
)
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
    val isPositive: Boolean,
    val user: AbstractUser
)

data class Image(
    val id: Int,
    val url: String,
    val isReceipt: Boolean,
    val isReceiptVerified: Boolean
)

data class User(
    val id: Int,
    val profileImage: String,
    val name: String,
    val username: String,
    val followerCount: Int,
    val followingCount: Int,
)

data class AbstractUser(
    val id: Int,
    val name: String,
)

data class UserDetailResponse(
    val reviewList: List<Review>
)

data class RandomReviews(
    val reviewList: List<Review>
)

data class MyReviews(
    val reviewList: List<Review>
)